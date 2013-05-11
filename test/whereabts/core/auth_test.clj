(ns whereabts.core.auth-test
	(:use 
		[whereabts.core.auth]
		[whereabts.models.anonymous-user]
		[midje.sweet]))

(def user {})

(fact "should authenticate by returning anonymous system user when email and pass match"
	(authenticate "anonymous@whereabts.com" "ae129325a4db22faab7771f10b39a8af") => anonymous-whereabts-user)