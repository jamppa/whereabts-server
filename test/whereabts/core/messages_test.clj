(ns whereabts.core.messages-test
	(:use
		[midje.sweet]
		[whereabts.models.message]
		[whereabts.models.reply]
		[whereabts.core.messages]
		[whereabts.core.replies]
		[whereabts.core.categories]
		[whereabts.core.profiles]
		[whereabts.core.with-util]
		[whereabts.util.geo])
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(def user {:_id "some"})
(def other-user {:_id "other"})

(def message {:_id "123abc" :views 1 :created-at 1})
(def messages [message])
(def expected-all-messages {:messages [message]})
(def message-with-user (merge message {:user_id (:_id user)}))
(def message-with-user-and-ownership (merge message-with-user {:owns true}))
(def message-with-user-and-replies (merge message-with-user-and-ownership {:replies []}))
(def viewed-message (merge message {:views 2}))
(def saved-message {})
(def compactified-saved-message saved-message)

(def bbox (bounding-box [1 1] [1 1]))

(fact "should find all messages by bounding box and compactify them"
	(find-all-messages-by-bbox bbox) => expected-all-messages
	(provided 
		(find-messages-by-bbox bbox) => messages :times 1
		(compactify-message message) => message :times 1))

(fact "should find all messages by bounding box and category, and return them compactified"
	(find-all-messages-by-bbox-and-category bbox "traffic") => expected-all-messages
	(provided 
		(resolve-category-id "traffic") => 1 :times 1
		(find-messages-by-bbox-and-category bbox 1) => messages :times 1
		(compactify-message message) => message :times 1))

(fact "should save new message and return it compactified"
	(save-new-message message user) => saved-message
	(provided 
		(with-category message-with-user) => message-with-user :times 1
		(save-message message-with-user) => saved-message :times 1
		(compactify-message saved-message) => compactified-saved-message :times 1))

(fact "should find a message with user profile and replies by id"
	(find-message "123abc" user) => message-with-user-and-replies
	(provided (find-message-by-id "123abc") => message-with-user :times 1
		(with-liked anything user) => message-with-user-and-ownership :times 1
		(with-user-profile message-with-user-and-ownership) => message-with-user-and-ownership :times 1
		(with-replies message-with-user-and-ownership) => message-with-user-and-replies :times 1
		(with-expiration message-with-user-and-replies message-expiration-time-ms) => message-with-user-and-replies :times 1))

(fact "should throw exception when message is not found by id"
	(find-message "123abc" user) => (throws WhereabtsResourceNotFoundException)
	(provided 
		(find-message-by-id "123abc") => nil :times 1))

(fact "should view the message by incrementing views counter"
	(view-message message) => (merge message {:views 2}))

(fact "should view and update message"
	(view-and-update-message message) => saved-message
	(provided 
		(view-message message) => viewed-message :times 1
		(update-message viewed-message) => saved-message :times 1))

(fact "should delete message when user owns it"
	(delete-message "123abc" user) => message-with-user
	(provided 
		(find-message-by-id "123abc") => message-with-user :times 1
		(user-owns-message? message-with-user user) => true :times 1
		(delete-message-by-id "123abc") => anything :times 1
		(delete-replies-by-message message-with-user) => anything :times 1))

(fact "should not delete and update message when user does not own it"
	(delete-message "123abc" other-user) => message-with-user
	(provided 
		(find-message-by-id "123abc") => message-with-user :times 1
		(user-owns-message? message-with-user other-user) => false :times 1
		(delete-message-by-id "123abc") => anything :times 0
		(delete-replies-by-message message-with-user) => anything :times 0))

(fact "should own message when messages user id and users id match"
	(user-owns-message? message-with-user user) => true)

(fact "should not own message when messages user-id and users id does not match"
	(user-owns-message? message-with-user other-user) => false)

(def liked-message (merge message {:likes ["some"]}))
(fact "should like message and update it"
	(like-message "123abc" user) => liked-message
	(provided
		(find-message-by-id "123abc") => message :times 1
		(add-user-to-likes message user) => liked-message :times 1
		(update-message liked-message) => liked-message :times 1))

(fact "should add user id to messages likes"
	(add-user-to-likes message user) => liked-message)

(fact "should not double add user id to messages likes"
	(add-user-to-likes liked-message user) => liked-message)