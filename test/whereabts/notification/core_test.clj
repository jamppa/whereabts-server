(ns whereabts.notification.core-test
	(:use 
		whereabts.notification.core 
		midje.sweet
		whereabts.db.test-fixtures
		whereabts.db.reply-test-fixtures))

(fact "should prepare message for redis channel"
	(prepare-message-for-channel test-reply-a test-message-a) => 
		{:replied-message "509d513f61395f0ebbd5e36a" 
		 :user-to-notify "509d513f61395f0ebbd5e38a"
		 :reply "509d513f61395f0ebbd5e40a"})