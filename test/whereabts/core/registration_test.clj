(ns whereabts.core.registration-test
	(:use 
		[whereabts.core.registration]
		[whereabts.core.users]
		[whereabts.core.profiles]
		[midje.sweet]))

(def user {:email "some" :user-uuid "123-abc"})
(def profile {:country "fi" :nick "testman"})
(def registered-user (merge user {:role "email"}))

(def user-with-gcm (merge registered-user {:gcm-id "123"}))
(def user-with-nil-gcm (merge registered-user {:gcm-id nil}))

(fact "should register new user with default profile"
	(register-user user profile) => registered-user
	(provided 
		(find-user-by-email "some") => nil :times 1
		(register-new-user user profile) => registered-user :times 1))

(fact "should not re-register already existing anonymous user"
	(register-user user profile) => registered-user
	(provided 
		(find-user-by-email "some") => registered-user :times 1
		(register-new-user registered-user profile) => anything :times 0))

(fact "should register gcm-id for registered user"
	(register-gcm registered-user "123") => user-with-gcm
	(provided (update-gcm-for-user registered-user "123") => user-with-gcm :times 1))

(fact "should save user and profile when registering new user"
	(register-new-user user profile) => user
	(provided
		(save-user user) => user :times 1
		(save-user-profile user profile) => anything :times 1))