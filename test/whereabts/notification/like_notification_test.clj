(ns whereabts.notification.like-notification-test
	(:use 
		midje.sweet
		whereabts.notification.like-notification))

(fact "should define channel name for like notifications"
	like-channel => "message.likes")

