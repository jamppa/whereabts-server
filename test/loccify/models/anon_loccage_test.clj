(ns loccify.models.anon-loccage-test
	(:use
		[midje.sweet]
		[loccify.models.anon-loccage]
		[loccify.db-helper]
		[loccify.models.util]
		[loccify.util.geo]
		[loccify.db-test-fixtures]))

(def valid-anon-message {:message "msg" :nick "Cool Guy" :loc [45.1 56.4]})
(def invalid-anon-message {:message "asdasd" :loc [12.2 34.4]})

(background (before :facts (setup-test-db)))

(fact "should find anonymous message by id"
	(find-anon-message-by-id "509d513f61395f0ebbd5e36a") => test-anon-message-a)

(fact "should not find anonymous message by id when one does not exist"
	(find-anon-message-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should save valid anonymous message"
	(let [saved-message (save-anon-message valid-anon-message)]
		(find-anon-message-by-id (obj-id-as-str saved-message)) => saved-message))

(fact "should not save invalid anonymous message"
	(save-anon-message invalid-anon-message) => nil)

(fact "should find anonymous messages by bounding box sorted by creation time"
	(find-anon-messages-by-bbox (bounding-box [0 0] [10 10])) => [test-anon-message-b test-anon-message-a])

(fact "should not find anonymous messages by bounding box when there isnt any"
	(find-anon-messages-by-bbox (bounding-box [45 34] [23 56])) => [])