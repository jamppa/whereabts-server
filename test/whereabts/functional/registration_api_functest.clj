(ns whereabts.functional.registration-api-functest
	(:use 
		[midje.sweet]
		[whereabts.functional.api-functest])
	(:require 
		[clj-http.client :as http]
		[clojure.data.json :as json])
	(:import [java.util UUID]))

(def registration-payload 
	(json/write-str {:user-uuid (.toString (UUID/randomUUID)) :email "anonymous@whereabts.com"}))

(def invalid-registration-payload
	(json/write-str {:invalid "value" :email "anonymous@whereabts.com"}))

(def anonymous-registration-api 
	(str whereabts-api-testsrv "/anonymousregistration"))

(defn- post-as-anon [payload]
	(http/post anonymous-registration-api
		(whereabts-api-request-anon payload)))

(fact "should response with HTTP Created When POSTing valid anonymous user registration payload" :functional
	(:status (post-as-anon registration-payload)) => 201)

(fact "should response with Bad Request When POSTing invalid formatted anonymous user registration payload" :functional
	(:status (post-as-anon invalid-registration-payload)) => 400)