(ns whereabts.api.registration-api-test
	(:use
		[whereabts.core.registration]
		[whereabts.api.registration-api]
		[whereabts.api-helper]
		[ring.mock.request]
		[midje.sweet]))

(def payload {:user-uuid "123-abc" :email "anonymous@whereabts.com"})
(def invalid-payload {:invalid "payload"})
(def successful-response (expected-res 201 payload))

(fact "should POST anonymous user details"
	(registration-api-routes
		(whereabts-request :post "/anonymousregistration" payload)) => successful-response
	(provided (register-anonymous-user payload) => payload :times 1))

(fact "should throw exception when POSTing invalid anonymous user details"
	(registration-api-routes
		(whereabts-request :post "/anonymousregistration" invalid-payload)) => (throws IllegalArgumentException)
	(provided (register-anonymous-user {:user-uuid nil :email nil}) =throws=> (IllegalArgumentException. "invalid!")))