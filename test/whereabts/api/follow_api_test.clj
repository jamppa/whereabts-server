(ns whereabts.api.follow-api-test
	(:use
		midje.sweet
		ring.mock.request
		whereabts.api.follow-api
		whereabts.api-helper
		whereabts.core.users-follow))

(def response-followed (expected-res 201 {}))
(def response-unfollowed (expected-res 200 {}))
(def response-get-followers (expected-res 200 {:followers []}))

(fact "should POST to user followers"
	(follow-api-routes 
		(whereabts-request-as-anonymous-user :post "/user/123AbC/followers" {})) => response-followed
	(provided
		(follow-user "123AbC" email-roled-user) => {} :times 1))

(fact "should DELETE from user followers"
	(follow-api-routes
		(whereabts-request-as-anonymous-user :delete "/user/123AbC/followers" {})) => response-unfollowed
	(provided
		(unfollow-user "123AbC" email-roled-user) => {} :times 1))

(fact "should GET followers of a user"
	(follow-api-routes 
		(whereabts-request-as-anonymous-user :get "/user/123AbC/followers" {})) => response-get-followers
	(provided
		(find-followers-of-user "123AbC") => [] :times 1))