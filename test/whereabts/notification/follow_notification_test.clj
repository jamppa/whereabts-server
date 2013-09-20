(ns whereabts.notification.follow-notification-test
	(:use 
		midje.sweet
		whereabts.notification.follow-notification
		whereabts.notification.core
		whereabts.db.user-test-fixtures))

(def follower test-user-a)
(def following test-user-b)

(def follow-channel-message {
	:follower-id (.toString (:_id test-user-a))
	:following-id (.toString (:_id test-user-b))
	})

(fact "should prepare message for channel"
	(prepare-follow-message-for-channel follower following) => follow-channel-message)