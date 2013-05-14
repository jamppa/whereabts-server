(ns whereabts.api.api-utils-test
	(:use 
		[midje.sweet]
		[whereabts.api.api-utils])
	(:import [whereabts.exception WhereabtsForbiddenException]))

(def request {:basic-authentication {:role "public"}})

(fact "should return nil when basic-authenticateds users role match"
	(check-role request "public") => nil)

(fact "should throw exception when basic-authenticateds useres role does not match"
	(check-role request "anonymous") => (throws WhereabtsForbiddenException))
