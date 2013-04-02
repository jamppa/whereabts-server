(ns whereabts.core.messages-test
	(:use
		[midje.sweet]
		[whereabts.models.message]
		[whereabts.models.anon-message]
		[whereabts.core.messages]
		[whereabts.util.geo]))

(def messages [{:message ""}])
(def anon-messages [{:message ""}])
(def expected-all-messages {:messages messages :anon-messages anon-messages})
(def anon-msg {:msg-type :anonymous})
(def user-msg {:msg-type :user})
(def saved-msg {})

(def bbox (bounding-box [1 1] [1 1]))

(fact "should find all messages by bounding box"
	(find-all-messages-by-bbox bbox) => expected-all-messages
	(provided (find-messages-by-bbox bbox) => messages :times 1)
	(provided (find-anon-messages-by-bbox bbox) => anon-messages :times 1))

(fact "should save new anonymous message"
	(save-new-message anon-msg) => saved-msg
	(provided (save-anon-message anon-msg) => saved-msg :times 1))

(fact "should save new user message"
	(save-new-message user-msg) => saved-msg
	(provided (save-message user-msg) => saved-msg :times 1))