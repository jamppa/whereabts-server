(ns whereabts.core.profiles-test
	(:use
		[whereabts.core.profiles]
		[whereabts.core.users]
		[whereabts.core.with-util]
		[whereabts.models.profile]
		[whereabts.models.user]
		[whereabts.db.test-fixtures]
		[whereabts.db.user-test-fixtures]
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
	(provided 
		(update-profile user profile) => profile :times 1
		(create-profile user profile) => anything :times 0))

(fact "should create profile for user when she doesn't have one"
	(save-user-profile user-without-profile profile) => anything
	(provided 
		(create-profile user-without-profile profile) => profile :times 1
		(update-profile user-without-profile profile) => anything :times 0))

(fact "should merge profile with user and save it when updating profile"
	(update-profile user profile) => profile
	(provided (save-profile profile) => profile :times 1))

(fact "should save profile and update it to user when creating profile"
	(create-profile user-without-profile profile) => profile
	(provided 
		(save-profile profile) => profile :times 1
		(set-profile-for-user user-without-profile profile) => user :times 1))

(fact "should find users profile"
	(find-user-profile user) => profile
	(provided (find-profile-by-id profile-id-as-str) => profile :times 1))

(fact "should throw exception when user doesnt have any profile"
	(find-user-profile user-without-profile) => (throws WhereabtsResourceNotFoundException)
	(provided (find-profile-by-id anything) => anything :times 0))

(def obj {:user_id user-id})
(def obj-with-user (merge obj {:user-profile profile}))
(fact "should return object with user-profile when object has user_id"
	(with-user-profile obj) => obj-with-user
	(provided 
		(find-profile-by-user-id user-id) => profile :times 1))

(def obj-without-user {:some "thing"})
(fact "should return object without user-profile when object doesnt have user_id "
	(with-user-profile obj-without-user) => obj-without-user
	(provided 
		(find-profile-by-user-id nil) => anything :times 0))

(fact "should merge object with given userprofile when profile is not nil"
	(with-user-profile obj profile) => obj-with-user
	(provided
		(find-profile-by-user-id anything) => anything :times 0))

(fact "should return nil when trying to merge with nil userprofile"
	(with-user-profile obj nil) => nil
	(provided
		(find-profile-by-user-id anything) => anything :times 0))

(def user-id "123abc")
(def auth-user {})
(def user-with-profile (merge user {:user-profile profile}))
(fact "should find profile of a user"
	(find-profile-of-user user-id auth-user) => user-with-profile
	(provided
		(find-user-by-id user-id) => user :times 1
		(find-user-profile user) => profile :times 1
		(with-followed? user-with-profile auth-user) => user-with-profile :times 1
		(with-following-as-number user-with-profile) => user-with-profile :times 1
		(with-followers-as-number user-with-profile) => user-with-profile :times 1))

(def message-a-with-profile (merge test-message-a {:user-profile test-profile-a}))
(def message-b-with-profile (merge test-message-c {:user-profile test-profile-b}))
(def messages [test-message-a test-message-c])
(def messages-with-profiles [message-a-with-profile message-b-with-profile])
(fact "should map objects with user profiles"
	(with-user-profiles messages) => messages-with-profiles
	(provided
		(find-profiles-by-user-ids anything) => [test-profile-a test-profile-b] :times 1))