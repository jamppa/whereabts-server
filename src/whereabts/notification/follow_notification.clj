(ns whereabts.notification.follow-notification
	(:use 
		whereabts.notification.core
		whereabts.models.util))

(def follow-channel "users.follow")

(defn prepare-follow-message-for-channel [follower following]
	{
		:follower-id (obj-id-as-str follower)
		:following-id (obj-id-as-str following)})

(defn publish-follow-message [follower following]
	(->> 
		(prepare-follow-message-for-channel follower following)
		(publish-message follow-channel))
	follower)