(ns whereabts.core.registration-test
	(:use 
		[whereabts.core.registration]
		[whereabts.core.users]
		[midje.sweet]))

(def user {:email "some" :user-uuid "123-abc"})
(def registered-user (merge user {:role "email"}))

(def user-with-gcm (merge registered-user {:gcm-id "123"}))
(def user-with-nil-gcm (merge registered-user {:gcm-id nil}))

(fact "should register new user"
	(register-user user) => registered-user
	(provided (find-user-by-email "some") => nil :times 1)
	(provided (save-user user) => registered-user :times 1))

(fact "should not re-register already existing anonymous user"
	(register-user user) => registered-user
	(provided (find-user-by-email "some") => registered-user :times 1)
	(provided (save-user registered-user) => registered-user :times 0))

(fact "should register gcm-id for registered user"
	(register-gcm registered-user "123") => user-with-gcm
	(provided (update-gcm-for-user registered-user "123") => user-with-gcm :times 1))