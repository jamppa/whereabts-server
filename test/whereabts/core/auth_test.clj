(ns whereabts.core.auth-test
	(:use 
		[whereabts.core.auth]
		[whereabts.models.anonymous-user]
		[midje.sweet]))

(fact "should authenticate by returning anonymous system user when email and pass match"
	(authenticate "anonymous@whereabts.com" "ae129325a4db22faab7771f10b39a8af") => anonymous-whereabts-user)

(fact "should not authenticate and instead return nil when anonymous-user email is wrong"
	(authenticate "wrong@email.com" "ae129325a4db22faab7771f10b39a8af") => nil)