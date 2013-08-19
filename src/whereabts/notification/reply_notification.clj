(ns whereabts.notification.reply-notification
	(:use 
		[whereabts.notification.core]
		[whereabts.models.util]))

(def reply-channel "message.replies")

(defn prepare-message-for-channel [reply message]
	{
		:message-id (obj-id-as-str message) 
		:user-id (id-as-str message :user_id)
		:reply-id (obj-id-as-str reply)})

(defn publish-reply-notification [reply message]
	(publish-message 
		reply-channel (prepare-message-for-channel reply message)))
