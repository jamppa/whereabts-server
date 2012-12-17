(ns loccify.api.signup-test
	(:use [midje.sweet]
			[ring.mock.request]
			[loccify.api.signup]
			[loccify.core.auth]))

(defn- expected-res [status body]
	{:status status :headers {} :body body})

(def expected-res-for-available-username
	(expected-res 200 {:name "teppo" :available true}))

(def expected-res-for-not-available-username
	(expected-res 200 {:name "seppo" :available false}))

(fact "should give correct response when requesting available username"
	(signup-routes (request :get "/user/available/teppo")) => expected-res-for-available-username
	(provided (available-username? "teppo") => true))

(fact "should give correct response when requesting not available username"
	(signup-routes (request :get "/user/available/seppo")) => expected-res-for-not-available-username
	(provided (available-username? "seppo") => false))

