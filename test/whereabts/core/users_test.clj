(ns whereabts.core.users-test
	(:use
		[whereabts.core.users]
		[whereabts.models.user]
		[whereabts.models.profile]
		[whereabts.models.util]
		[midje.sweet])
	(:import [org.bson.types ObjectId]))

(def user {:_id "123"})
(def user-created-now 
	(merge user {:created-at (System/currentTimeMillis)}))
(def user-created-and-last-seen-now
	(merge user-created-now {:last-seen-at (System/currentTimeMillis)}))
(def user-with-email (merge user-created-and-last-seen-now {:role "email"}))
(def saved-user user-with-email)

(def user-with-gcm (merge user {:gcm-id "aBCd"}))
(def user-with-nil-gcm (merge user {:gcm-id nil}))

(def profile {:_id (ObjectId.)})
(def user-with-profile (merge saved-user {:profile_id (:_id profile)}))
(def user-without-profile (merge saved-user {:profile_id ""}))

(fact "should save new user"
	(save-user user) => saved-user
		(provided 
			(created-now user) => user-created-now :times 1
			(last-seen-now user-created-now) => user-created-and-last-seen-now :times 1
			(with-email-role user-created-and-last-seen-now) => user-with-email :times 1
			(save-new-user user-with-email) => saved-user :times 1))

(fact "should find user by email"
	(find-user-by-email "testman@testland.fi") => saved-user
	(provided 
		(find-user {:email "testman@testland.fi"}) => saved-user :times 1))

(fact "should update gcm for user"
	(update-gcm-for-user user "aBCd") => user-with-gcm
	(provided 
		(update-user user-with-gcm) => user-with-gcm :times 1))

(fact "should not update gcm for user when gcm is nil"
	(update-gcm-for-user user nil) => (throws IllegalArgumentException)
	(provided (update-user user-with-nil-gcm) => user-with-gcm :times 0))

(fact "should set profile for user"
	(set-profile-for-user saved-user profile) => user-with-profile
	(provided (update-user user-with-profile) => user-with-profile :times 1))
