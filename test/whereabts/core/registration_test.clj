(ns whereabts.core.registration-test
	(:use 
		[whereabts.core.registration]
		[whereabts.models.anonymous-user]
		[whereabts.models.util]
		[midje.sweet]))

(def anonymous-user {:email "some" :user-uuid "123-abc"})
(def anonymous-user-created-now 
	(merge anonymous-user {:created-at (System/currentTimeMillis)}))

(fact "should register an anonymous new user"
	(register-anonymous-user anonymous-user) => anonymous-user-created-now
	(provided (find-anonymous-user anonymous-user) => nil :times 1)
	(provided (created-now anonymous-user) => anonymous-user-created-now :times 1)
	(provided (save-anonymous-user anonymous-user-created-now) => anonymous-user-created-now :times 1))

(fact "should not re-register already existing anonymous user"
	(register-anonymous-user anonymous-user) => anonymous-user
	(provided (find-anonymous-user {:user-uuid "123-abc" :email "some"}) => anonymous-user)
	(provided (created-now anonymous-user) => anonymous-user-created-now :times 0)
	(provided (save-anonymous-user anonymous-user-created-now) => anonymous-user-created-now :times 0))