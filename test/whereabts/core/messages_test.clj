(ns whereabts.core.messages-test
	(:use
		[midje.sweet]
		[whereabts.models.message]
		[whereabts.core.messages]
		[whereabts.core.expirations]
		[whereabts.core.replies]
		[whereabts.core.categories]
		[whereabts.util.geo])
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(def user {:_id "some"})
(def other-user {:_id "other"})

(def messages [{:_id "123" :loc [12.12 12.12] :title "title" :created-at "1.1.2013" :nick "jamppa" :message "looong message"}])
(def expected-all-messages {:messages [{:_id "123" :loc [12.12 12.12] :short-message "title" :created-at "1.1.2013"}]})
(def message {:views 1})
(def message-with-user (merge message {:user_id (:_id user)}))
(def message-with-user-and-ownership (merge message-with-user {:owns true}))
(def message-with-user-and-replies (merge message-with-user-and-ownership {:replies []}))
(def viewed-message (merge message {:views 2}))
(def saved-message {})
(def compactified-saved-message saved-message)

(def bbox (bounding-box [1 1] [1 1]))

(fact "should find all messages by bounding box and compactify them"
	(find-all-messages-by-bbox bbox) => expected-all-messages
	(provided (find-messages-by-bbox bbox) => messages :times 1))

(fact "should save new message and return it compactified"
	(save-new-message message user) => saved-message
	(provided (with-category message-with-user) => message-with-user :times 1)
	(provided (expires-at message-with-user) => message-with-user :times 1)
	(provided (save-message message-with-user) => saved-message :times 1)
	(provided (compactify-message saved-message) => compactified-saved-message :times 1))

(fact "should find a message with replies and user by id"
	(find-message "123abc" user) => message-with-user-and-replies
	(provided (find-message-by-id "123abc") => message-with-user :times 1)
	(provided (with-replies message-with-user-and-ownership) => message-with-user-and-replies :times 1))

(fact "should throw exception when message is not found by id"
	(find-message "123abc" user) => (throws WhereabtsResourceNotFoundException)
	(provided (find-message-by-id "123abc") => nil :times 1))

(fact "should view the message by incrementing views counter"
	(view-message message) => (merge message {:views 2}))

(fact "should view and update message"
	(view-and-update-message message) => saved-message
	(provided (view-message message) => viewed-message :times 1)
	(provided (update-message viewed-message) => saved-message :times 1))

(fact "should delete and update message when user owns it"
	(delete-message "123abc" user) => message-with-user
	(provided (find-message-by-id "123abc") => message-with-user :times 1)
	(provided (user-owns-message? message-with-user user) => true :times 1)
	(provided (delete-and-update-message message-with-user) => message-with-user :times 1))

(fact "should not delete and update message when user does not own it"
	(delete-message "123abc" other-user) => message-with-user
	(provided (find-message-by-id "123abc") => message-with-user :times 1)
	(provided (user-owns-message? message-with-user other-user) => false :times 1)
	(provided (delete-and-update-message message-with-user) => message-with-user :times 0))

(fact "should own message when messages user id and users id match"
	(user-owns-message? message-with-user user) => true)

(fact "should not own message when messages user-id and users id does not match"
	(user-owns-message? message-with-user other-user) => false)