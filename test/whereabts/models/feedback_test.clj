(ns whereabts.models.feedback-test
	(:use 
		[midje.sweet]
		[whereabts.models.feedback]
		[whereabts.db-helper]
		[whereabts.db-test-fixtures]))

(background (before :facts (setup-test-db)))

(fact "should find a feedback by its id"
	(find-feedback-by-id "509d513f61395f0ebbd5e37a") => test-feedback-a)