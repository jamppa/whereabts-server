(ns whereabts.core.replies-test
	(:use
		[midje.sweet]
		[whereabts.core.replies]
		[whereabts.core.profiles]
		[whereabts.core.with-util]
		[whereabts.models.reply]
		[whereabts.models.message]
		[whereabts.models.util]
		[whereabts.notification.reply-notification]))

(def user {:_id 1})
(def message {:message_id 1})

(def reply {:replymessage "asdasd"})
(def reply-created-now (merge reply {:created-at 123123123}))
(def reply-with-user (merge reply-created-now {:user_id 1}))
(def reply-with-message (merge reply-with-user {:message_id 1}))
(def saved-reply (merge reply-with-message {:_id 1}))

(fact "should save reply for message with user and update message with current timestamp"
	(save-reply-to-message reply user message) => saved-reply
	(provided 
		(save-reply reply user message) => saved-reply :times 1
		(updated-now message) => message :times 1
		(update-message message) => message :times 1
		(publish-reply-notification saved-reply message) => message :times 1
		(with-user-profile saved-reply) => saved-reply :times 1))

(fact "should return message with replies that are mapped with user profile"
	(with-replies message) => (merge message {:replies [reply-with-message]})
	(provided 
		(find-replies-by-message message) => [reply-with-message] :times 1
		(with-user-profile anything) => reply-with-message :times 1))

(fact "should be replying to own message when user is the owner of the message"
	(replying-to-own-message? message user) => true
	(provided (with-ownership message user) => (merge message {:owns true})))

(fact "should not be replying to own messag when user is not the owner of the message"
	(replying-to-own-message? message user) => false
	(provided (with-ownership message user) => (merge message {:owns false})))