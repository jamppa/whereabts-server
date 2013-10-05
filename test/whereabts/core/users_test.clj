(ns whereabts.core.users-test
	(:use
		whereabts.core.users
		whereabts.models.user
		whereabts.models.profile
		whereabts.models.message
		whereabts.models.util
		midje.sweet)
	(:import [org.bson.types ObjectId]))

(def user {:_id "123"})
(def user-with-gcm (merge user {:gcm-id "aBCd"}))
(def user-with-nil-gcm (merge user {:gcm-id nil}))

(def profile {:_id (ObjectId.)})
(def user-with-profile (merge user {:profile_id (:_id profile)}))

(fact "should save new user"
	(save-user user) => user
		(provided 
			(created-now user) => user :times 1
			(last-seen-now user) => user :times 1
			(with-email-role user) => user :times 1
			(with-empty-followers user) => user :times 1
			(with-empty-followings user) => user :times 1
			(save-new-user user) => user :times 1))

(fact "should find user by email"
	(find-user-by-email "testman@testland.fi") => user
	(provided 
		(find-user {:email "testman@testland.fi"}) => user :times 1))

(fact "should update gcm for user"
	(update-gcm-for-user user "aBCd") => user-with-gcm
	(provided 
		(update-user user-with-gcm) => user-with-gcm :times 1))

(fact "should not update gcm for user when gcm is nil"
	(update-gcm-for-user user nil) => (throws IllegalArgumentException)
	(provided (update-user user-with-nil-gcm) => user-with-gcm :times 0))

(fact "should set profile for user"
	(set-profile-for-user user profile) => user-with-profile
	(provided (update-user user-with-profile) => user-with-profile :times 1))

(fact "should delete user"
	(delete-user user) => anything
	(provided
		(delete-user-by-id "123") => anything :times 1))

(def user-with-messages-count (merge user {:messages-count 1}))
(fact "should merge user with messages count"
	(with-messages-count user) => user-with-messages-count
	(provided
		(count-messages-by-user "123") => 1 :times 1))
