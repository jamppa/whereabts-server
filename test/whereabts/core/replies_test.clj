(ns whereabts.core.replies-test
	(:use
		[midje.sweet]
		[whereabts.core.replies]
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
	(provided (created-now reply) => reply-created-now :times 1)
	(provided (with-user reply-created-now user) => reply-with-user :times 1)
	(provided (with-message reply-with-user message) => reply-with-message :times 1)
	(provided (save-new-reply reply-with-message) => saved-reply :times 1)
	(provided (updated-now message) => message :times 1)
	(provided (update-message message) => message :times 1)
	(provided (notify-on-reply message) => message :times 1))

(fact "should return message with replies"
	(with-replies message) => (merge message {:replies [reply-with-message]})
	(provided (find-replies-by-message message) => [reply-with-message] :times 1))