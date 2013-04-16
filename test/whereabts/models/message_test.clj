(ns whereabts.models.message-test
	(:use
		[midje.sweet]
		[whereabts.models.message]
		[whereabts.db-helper]
		[whereabts.models.util]
		[whereabts.util.geo]
		[whereabts.db-test-fixtures]))

(def anon-message {
	:title "My Cool Message" 
	:message "This is the content of My Cool Message" 
	:nick "Cool Guy"
	:loc {:lon 45.1 :lat 56.4}
	:created-at (System/currentTimeMillis)})

(def anon-message-after-copy (merge anon-message {:loc [45.1 56.4]}))
(def anon-message-with-obj-id (with-obj-id anon-message))
(def msg-with-empty-title (merge anon-message {:title ""}))
(def msg-title-too-long 
	(merge anon-message {:title "this is title text that is unfortunately too looooooong"}))
(def msg-too-long
	(merge anon-message {:message (clojure.string/join "" (repeat 251 "s"))}))
(def msg-nick-too-long (merge anon-message {:nick "Termitekiller12345678"}))
(def msg-missing-title (dissoc anon-message :title))
(def msg-extra-kv (merge anon-message {:some "bullshit"}))
(def msg-loc-erronous (merge anon-message {:loc "im invalid"}))
(def msg-lon-lat-str (merge anon-message {:loc {:lon "123.323" :lat "blaa"}}))

(background (before :facts (setup-test-db)))

(fact "should find anonymous message by id"
	(find-message-by-id "509d513f61395f0ebbd5e36a") => test-message-a)

(fact "should not find anonymous message by id when one does not exist"
	(find-message-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should save valid anonymous message"
	(let [saved-message (save-message anon-message)]
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
	(save-message msg-loc-erronous) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous message with longitute and latitude as a string"
	(save-message msg-lon-lat-str) => (throws IllegalArgumentException))

(fact "should find anonymous messages by bounding box sorted by creation time"
	(find-anon-messages-by-bbox (bounding-box [0 0] [10 10])) => [test-message-b test-message-a])

(fact "should not find anonymous messages by bounding box when there isnt any"
	(find-anon-messages-by-bbox (bounding-box [45 34] [23 56])) => [])

(fact "should save valid anonymous message with empty title"
	(let [saved-message (save-message msg-with-empty-title)]
		(find-message-by-id (obj-id-as-str saved-message)) => saved-message))

(fact "should compactify anonymous message extracting short-message"
	(keys (compactify-message anon-message-with-obj-id)) => '(:_id :loc :short-message :created-at)
	(provided (short-message anon-message-with-obj-id) => "short message" :times 1))

(fact "should create a copy of anonymous message"
	(new-message anon-message) => anon-message-after-copy)

(fact "should ignore uninterested keys when copying new anonymous message"
	(new-message msg-extra-kv) => anon-message-after-copy)