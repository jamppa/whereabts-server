(ns whereabts.models.profile-test
	(:use
		[midje.sweet]
		[whereabts.models.profile]
		[whereabts.db-helper]
		[whereabts.db.user-test-fixtures]))

(background (before :facts (setup-test-db)))

(fact "should find profile by id"
	(find-profile-by-id "509d513f61395f0ebbd5e58a") => test-profile-a)