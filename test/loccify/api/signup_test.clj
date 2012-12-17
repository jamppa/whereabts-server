(ns loccify.api.signup-test
	(:use [midje.sweet]
			[ring.mock.request]
			[loccify.api.signup]
			[loccify.core.auth]))

(defn- expected-res [status body]
	{:status status :headers {} :body body})

(def expected-res-for-available-username 
	(expected-res 200 {:name "teppo" :available true}))

(fact "should give correct response when requesting available username"
	(signup-routes (request :get "/signup/user/available/teppo")) => expected-res-for-available-username
	(provided (available-username? "teppo") => true))

