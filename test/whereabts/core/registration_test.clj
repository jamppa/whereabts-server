(ns whereabts.core.registration-test
	(:use 
		[whereabts.core.registration]
		[whereabts.models.user]
		[whereabts.models.util]
		[midje.sweet]))

(def user {:email "some" :user-uuid "123-abc"})
(def user-created-now 
	(merge user {:created-at (System/currentTimeMillis)}))
(def user-created-and-last-seen-now
	(merge user-created-now {:last-seen-at (System/currentTimeMillis)}))
(def user-with-email (merge user-created-and-last-seen-now {:role "email"}))

(def user-with-gcm (merge user-with-email {:gcm-id "123"}))
(def user-with-nil-gcm (merge user-with-email {:gcm-id nil}))

(fact "should register new user"
	(register-user user) => user-with-email
	(provided (find-user {:email (:email user)}) => nil :times 1)
	(provided (created-now user) => user-created-now :times 1)
	(provided (last-seen-now user-created-now) => user-created-and-last-seen-now :times 1)
	(provided (with-email-role user-created-and-last-seen-now) => user-with-email :times 1)
	(provided (save-new-user user-with-email) => user-with-email :times 1))

(fact "should not re-register already existing anonymous user"
	(register-user user) => user
	(provided (find-user {:email (:email user)}) => user)
	(provided (created-now user) => user-created-now :times 0)
	(provided (save-new-user user-created-now) => user-created-now :times 0))

(fact "should register gcm-id for already existing user"
	(register-gcm-for-user user-with-email "123") => user-with-gcm
	(provided (update-user user-with-gcm) => user-with-gcm :times 1))

(fact "should throw exception when trying to register nil gcm-id"
	(register-gcm-for-user user-with-email nil) => (throws IllegalArgumentException)
	(provided (update-user user-with-nil-gcm) => user-with-nil-gcm :times 0))