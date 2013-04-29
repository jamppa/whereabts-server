(ns whereabts.core.messages-test
	(:use
		[midje.sweet]
		[whereabts.models.user-message]
		[whereabts.models.message]
		[whereabts.core.messages]
		[whereabts.util.geo])
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(def messages [{:_id "123" :loc [12.12 12.12] :title "title" :created-at "1.1.2013" :nick "jamppa" :message "looong message"}])
(def expected-all-messages {:messages [{:_id "123" :loc [12.12 12.12] :short-message "title" :created-at "1.1.2013"}]})
(def message {:views 1})
(def viewed-message (merge message {:views 2}))
(def saved-message {})
(def compactified-saved-message saved-message)

(def bbox (bounding-box [1 1] [1 1]))

(fact "should find all messages by bounding box and compactify them"
	(find-all-messages-by-bbox bbox) => expected-all-messages
	(provided (find-messages-by-bbox bbox) => messages :times 1))

(fact "should save new message and return it compactified"
	(save-new-message message) => saved-message
	(provided (save-message message) => saved-message :times 1)
	(provided (compactify-message saved-message) => compactified-saved-message :times 1))

(fact "should find a message by id"
	(find-message "123abc") => message
	(provided (find-message-by-id "123abc") => message :times 1))

(fact "should throw exception when message is not found by id"
	(find-message "123abc") => (throws WhereabtsResourceNotFoundException)
	(provided (find-message-by-id "123abc") => nil :times 1))

(fact "should view the message by incrementing views counter"
	(view-message message) => (merge message {:views 2}))

(fact "should view and update message"
	(view-and-update-message message) => saved-message
	(provided (view-message message) => viewed-message :times 1)
	(provided (update-message viewed-message) => saved-message :times 1))