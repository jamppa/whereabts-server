(ns whereabts.notification.reply-notification-test
	(:use 
		whereabts.notification.reply-notification
		whereabts.notification.core
		midje.sweet
		whereabts.db.test-fixtures
		whereabts.db.reply-test-fixtures))

(fact "should prepare message for redis channel"
	(prepare-message-for-channel test-reply-a test-message-a) => 
		{:message-id "509d513f61395f0ebbd5e36a" 
		 :user-id "509d513f61395f0ebbd5e38a"
		 :reply-id "509d513f61395f0ebbd5e40a"})

(fact "should publish reply notification message"
	(publish-reply-notification test-reply-a test-message-a) => anything
	(provided
		(prepare-message-for-channel test-reply-a test-message-a) => anything :times 1
		(publish-message "message.replies" anything) => anything :times 1))