(ns whereabts.core.messages-test
	(:use
		[midje.sweet]
		[whereabts.models.message]
		[whereabts.models.reply]
		[whereabts.core.messages]
		[whereabts.core.replies]
		[whereabts.core.profiles]
		[whereabts.core.with-util]
		[whereabts.notification.like-notification]
		[whereabts.util.geo])
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(def user {:_id "some"})
(def other-user {:_id "other"})

(def message {:_id "123abc" :views 1 :created-at 1})
(def messages [message])
(def message-with-user (merge message {:user_id (:_id user)}))
(def message-with-user-and-ownership (merge message-with-user {:owns true}))
(def message-with-user-and-replies (merge message-with-user-and-ownership {:replies []}))
(def viewed-message (merge message {:views 2}))
(def saved-message {})
(def compactified-saved-message saved-message)

(def bbox (bounding-box [1 1] [1 1]))

(fact "should find messages by bounding box and compactify them with userprofiles"
	(find-all-messages-by-bbox bbox) => messages
	(provided 
		(find-messages-by-bbox bbox) => messages :times 1
		(compactify-messages messages) => messages :times 1
		(with-user-profiles messages) => messages :times 1))

(fact "should save new message and return it compactified"
	(save-new-message message user) => saved-message
	(provided 
		(save-message message-with-user) => saved-message :times 1
		(compactify-message saved-message) => saved-message :times 1
		(with-user-profile saved-message) => saved-message :times 1))

(fact "should find a message with user profile and replies by id"
	(find-message "123abc" user) => message-with-user-and-replies
	(provided 
		(find-message-by-id "123abc") => message-with-user :times 1
		(update-message anything) => message-with-user :times 1
		(with-liked anything user) => message-with-user-and-ownership :times 1
		(with-likes-as-number anything) => message-with-user-and-ownership :times 1
		(with-user-profile message-with-user-and-ownership) => message-with-user-and-ownership :times 1
		(with-replies message-with-user-and-ownership) => message-with-user-and-replies :times 1
		(with-expiration message-with-user-and-replies message-expiration-time-ms) => message-with-user-and-replies :times 1))

(fact "should throw exception when message is not found by id"
	(find-message "123abc" user) => (throws WhereabtsResourceNotFoundException)
	(provided 
		(find-message-by-id "123abc") => nil :times 1))

(fact "should view and update message"
	(view-and-update-message message) => saved-message
	(provided 
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
		(update-message liked-message) => liked-message :times 1
		(publish-like-message liked-message user) => liked-message :times 1))

(fact "should add user id to messages likes"
	(add-user-to-likes message user) => liked-message)

(fact "should not double add user id to messages likes"
	(add-user-to-likes liked-message user) => liked-message)

(def follower {:following ["123" "abc"]})
(def following (:following follower))
(fact "should find following messages of follower skipping some"
	(find-following-messages follower 1) => []
	(provided
		(find-messages-by-users following 1) => [] :times 1
		(compactify-messages []) => [] :times 1
		(with-user-profiles []) => [] :times 1))

(fact "should not find following messages of follower when followings is nil"
	(find-following-messages {} 1) => []
	(provided
		(find-messages-by-users nil 1) => anything :times 0))

(fact "should find following messages of follower skippipng and older than"
	(find-following-messages-older-than follower 0 123123) => []
	(provided
		(find-messages-by-users-older-than following 0 123123) => [] :times 1
		(compactify-messages []) => [] :times 1
		(with-user-profiles []) => [] :times 1))

(fact "should find messages of a user skipping some and older than"
	(find-users-messages-older-than "123qwe" 1 123123) => []
	(provided
		(find-messages-by-users-older-than ["123qwe"] 1 123123) => [] :times 1
		(compactify-messages []) => [] :times 1
		(with-user-profiles []) => [] :times 1))