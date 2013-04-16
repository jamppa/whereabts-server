(ns whereabts.core.messages-test
	(:use
		[midje.sweet]
		[whereabts.models.message]
		[whereabts.models.anon-message]
		[whereabts.core.messages]
		[whereabts.util.geo]))

(def anon-messages [{:_id "123" :loc [12.12 12.12] :title "title" :created-at "1.1.2013" :nick "jamppa" :message "looong message"}])
(def expected-all-messages {:messages [{:_id "123" :loc [12.12 12.12] :short-message "title" :created-at "1.1.2013"}]})
(def anon-msg {:msg-type :anonymous})
(def user-msg {:msg-type :user})
(def saved-msg {})

(def bbox (bounding-box [1 1] [1 1]))

(fact "should find all messages by bounding box and compactify them"
	(find-all-messages-by-bbox bbox) => expected-all-messages
	(provided (find-anon-messages-by-bbox bbox) => anon-messages :times 1))

(fact "should save new anonymous message"
	(save-new-message anon-msg) => saved-msg
	(provided (save-anon-message anon-msg) => saved-msg :times 1))

(fact "should save new user message"
	(save-new-message user-msg) => saved-msg
	(provided (save-message user-msg) => saved-msg :times 1))

(fact "should stamp object as anonymous message"
	(anonymous-message {}) => {:msg-type :anonymous})
