(ns whereabts.api.profiles-api-test
	(:use
		[whereabts.core.profiles]
		[whereabts.core.profiles-search]
		[whereabts.api.profiles-api]
		[whereabts.api-helper]
		[ring.mock.request]
		[midje.sweet]))

(def profile {:nick "asdasd" :country "fi" :description "asdasd"})
(def profile-payload {:profile profile})
(def successful-response (expected-res 201 profile))
(def successfull-get-response (expected-res 200 profile))
(def successfull-get-response-users (expected-res 200 {:users [profile]}))

(fact "should post profile details for user"
	(profiles-api-routes (whereabts-request-as-anonymous-user :post "/users" profile-payload)) => successful-response
	(provided 
		(save-user-profile email-roled-user profile) => profile :times 1))

(fact "should get profile details of user"
	(profiles-api-routes (whereabts-request-as-anonymous-user :get "/users")) => successfull-get-response
	(provided 
		(find-user-profile email-roled-user) => profile :times 1))

(fact "should get profile detais of specific user"
	(profiles-api-routes (whereabts-request-as-anonymous-user :get "/users/123abc")) => successfull-get-response
	(provided 
		(find-profile-of-user "123abc" email-roled-user) => profile :times 1))

(fact "should get recent user profiles"
	(profiles-api-routes (whereabts-request-as-anonymous-user :get "/recent/users")) => successfull-get-response-users
	(provided
		(find-recent-profiles email-roled-user) => [profile] :times 1))

(fact "should find user profiles by query search string"
	(profiles-api-routes 
		(whereabts-request-as-user :get "/users_search?search=jaska" {"search" "jaska"})) => successfull-get-response-users
	(provided
		(find-profiles "jaska") => [profile] :times 1))