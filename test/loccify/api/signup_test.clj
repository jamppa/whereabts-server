(ns loccify.api.signup-test
	(:use [midje.sweet]
			[ring.mock.request]
			[loccify.api.signup]))

(defn- expected-res [status body]
	{:status status :headers {} :body body})

(def expected-res-for-available-username 
	(expected-res 200 {:name "teppo" :available true}))

(fact "should response username is available as a json with http-status 200"
	(signup-routes (request :get "/signup/user/available/teppo")) => expected-res-for-available-username)

