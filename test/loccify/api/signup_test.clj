(ns loccify.api.signup-test
	(:use 
		[midje.sweet]
		[ring.mock.request]
		[loccify.api.signup]
		[loccify.core.auth]
		[loccify.core.signup]
		[loccify.api-helper])
	(:import [loccify.exception SignUpException]))

(defn- expected-res [status body]
	{:status status :headers {} :body body})

(def expected-res-for-available-username (expected-res 200 {:name "teppo" :available true}))
(def expected-res-for-not-available-username (expected-res 200 {:name "seppo" :available false}))
(def expected-res-for-available-email (expected-res 200 {:email "teppo@test.fi" :available true}))
(def expected-res-for-not-available-email (expected-res 200 {:email "teppo@test.fi" :available false}))

(def user-payload {:name "teppo" :email "teppo@test.fi" :password "secret" :type "email"})
(def expected-res-for-signup (expected-res 200 user-payload))
(def expected-res-for-failed-signup (expected-res 400 {:reason "Signup failed!"}))

(fact "should give correct response when requesting available username availability"
	(signup-routes (request :get "/user/available/teppo")) => expected-res-for-available-username
	(provided (available-username? "teppo") => true))

(fact "should give correct response when requesting not available username availability"
	(signup-routes (request :get "/user/available/seppo")) => expected-res-for-not-available-username
	(provided (available-username? "seppo") => false))

(fact "should give correct response when requesting available email availabilitu"
	(signup-routes (request :get "/email/available/teppo@test.fi")) => expected-res-for-available-email
	(provided (available-email? "teppo@test.fi") => true))

(fact "should give correct response when requesting not available email availability"
	(signup-routes (request :get "/email/available/teppo@test.fi")) => expected-res-for-not-available-email
	(provided (available-email? "teppo@test.fi") => false))

(fact "should give correct response when requesting to signup a user with valid payload"
	(signup-routes (loccify-request :post "/signup" user-payload)) => expected-res-for-signup
	(provided (signup user-payload) => user-payload))

(fact "should give correct response when requesting to signup and it fails with exception"
	(signup-routes (loccify-request :post "/signup" user-payload)) => expected-res-for-failed-signup
	(provided (signup user-payload) =throws=> (SignUpException. "Signup failed!")))