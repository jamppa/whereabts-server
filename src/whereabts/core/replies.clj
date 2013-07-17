(ns whereabts.core.replies
	(:use
		[whereabts.models.reply]
		[whereabts.models.message]
		[whereabts.models.util]
		[whereabts.core.with-util]
		[whereabts.core.profiles]
		[whereabts.notification.core]))

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

(defn replying-to-own-message? [message user]
	(let [msg (with-ownership message user)]
		(:owns msg)))

(defn notify-on-reply-if-not-owner [reply user message]
	(when (not (replying-to-own-message? message user))
		(notify-on-reply reply user message)))

(defn save-reply-to-message [reply user message]
	(let [saved-reply (save-reply reply user message)]
		(update-message (updated-now message))
		(notify-on-reply-if-not-owner saved-reply user message)
			(with-user-profile saved-reply)))

(defn with-replies [message]
	(merge message {
		:replies (map with-user-profile (find-replies-by-message message))}))