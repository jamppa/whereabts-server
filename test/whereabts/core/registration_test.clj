(ns whereabts.core.registration-test
	(:use 
		[whereabts.core.registration]
		[whereabts.models.anonymous-user]
		[whereabts.models.util]
		[midje.sweet]))

(def anonymous-user {:email "some" :user-uuid "123-abc"})
(def anonymous-user-created-now 
	(merge anonymous-user {:created-at (System/currentTimeMillis)}))
(def anonymous-user-created-and-last-seen-now
	(merge anonymous-user-created-now {:last-seen-at (System/currentTimeMillis)}))
(def anonymified (merge anonymous-user-created-and-last-seen-now {:role "anonymous"}))

(def user-with-gcm (merge anonymified {:gcm-id "123"}))

(fact "should register new anonymous user"
	(register-anonymous-user anonymous-user) => anonymified
	(provided (find-anonymous-user anonymous-user) => nil :times 1)
	(provided (created-now anonymous-user) => anonymous-user-created-now :times 1)
	(provided (last-seen-now anonymous-user-created-now) => anonymous-user-created-and-last-seen-now :times 1)
	(provided (anonymify anonymous-user-created-and-last-seen-now) => anonymified :times 1)
	(provided (save-anonymous-user anonymified) => anonymified :times 1))

(fact "should not re-register already existing anonymous user"
	(register-anonymous-user anonymous-user) => anonymous-user
	(provided (find-anonymous-user {:user-uuid "123-abc" :email "some"}) => anonymous-user)
	(provided (created-now anonymous-user) => anonymous-user-created-now :times 0)
	(provided (save-anonymous-user anonymous-user-created-now) => anonymous-user-created-now :times 0))

(fact "should register gcm-id for already existing user"
	(register-gcm-for-user anonymified "123") => user-with-gcm
	(provided (update-anonymous-user user-with-gcm) => user-with-gcm :times 1))