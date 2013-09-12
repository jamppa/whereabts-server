(ns whereabts.api.follow-api-test
	(:use
		midje.sweet
		ring.mock.request
		whereabts.api.follow-api
		whereabts.api-helper))

(def response-followed (expected-res 201 {}))
(def response-unfollowed (expected-res 200 {}))

(fact "should POST to user followers"
	(follow-api-routes 
		(whereabts-request-as-anonymous-user :post "/user/123AbC/followers" {})) => response-followed)

(fact "should DELETE from user followers"
	(follow-api-routes
		(whereabts-request-as-anonymous-user :delete "/user/123AbC/followers" {})) => response-unfollowed)