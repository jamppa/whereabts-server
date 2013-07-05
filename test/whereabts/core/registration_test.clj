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
(def anonymified (merge user-created-and-last-seen-now {:role "anonymous"}))

(def user-with-gcm (merge anonymified {:gcm-id "123"}))
(def user-with-nil-gcm (merge anonymified {:gcm-id nil}))

(fact "should register new anonymous user"
	(register-user user) => anonymified
	(provided (find-user user) => nil :times 1)
	(provided (created-now user) => user-created-now :times 1)
	(provided (last-seen-now user-created-now) => user-created-and-last-seen-now :times 1)
	(provided (with-email-role user-created-and-last-seen-now) => anonymified :times 1)
	(provided (save-new-user anonymified) => anonymified :times 1))

(fact "should not re-register already existing anonymous user"
	(register-user user) => user
	(provided (find-user {:user-uuid "123-abc" :email "some"}) => user)
	(provided (created-now user) => user-created-now :times 0)
	(provided (save-new-user user-created-now) => user-created-now :times 0))

(fact "should register gcm-id for already existing user"
	(register-gcm-for-user anonymified "123") => user-with-gcm
	(provided (update-user user-with-gcm) => user-with-gcm :times 1))

(fact "should throw exception when trying to register nil gcm-id"
	(register-gcm-for-user anonymified nil) => (throws IllegalArgumentException)
	(provided (update-user user-with-nil-gcm) => user-with-nil-gcm :times 0))