(ns whereabts.notification.like-notification
	(:use 
		whereabts.notification.core
		whereabts.models.util))

(def like-channel "message.likes")

(defn prepare-like-message-for-channel [message user]
	{
		:user-id (obj-id-as-str user)
		:message-id (obj-id-as-str message)})

(defn publish-like-message [message user]
	(publish-message like-channel 
		(prepare-like-message-for-channel message user)))