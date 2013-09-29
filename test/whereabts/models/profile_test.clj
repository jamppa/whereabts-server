(ns whereabts.models.profile-test
	(:use
		[midje.sweet]
		[whereabts.models.profile]
		[whereabts.db-helper]
		[whereabts.db.user-test-fixtures]
		[whereabts.models.util])
	(:import [org.bson.types ObjectId]))

(background (before :facts (setup-test-db)))

(def profile {:user_id (:_id test-user-a) :nick "testman" :country "fi" :description "testman from testland" :photo "me.jpg"})
(def profile-missing-nick (dissoc profile :nick))
(def profile-missing-country (dissoc profile :country))
(def profile-missing-description (dissoc profile :description))
(def profile-missing-user (dissoc profile :user_id))
(def profile-with-empty-desc (merge profile {:description ""}))
(def profile-with-empty-photo (merge profile {:photo ""}))

(fact "should find profile by id"
	(find-profile-by-id "509d513f61395f0ebbd5e58a") => test-profile-a)

(fact "should not find profile by id when such doesnt exist"
	(find-profile-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should save profile"
	(let [saved-profile (save-profile profile)]
		(find-profile-by-id (obj-id-as-str saved-profile)) => saved-profile))

(fact "should not save profile when nick is missing"
	(save-profile profile-missing-nick) => (throws IllegalArgumentException))

(fact "should not save profile when nil nick"
	(save-profile (assoc-in profile [:nick] nil)) => (throws IllegalArgumentException))

(fact "should not save profile when country is missing"
	(save-profile profile-missing-country) => (throws IllegalArgumentException))

(fact "should not save profile when description is missing"
	(save-profile profile-missing-description) => (throws IllegalArgumentException))

(fact "should save profile with empty description"
	(let [saved-profile (save-profile profile-with-empty-desc)]
		(find-profile-by-id (obj-id-as-str saved-profile)) => saved-profile))

(fact "should save profile with empty photo"
	(let [saved-profile (save-profile profile-with-empty-photo)]
		(find-profile-by-id (obj-id-as-str saved-profile)) => saved-profile))

(fact "should not save profile with missing user_id"
	(save-profile profile-missing-user) => (throws IllegalArgumentException))

(fact "should find profile by user_id"
	(let [user-id (:_id test-user-a)]
		(find-profile-by-user-id user-id) => test-profile-a))

(fact "should not find profile by user_id when one doesnt exist"
	(let [false-user-id (ObjectId.)]
		(find-profile-by-user-id false-user-id) => nil))

(fact "should not find profile by nil user_id"
	(find-profile-by-user-id nil) => nil)

(fact "should find profiles by user ids"
	(find-profiles-by-user-ids ["509d513f61395f0ebbd5e38a" "509d513f61395f0ebbd5e38b"]) => [test-profile-a test-profile-b])

(fact "should find 2 most recent profiles"
	(find-profiles-recent 2) => [test-profile-c test-profile-b])

(fact "should find most recent profile"
	(find-profiles-recent 1) => [test-profile-c])

(fact "should find two profiles by user name"
	(find-profiles-by-name "man") => [test-profile-a test-profile-c])

(fact "should find one profile by user name"
	(find-profiles-by-name "sEPpo") => [test-profile-b])

(fact "should not find any profile by name when one doesnt exist"
	(find-profiles-by-name "teppo") => [])