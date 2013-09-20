(ns whereabts.notification.follow-notification
	(:use 
		whereabts.notification.core
		whereabts.models.util))

(defn prepare-follow-message-for-channel [follower following]
	{
		:follower-id (obj-id-as-str follower)
		:following-id (obj-id-as-str following)})