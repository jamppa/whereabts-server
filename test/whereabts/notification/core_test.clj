(ns whereabts.notification.core-test
	(:use 
		whereabts.notification.core 
		midje.sweet
		whereabts.db.test-fixtures))

(fact "should prepare message for redis channel"
	(prepare-message-for-channel test-message-a) => 
		{:message_id "509d513f61395f0ebbd5e36a" :user_id "509d513f61395f0ebbd5e38a"})