(ns whereabts.models.profile-test
	(:use
		[midje.sweet]
		[whereabts.models.profile]
		[whereabts.db-helper]
		[whereabts.db.user-test-fixtures]
		[whereabts.models.util]))

(background (before :facts (setup-test-db)))


(def profile {:user_id (:_id test-user-a) :nick "testman" :country "fi" :description "testman from testland"})
(def profile-missing-nick (dissoc profile :nick))
(def profile-missing-country (dissoc profile :country))
(def profile-missing-description (dissoc profile :description))
(def profile-missing-user (dissoc profile :user_id))
(def profile-with-empty-desc (merge profile {:description ""}))

(fact "should find profile by id"
	(find-profile-by-id "509d513f61395f0ebbd5e58a") => test-profile-a)

(fact "should not find profile by id when such doesnt exist"
	(find-profile-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should not find profile by id when trying to find with nil id"
	(find-profile-by-id nil) => nil)

(fact "should save profile"
	(let [saved-profile (save-profile profile)]
		(find-profile-by-id (obj-id-as-str saved-profile)) => saved-profile))

(fact "should not save profile when nick is missing"
	(save-profile profile-missing-nick) => (throws IllegalArgumentException))

(fact "should not save profile when country is missing"
	(save-profile profile-missing-country) => (throws IllegalArgumentException))

(fact "should not save profile when description is missing"
	(save-profile profile-missing-description) => (throws IllegalArgumentException))

(fact "should save profile with empty description"
	(let [saved-profile (save-profile profile-with-empty-desc)]
		(find-profile-by-id (obj-id-as-str saved-profile)) => saved-profile))

(fact "should not save profile with missing user_id"
	(save-profile profile-missing-user) => (throws IllegalArgumentException))