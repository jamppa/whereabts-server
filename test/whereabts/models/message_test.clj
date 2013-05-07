(ns whereabts.models.message-test
	(:use
		[midje.sweet]
		[whereabts.models.message]
		[whereabts.db-helper]
		[whereabts.models.util]
		[whereabts.util.geo]
		[whereabts.db.test-fixtures])
	(:import [com.mongodb MongoException]))

(def message {
	:title "My Cool Message" 
	:message "This is the content of My Cool Message" 
	:nick "Cool Guy"
	:loc {:lon 45.1 :lat 56.4}
	:created-at (System/currentTimeMillis)
	:views 0})

(def msg-after-copy (merge message {:loc [45.1 56.4]}))
(def msg-with-obj-id (with-obj-id message))
(def msg-with-empty-title (merge message {:title ""}))
(def msg-title-too-long (merge message {:title "this is title text that is unfortunately too looooooong"}))
(def msg-too-long (merge message {:message (clojure.string/join "" (repeat 251 "s"))}))
(def msg-nick-too-long (merge message {:nick "Termitekiller12345678"}))
(def msg-missing-title (dissoc message :title))
(def msg-extra-kv (merge message {:some "bullshit"}))
(def msg-loc-erronous (merge message {:loc "im invalid"}))
(def msg-lon-lat-str (merge message {:loc {:lon "123.323" :lat "blaa"}}))

(background (before :facts (setup-test-db)))

(fact "should find anonymous message by id"
	(find-message-by-id "509d513f61395f0ebbd5e36a") => test-message-a)

(fact "should not find anonymous message by id when one does not exist"
	(find-message-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should save valid anonymous message"
	(let [saved-message (save-message message)]
		(find-message-by-id (obj-id-as-str saved-message)) => saved-message))

(fact "should not save invalid anonymous message missing title but throw IllegalArgumentException"
	(save-message msg-missing-title) => (throws IllegalArgumentException))

(fact "sould not save invalid anonymous message with too long title but throw IllegalArgumentException"
	(save-message msg-title-too-long) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous message with too long message but throw IllegalArgumentException"
	(save-message msg-too-long) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous message with too long nick but throw IllegalArgumentException"
	(save-message msg-nick-too-long) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous message with location as a string"
	(save-message msg-loc-erronous) => (throws MongoException))

(fact "should not save invalid anonymous message with longitute and latitude as a string"
	(save-message msg-lon-lat-str) => (throws MongoException))

(fact "should find anonymous messages by bounding box sorted by creation time"
	(find-messages-by-bbox (bounding-box [0 0] [10 10])) => [test-message-b test-message-a])

(fact "should not find anonymous messages by bounding box when there isnt any"
	(find-messages-by-bbox (bounding-box [45 34] [23 56])) => [])

(fact "should save valid anonymous message with empty title"
	(let [saved-message (save-message msg-with-empty-title)]
		(find-message-by-id (obj-id-as-str saved-message)) => saved-message))

(fact "should compactify anonymous message extracting short-message"
	(keys (compactify-message msg-with-obj-id)) => '(:_id :loc :short-message :created-at)
	(provided (short-message msg-with-obj-id) => "short message" :times 1))

(fact "should create a copy of anonymous message"
	(new-message message) => msg-after-copy)

(fact "should ignore uninterested keys when copying new anonymous message"
	(new-message msg-extra-kv) => msg-after-copy)

(fact "should update valid message"
	(let [found-msg (find-message-by-id "509d513f61395f0ebbd5e36a")
		  updated (update-message (merge found-msg {:views 100}))]
		(find-message-by-id "509d513f61395f0ebbd5e36a") => (merge test-message-a {:views 100})))

(fact "shoult not update invalid message"
	(let [found-msg (find-message-by-id "509d513f61395f0ebbd5e36a")]
		(update-message (merge found-msg {:message ""})) => (throws IllegalArgumentException)))