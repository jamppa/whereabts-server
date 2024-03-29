(ns whereabts.models.message-test
	(:use
		[midje.sweet]
		[whereabts.models.message]
		[whereabts.db-helper]
		[whereabts.models.util]
		[whereabts.util.geo]
		[whereabts.db.test-fixtures]
		[whereabts.db.user-test-fixtures])
	(:import 
		[com.mongodb MongoException]
		[org.bson.types ObjectId]))

(def message {
	:user_id (:_id test-user-a)
	:message "This is the content of My Cool Message" 
	:loc {:lon 45.1 :lat 56.4}
	:views 0
	:category "happenings"
	:likes []
	:photo false})

(def existing-message (with-obj-id (updated-now (created-now message))))

(def msg-after-copy (merge message {:loc [45.1 56.4]}))
(def msg-with-obj-id (with-obj-id message))
(def msg-too-long (merge message {:message (clojure.string/join "" (repeat 251 "s"))}))
(def msg-extra-kv (merge message {:some "bullshit"}))
(def msg-loc-erronous (merge message {:loc "im invalid"}))
(def msg-lon-lat-str (merge message {:loc {:lon "123.323" :lat "blaa"}}))
(def msg-missing-user (dissoc message :user_id))

(background (before :facts (setup-test-db)))

(fact "should find  message by id"
	(find-message-by-id "509d513f61395f0ebbd5e36a") => test-message-a)

(fact "should not find message by id when one does not exist"
	(find-message-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should save valid message"
	(let [saved-message (save-message message)]
		(find-message-by-id (obj-id-as-str saved-message)) => saved-message))

(fact "should not save message with too long message but throw IllegalArgumentException"
	(save-message msg-too-long) => (throws IllegalArgumentException))

(fact "should not save message with location as a string"
	(save-message msg-loc-erronous) => (throws MongoException))

(fact "should not save message with longitute and latitude as a string"
	(save-message msg-lon-lat-str) => (throws MongoException))

(fact "should not save message without user-id"
	(save-message msg-missing-user) => (throws IllegalArgumentException))

(fact "should find fresh messages by bounding box"
	(find-messages-by-bbox (bounding-box [0 0] [10 10])) => [test-message-b test-message-a])

(fact "should not find messages by bounding box when there isnt any"
	(find-messages-by-bbox (bounding-box [45 34] [23 56])) => [])

(fact "should compactify message"
	(keys (compactify-message existing-message)) => '(:message :category :created-at :updated-at :loc :user_id :_id))

(fact "should create a copy of message"
	(new-message message) => msg-after-copy)

(fact "should ignore uninterested keys when copying new anonymous message"
	(new-message msg-extra-kv) => msg-after-copy)

(fact "should update valid message"
	(let [found-msg (find-message-by-id "509d513f61395f0ebbd5e36a")
		  updated (update-message (merge found-msg {:views 100}))]
		(find-message-by-id "509d513f61395f0ebbd5e36a") => (merge test-message-a {:views 100})))

(fact "should not update invalid message"
	(let [found-msg (find-message-by-id "509d513f61395f0ebbd5e36a")]
		(update-message (merge found-msg {:message ""})) => (throws IllegalArgumentException)))

(fact "should delete message"
	(let [message (find-message-by-id "509d513f61395f0ebbd5e36a")]
		(delete-message-by-id (:_id message))
		(find-message-by-id "509d513f61395f0ebbd5e36a") => nil))

(def user-a-id (:_id test-user-a))
(fact "should find messages by users skipping none"
	(find-messages-by-users [user-a-id] 0) => [test-message-b test-message-a])

(fact "should find messages by users skipping first"
	(find-messages-by-users [user-a-id] 1) => [test-message-a])

(fact "should find empty set of message by users when skipping all of them"
	(find-messages-by-users [user-a-id] 2) => [])

(def older-than (- (System/currentTimeMillis) (* 1000 60 60 10)))
(fact "should find messages by users skipping none and older than 10 hours"
	(find-messages-by-users-older-than [user-a-id] 0 older-than) => [test-message-a])

(def older-than (- (System/currentTimeMillis) (* 1000 60 60 24)))
(fact "should find empty messages by users skipping none and older than 24 hours"
	(find-messages-by-users-older-than [user-a-id] 0 older-than) => [])

(fact "should count messages of user"
	(count-messages-by-user user-a-id) => 2)

(fact "should count messages of nonexisting user as zero"
	(count-messages-by-user (ObjectId.)) => 0)