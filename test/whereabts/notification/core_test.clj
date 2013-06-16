(ns whereabts.notification.core-test
	(:use 
		whereabts.notification.core 
		midje.sweet
		whereabts.db.test-fixtures
		whereabts.db.reply-test-fixtures))

(fact "should prepare message for redis channel"
	(prepare-message-for-channel test-reply-a test-message-a) => 
		{:replied_message "509d513f61395f0ebbd5e36a" 
		 :user_to_notify "509d513f61395f0ebbd5e38a"
		 :reply "509d513f61395f0ebbd5e40a"})