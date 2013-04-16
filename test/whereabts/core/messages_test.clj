(ns whereabts.core.messages-test
	(:use
		[midje.sweet]
		[whereabts.models.user-message]
		[whereabts.models.message]
		[whereabts.core.messages]
		[whereabts.util.geo]))

(def messages [{:_id "123" :loc [12.12 12.12] :title "title" :created-at "1.1.2013" :nick "jamppa" :message "looong message"}])
(def expected-all-messages {:messages [{:_id "123" :loc [12.12 12.12] :short-message "title" :created-at "1.1.2013"}]})
(def message {})
(def saved-message {})

(def bbox (bounding-box [1 1] [1 1]))

(fact "should find all messages by bounding box and compactify them"
	(find-all-messages-by-bbox bbox) => expected-all-messages
	(provided (find-messages-by-bbox bbox) => messages :times 1))

(fact "should save new message"
	(save-new-message message) => saved-message
	(provided (save-message message) => saved-message :times 1))

(fact "should find a message by id"
	(find-message "123abc") => message
	(provided (find-message-by-id "123abc") => message :times 1))

