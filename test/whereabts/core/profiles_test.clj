(ns whereabts.core.profiles-test
	(:use
		[whereabts.core.profiles]
		[whereabts.core.users]
		[whereabts.models.profile]
		[midje.sweet])
	(:import 
		[org.bson.types ObjectId]
		[whereabts.exception WhereabtsResourceNotFoundException]))

(def profile-id (ObjectId.))
(def user-id (ObjectId.))
(def profile-id-as-str (.toString profile-id))

(def user {:_id user-id :profile_id profile-id})
(def user-without-profile (dissoc user :profile_id))
(def profile {:_id profile-id :user_id user-id :nick "testman"})

(fact "should just update profile when user already has one"
	(save-user-profile user profile) => anything
	(provided (update-profile user profile) => profile :times 1)
	(provided (create-profile user profile) => anything :times 0))

(fact "should create profile for user when she doesn't have one"
	(save-user-profile user-without-profile profile) => anything
	(provided (create-profile user-without-profile profile) => profile :times 1)
	(provided (update-profile user-without-profile profile) => anything :times 0))

(fact "should merge profile with user and save it when updating profile"
	(update-profile user profile) => profile
	(provided (save-profile profile) => profile :times 1))

(fact "should save profile and update it to user when creating profile"
	(create-profile user-without-profile profile) => profile
	(provided (save-profile profile) => profile :times 1)
	(provided (set-profile-for-user user-without-profile profile) => user :times 1))

(fact "should find profile of a user"
	(find-user-profile user) => profile
	(provided (find-profile-by-id profile-id-as-str) => profile :times 1))

(fact "should throw exception when user doesnt have any profile"
	(find-user-profile user-without-profile) => (throws WhereabtsResourceNotFoundException)
	(provided (find-profile-by-id anything) => anything :times 0))

(def obj {:user_id user-id})
(def obj-with-user (merge obj {:user-profile profile}))
(fact "should return object with user-profile when object has user_id"
	(with-user-profile obj) => obj-with-user
	(provided (find-profile-by-user-id user-id) => profile))

(def obj-without-user {:some "thing"})
(fact "should return object without user-profile when object doesnt have user_id "
	(with-user-profile obj-without-user) => obj-without-user
	(provided (find-profile-by-user-id nil) => anything :times 0))