(ns whereabts.api.follow-api-test
	(:use
		midje.sweet
		ring.mock.request
		whereabts.api.follow-api
		whereabts.api-helper
		whereabts.core.users-follow))

(def response-followed (expected-res 201 {}))
(def response-unfollowed (expected-res 200 {}))

(fact "should POST to user followers"
	(follow-api-routes 
		(whereabts-request-as-anonymous-user :post "/user/123AbC/followers" {})) => response-followed
	(provided
		(follow-user "123AbC" email-roled-user) => {} :times 1))

(fact "should DELETE from user followers"
	(follow-api-routes
		(whereabts-request-as-anonymous-user :delete "/user/123AbC/followers" {})) => response-unfollowed)