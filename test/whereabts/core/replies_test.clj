(ns whereabts.core.replies-test
	(:use
		[midje.sweet]
		[whereabts.core.replies]
		[whereabts.core.profiles]
		[whereabts.core.with-util]
		[whereabts.models.reply]
		[whereabts.models.message]
		[whereabts.models.util]
		[whereabts.notification.core]))

(def user {:_id 1})
(def message {:message_id 1})

(def reply {:replymessage "asdasd"})
(def reply-created-now (merge reply {:created-at 123123123}))
(def reply-with-user (merge reply-created-now {:user_id 1}))
(def reply-with-message (merge reply-with-user {:message_id 1}))
(def saved-reply (merge reply-with-message {:_id 1}))

(fact "should save reply for message with user and update message with current timestamp"
	(save-reply-to-message reply user message) => saved-reply
	(provided (save-reply reply user message) => saved-reply :times 1)
	(provided (updated-now message) => message :times 1)
	(provided (update-message message) => message :times 1)
	(provided (notify-on-reply-if-not-owner saved-reply user message) => message :times 1))

(fact "should return message with replies that are mapped with user profile"
	(with-replies message) => (merge message {:replies [reply-with-message]})
	(provided (find-replies-by-message message) => [reply-with-message] :times 1)
	(provided (with-user-profile anything) => reply-with-message :times 1))

(fact "should notify on reply when not owner of the replied message"
	(notify-on-reply-if-not-owner reply user message) => reply
	(provided (replying-to-own-message? message user) => false :times 1)
	(provided (notify-on-reply reply user message) => reply :times 1))

(fact "should not notify on reply when owner of the replied message"
	(notify-on-reply-if-not-owner reply user message) => nil
	(provided (replying-to-own-message? message user) => true :times 1)
	(provided (notify-on-reply reply user message) => anything :times 0))

(fact "should be replying to own message when user is the owner of the message"
	(replying-to-own-message? message user) => true
	(provided (with-ownership message user) => (merge message {:owns true})))

(fact "should not be replying to own messag when user is not the owner of the message"
	(replying-to-own-message? message user) => false
	(provided (with-ownership message user) => (merge message {:owns false})))