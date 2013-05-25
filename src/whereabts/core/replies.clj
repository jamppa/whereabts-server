(ns whereabts.core.replies
	(:use
		[whereabts.models.reply]
		[whereabts.models.util]
		[whereabts.core.with-util]))

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