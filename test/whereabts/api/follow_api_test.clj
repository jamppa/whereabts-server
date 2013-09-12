(ns whereabts.api.follow-api-test
	(:use
		midje.sweet
		ring.mock.request
		whereabts.api.follow-api
		whereabts.api-helper))

(def response-success (expected-res 201 {}))

(fact "should POST to user followers"
	(follow-api-routes 
		(whereabts-request-as-anonymous-user :post "/user/123AbC/followers" {})) => response-success)