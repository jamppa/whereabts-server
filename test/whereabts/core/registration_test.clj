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
	(provided (created-now anonymous-user) => anonymous-user-created-now :times 1)
	(provided (save-anonymous-user anonymous-user-created-now) => anonymous-user-created-now :times 1))