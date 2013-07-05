(ns whereabts.core.auth-test
	(:use 
		[whereabts.core.auth]
		[whereabts.models.user]
		[whereabts.models.util]
		[midje.sweet]))

(def anonymous-user {:email "anonymous@whereabts.com" :user-uuid "123abc"})
(def anonymous-user-authenticated-now
	(merge anonymous-user {:last-seen-at (System/currentTimeMillis)}))

(fact "should authenticate as anonymous user when one is found by email and uuid"
	(authenticate "anonymous@whereabts.com" "123abc") => anonymous-user
	(provided (find-user anonymous-user) => anonymous-user :times 1)
	(provided (authenticated-now-async (agent anonymous-user)) => anonymous-user :times 1))

(fact "should not authenticate as anonymous user when one is not found by email and uuid"
	(authenticate "anonymous@whereabts.com" "123abc") => nil
	(provided (find-user anonymous-user) => nil :times 1))

(fact "should update last seen timestamp and save user when authenticating now"
	(authenticated-now anonymous-user) => anonymous-user-authenticated-now
	(provided (last-seen-now anonymous-user) => anonymous-user-authenticated-now :times 1)
	(provided (update-user anonymous-user-authenticated-now) => anonymous-user-authenticated-now :times 1))

(fact "should not update last seen timestamp when now authenticating nil user"
	(authenticated-now nil) => nil
	(provided (last-seen-now nil) => nil :times 0)
	(provided (update-user nil) => nil :times 0))