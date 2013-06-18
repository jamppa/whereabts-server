(ns whereabts.api.registration-api-test
	(:use
		[whereabts.core.registration]
		[whereabts.api.registration-api]
		[whereabts.api-helper]
		[ring.mock.request]
		[midje.sweet]))

(def payload {:user-uuid "123-abc" :email "anonymous@whereabts.com"})
(def invalid-payload {:invalid "payload"})
(def gcm-registration-payload {:gcm-id "123abc"})

(def successful-response (expected-res 201 payload))

(fact "should POST anonymous user details"
	(registration-api-routes
		(whereabts-request-as-public-user :post "/anonymousregistration" payload)) => successful-response
	(provided (register-anonymous-user payload) => payload :times 1))

(fact "should throw exception when POSTing invalid anonymous user details"
	(registration-api-routes
		(whereabts-request-as-public-user :post "/anonymousregistration" invalid-payload)) => (throws IllegalArgumentException)
	(provided (register-anonymous-user {:user-uuid nil :email nil}) =throws=> (IllegalArgumentException. "invalid!")))

(fact "should POST gcm id and register it for the user"
	(registration-api-routes
		(whereabts-request-as-anonymous-user :post "/register_gcm" gcm-registration-payload)) => successful-response
	(provided (register-gcm-for-user anonymous-roled-user "123abc") => payload :times 1))