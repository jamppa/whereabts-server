(ns whereabts.models.anon-message-test
	(:use
		[midje.sweet]
		[whereabts.models.anon-message]
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
(def msg-nick-too-long
	(merge anon-message {:nick "Termitekiller12345678"}))
(def msg-missing-title (dissoc anon-message :title))
(def msg-extra-kv (merge anon-message {:some "bullshit"}))

(background (before :facts (setup-test-db)))

(fact "should find anonymous message by id"
	(find-anon-message-by-id "509d513f61395f0ebbd5e36a") => test-anon-message-a)

(fact "should not find anonymous message by id when one does not exist"
	(find-anon-message-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should save valid anonymous message"
	(let [saved-message (save-anon-message anon-message)]
		(find-anon-message-by-id (obj-id-as-str saved-message)) => saved-message))

(fact "should not save invalid anonymous message missing title but throw IllegalArgumentException"
	(save-anon-message msg-missing-title) => (throws IllegalArgumentException))

(fact "sould not save invalid anonymous message with too long title but throw IllegalArgumentException"
	(save-anon-message msg-title-too-long) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous message with too long message but throw IllegalArgumentException"
	(save-anon-message msg-too-long) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous message with too long nick but throw IllegalArgumentException"
	(save-anon-message msg-nick-too-long) => (throws IllegalArgumentException))

(fact "should find anonymous messages by bounding box sorted by creation time"
	(find-anon-messages-by-bbox (bounding-box [0 0] [10 10])) => [test-anon-message-b test-anon-message-a])

(fact "should not find anonymous messages by bounding box when there isnt any"
	(find-anon-messages-by-bbox (bounding-box [45 34] [23 56])) => [])

(fact "should save valid anonymous message with empty title"
	(let [saved-message (save-anon-message msg-with-empty-title)]
		(find-anon-message-by-id (obj-id-as-str saved-message)) => saved-message))

(fact "should compactify anonymous message extracting short-message"
	(keys (compactify-anon-message anon-message-with-obj-id)) => '(:_id :loc :short-message :created-at)
	(provided (short-message anon-message-with-obj-id) => "short message" :times 1))

(fact "should create a copy of anonymous message"
	(new-anon-message anon-message) => anon-message-after-copy)

(fact "should ignore uninterested keys when copying new anonymous message"
	(new-anon-message msg-extra-kv) => anon-message-after-copy)