(ns whereabts.models.profile-test
	(:use
		[midje.sweet]
		[whereabts.models.profile]
		[whereabts.db-helper]
		[whereabts.db.user-test-fixtures]
		[whereabts.models.util]))

(background (before :facts (setup-test-db)))


(def profile {:nick "testman" :country "fi" :description "testman from testland"})


(fact "should find profile by id"
	(find-profile-by-id "509d513f61395f0ebbd5e58a") => test-profile-a)

(fact "should find profile by user"
	(find-profile-by-user test-user-a) => test-profile-a)

(fact "should save valid profile"
	(let [saved-profile (save-profile profile)]
		(find-profile-by-id (obj-id-as-str saved-profile)) => saved-profile))