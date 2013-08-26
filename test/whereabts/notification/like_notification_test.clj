(ns whereabts.notification.like-notification-test
	(:use 
		midje.sweet
		whereabts.notification.like-notification
		whereabts.notification.core
		whereabts.db.test-fixtures
		whereabts.db.user-test-fixtures))

(fact "should define channel name for like notifications"
	like-channel => "message.likes")

(def liked-message test-message-a)
(def user-who-liked test-user-a)
(def like-channel-message {
	:user-id (.toString (:_id user-who-liked))
	:message-owner-id (.toString (:user_id liked-message))
	:message-id (.toString (:_id liked-message))
	})

(fact "should prepare like message for channel"
	(prepare-like-message-for-channel liked-message user-who-liked) => like-channel-message)

(fact "should publish like message to channel"
	(publish-like-message liked-message user-who-liked) => liked-message
	(provided
		(prepare-like-message-for-channel liked-message user-who-liked) => anything :times 1
		(publish-message like-channel anything) => anything :times 1))