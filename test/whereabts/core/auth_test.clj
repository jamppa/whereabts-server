(ns whereabts.core.auth-test
	(:use 
		[whereabts.core.auth]
		[whereabts.models.anonymous-user]
		[midje.sweet]))

(def anonymous-user {:email "anonymous@whereabts.com" :user-uuid "123abc"})

(fact "should authenticate as whereabts user when email and pass match"
	(authenticate-whereabts-anon-user "anonymous@whereabts.com" "ae129325a4db22faab7771f10b39a8af") => anonymous-whereabts-user)

(fact "should not authenticate as whereabts user when email is wrong"
	(authenticate-whereabts-anon-user "wrong@email.com" "ae129325a4db22faab7771f10b39a8af") => nil)

(fact "should not authenticate as whereabts user when user-uuid is wrong"
	(authenticate-whereabts-anon-user "anonymous@whereabts.com" "666") => nil)

(fact "should authenticate as anonymous user when one is found by email and uuid"
	(authenticate "anonymous@whereabts.com" "123abc") => anonymous-user
	(provided (find-anonymous-user anonymous-user) => anonymous-user :times 1))