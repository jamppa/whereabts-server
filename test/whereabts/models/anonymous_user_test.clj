(ns whereabts.models.anonymous-user-test
	(:use
		[whereabts.models.anonymous-user]
		[whereabts.db.user-test-fixtures]
		[whereabts.db-helper]
		[midje.sweet]))

(background (before :facts (setup-test-db)))

(fact "should find anonymous user by id"
	(find-anonymous-user-by-id "509d513f61395f0ebbd5e38a") => anonymous-user-a)

