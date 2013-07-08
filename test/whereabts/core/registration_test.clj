(ns whereabts.core.registration-test
	(:use 
		[whereabts.core.registration]
		[whereabts.core.users]
		[whereabts.models.user]
		[whereabts.models.util]
		[midje.sweet]))

(def user {:email "some" :user-uuid "123-abc"})
(def registered-user (merge user {:role "email"}))

(def user-with-gcm (merge registered-user {:gcm-id "123"}))
(def user-with-nil-gcm (merge registered-user {:gcm-id nil}))

(fact "should register new user"
	(register-user user) => registered-user
	(provided (find-user {:email (:email user)}) => nil :times 1)
	(provided (save-user user) => registered-user :times 1))

(fact "should not re-register already existing anonymous user"
	(register-user user) => registered-user
	(provided (find-user {:email (:email user)}) => registered-user)
	(provided (save-user registered-user) => registered-user :times 0))

(fact "should register gcm-id for already existing user"
	(register-gcm-for-user registered-user "123") => user-with-gcm
	(provided (update-user user-with-gcm) => user-with-gcm :times 1))

(fact "should throw exception when trying to register nil gcm-id"
	(register-gcm-for-user registered-user nil) => (throws IllegalArgumentException)
	(provided (update-user user-with-nil-gcm) => user-with-nil-gcm :times 0))