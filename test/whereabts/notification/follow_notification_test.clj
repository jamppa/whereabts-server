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

(fact "should define correct channel name for follow"
	follow-channel => "users.follow")

(fact "should publish follow message to channel"
	(publish-follow-message follower following) => anything
	(provided
		(prepare-follow-message-for-channel follower following) => anything :times 1
		(publish-message follow-channel anything) => anything :times 1))