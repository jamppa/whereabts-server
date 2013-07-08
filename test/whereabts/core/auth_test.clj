(ns whereabts.core.auth-test
	(:use 
		[whereabts.core.auth]
		[whereabts.models.user]
		[whereabts.models.util]
		[midje.sweet]))

(def user {:email "anonymous@whereabts.com" :user-uuid "123abc"})
(def user-authenticated-now
	(merge user {:last-seen-at (System/currentTimeMillis)}))

(fact "should authenticate as when user is found by email and uuid"
	(authenticate "anonymous@whereabts.com" "123abc") => user
	(provided (find-user user) => user :times 1)
	(provided (authenticated-now-async (agent user)) => user :times 1))

(fact "should not authenticate when user is not found by email and uuid"
	(authenticate "anonymous@whereabts.com" "123abc") => nil
	(provided (find-user user) => nil :times 1))

(fact "should update last seen timestamp and save user when authenticating now"
	(authenticated-now user) => user-authenticated-now
	(provided (last-seen-now user) => user-authenticated-now :times 1)
	(provided (update-user user-authenticated-now) => user-authenticated-now :times 1))

(fact "should not update last seen timestamp when now authenticating nil user"
	(authenticated-now nil) => nil
	(provided (last-seen-now nil) => nil :times 0)
	(provided (update-user nil) => nil :times 0))