(ns loccify.core.messages-test
	(:use
		[midje.sweet]
		[loccify.models.message]
		[loccify.models.anon-message]
		[loccify.core.loccages]
		[loccify.util.geo]))

(def messages [{:message ""}])
(def anon-messages [{:message ""}])
(def expected-all-messages {:messages messages :anon-messages anon-messages})

(def bbox (bounding-box [1 1] [1 1]))

(fact "should find all messages by bounding box"
	(find-all-messages-by-bbox bbox) => expected-all-messages
	(provided (find-messages-by-bbox bbox) => messages :times 1)
	(provided (find-anon-messages-by-bbox bbox) => anon-messages :times 1))