(ns whereabts.core.replies
	(:use
		[whereabts.models.reply]
		[whereabts.models.util]
		[whereabts.core.with-util]))

(defn save-reply [reply user message]
	(-> reply
		(created-now)
		(with-user user)
		(with-message message)
		(save-new-reply)))