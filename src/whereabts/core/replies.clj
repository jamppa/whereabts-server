(ns whereabts.core.replies
	(:use
		[whereabts.models.reply]
		[whereabts.models.message]
		[whereabts.models.util]
		[whereabts.core.with-util]
		[whereabts.core.profiles]
		[whereabts.notification.reply-notification]))

(defn- new-reply [candidate]
	(select-keys candidate
		[:message_id :user_id :nick :replymessage :created-at]))

(defn save-reply [reply user message]
	(-> reply
		(created-now)
		(with-user user)
		(with-message message)
		(new-reply)
		(save-new-reply)))

(defn save-reply-to-message [reply user message]
	(let [saved-reply (save-reply reply user message)]
		(update-message (updated-now message))
		(publish-reply-notification saved-reply message)
		(with-user-profile saved-reply)))

(defn with-replies [message]
	(merge message {
		:replies (map with-user-profile (find-replies-by-message message))}))