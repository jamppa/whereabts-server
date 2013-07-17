(ns whereabts.api.profiles-api-test
	(:use
		[whereabts.core.profiles]
		[whereabts.api.profiles-api]
		[whereabts.api-helper]
		[ring.mock.request]
		[midje.sweet]))

(def profile {:nick "asdasd" :country "fi" :description "asdasd"})
(def profile-payload {:profile profile})
(def successful-response (expected-res 201 profile))
(def successfull-get-response (expected-res 200 profile))

(fact "should post profile details for user"
	(profiles-api-routes (whereabts-request-as-anonymous-user :post "/user" profile-payload)) => successful-response
	(provided (save-user-profile email-roled-user profile) => profile :times 1))

(fact "should get profile details of user"
	(profiles-api-routes (whereabts-request-as-anonymous-user :get "/user")) => successfull-get-response
	(provided (find-user-profile email-roled-user) => profile :times 1))