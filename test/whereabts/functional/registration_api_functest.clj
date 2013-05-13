(ns whereabts.functional.registration-api-functest
	(:use 
		[midje.sweet]
		[whereabts.functional.api-functest]
		[whereabts.db-helper])
	(:require 
		[clj-http.client :as http]
		[clojure.data.json :as json])
	(:import [java.util UUID]))

(def registration-payload 
	(json/write-str {
		:user-uuid (.toString (UUID/randomUUID)) 
		:email "anonymous@whereabts.com"}))

(def invalid-registration-payload
	(json/write-str {
		:invalid "value" 
		:email "anonymous@whereabts.com"}))

(def registration-payload-wrong-uuid
	(json/write-str {
		:user-uuid "blaa-123-abc" 
		:email "anonymous@whereabts.com"}))

(def anonymous-registration-api 
	(str whereabts-api-testsrv "/anonymousregistration"))

(defn- post-as-anon [payload]
	(http/post anonymous-registration-api
		(whereabts-api-request-anon payload)))

(defn- post-as-unauth-user [payload]
	(http/post anonymous-registration-api
		(whereabts-api-request ["invalid@creds.com" "blaaah"] payload)))

(background (before :facts (setup-db)))

(fact "should response with HTTP Created When POSTing valid anonymous user registration payload" :functional
	(:status (post-as-anon registration-payload)) => 201)

(fact "should response with Bad Request When POSTing invalid formatted anonymous user registration payload" :functional
	(:status (post-as-anon invalid-registration-payload)) => 400)

(fact "should response with Bad Request when POSTing anonymous user registration payload with wrong uuid" :functional
	(:status (post-as-anon registration-payload-wrong-uuid)) => 400)

(fact "should response with Authentication failed when POSTing anonymous user registration payload with invalid credentials" :functional
	(:status (post-as-unauth-user registration-payload)) => 401)