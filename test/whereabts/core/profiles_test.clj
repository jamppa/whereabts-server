(ns whereabts.core.profiles-test
	(:use
		[whereabts.core.profiles]
		[whereabts.core.users]
		[whereabts.models.profile]
		[midje.sweet])
	(:import [org.bson.types ObjectId]))

(def user {:_id "123" :profile_id (ObjectId.)})
(def user-without-profile (merge user {:profile_id "0"}))
(def profile {:_id "abc" :nick "testman"})

(fact "should just update profile when user already has one"
	(save-user-profile user profile) => anything
	(provided (update-profile user profile) => profile :times 1)
	(provided (create-profile user profile) => anything :times 0))

(fact "should create profile for user when she doesn't have one"
	(save-user-profile user-without-profile profile) => anything
	(provided (create-profile user-without-profile profile) => profile :times 1)
	(provided (update-profile user-without-profile profile) => anything :times 0))

(fact "should merge profile id from user and save it when updating profile"
	(update-profile user profile) => profile
	(provided (save-profile (merge profile {:_id (:profile_id user)})) => profile :times 1))

(fact "should save profile and update it to user when creating profile"
	(create-profile user-without-profile profile) => profile
	(provided (save-profile profile) => profile :times 1)
	(provided (update-profile-for-user user-without-profile profile) => user :times 1))