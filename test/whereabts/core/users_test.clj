(ns whereabts.core.users-test
	(:use
		[whereabts.core.users]
		[whereabts.models.user]
		[whereabts.models.util]
		[midje.sweet]))

(def user {})
(def user-created-now 
	(merge user {:created-at (System/currentTimeMillis)}))
(def user-created-and-last-seen-now
	(merge user-created-now {:last-seen-at (System/currentTimeMillis)}))
(def user-with-email (merge user-created-and-last-seen-now {:role "email"}))
(def saved-user user-with-email)

(fact "should save new user"
	(save-user user) => saved-user
		(provided (created-now user) => user-created-now :times 1)
		(provided (last-seen-now user-created-now) => user-created-and-last-seen-now :times 1)
		(provided (with-email-role user-created-and-last-seen-now) => user-with-email :times 1)
		(provided (save-new-user user-with-email) => saved-user :times 1))

(fact "should find user by email with profile"
	(find-user-by-email "testman@testland.fi") => user-with-email
	(provided (find-user {:email "testman@testland.fi"}) => user-with-email :times 1))