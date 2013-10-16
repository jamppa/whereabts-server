(ns whereabts.api.registration-api-test
	(:use
		[whereabts.core.registration]
		[whereabts.api.registration-api]
		[whereabts.api-helper]
		[ring.mock.request]
		[midje.sweet]))

(def payload {:user-uuid "123-abc" :email "anonymous@whereabts.com" :country "fi" :nick "anonymous" :photo "" :description ""})
(def user-from-payload (select-keys payload [:user-uuid :email]))
(def profile-from-payload (select-keys payload [:country :nick :description :photo]))

(def invalid-payload {:invalid "payload"})
(def gcm-registration-payload {:gcm-id "123abc"})

(def successful-response (expected-res 201 user-from-payload))

(fact "should POST user details"
	(registration-api-routes
		(whereabts-request-as-public-user :post "/register_user" payload)) => successful-response
	(provided 
		(register-user user-from-payload profile-from-payload) => user-from-payload :times 1))

(fact "should throw exception when POSTing invalid user details"
	(registration-api-routes
		(whereabts-request-as-public-user :post "/register_user" invalid-payload)) => (throws IllegalArgumentException)
	(provided 
		(register-user {} {}) =throws=> (IllegalArgumentException. "invalid!")))

(fact "should POST gcm id and register it for the user"
	(registration-api-routes
		(whereabts-request-as-anonymous-user :post "/register_gcm" gcm-registration-payload)) => successful-response
	(provided (register-gcm email-roled-user "123abc") => user-from-payload :times 1))

(fact "should GET minimum required client version"
	(registration-api-routes
		(whereabts-request-as-public-user :get "/required_client_version")) => (expected-res 200 {:version-code 22}))