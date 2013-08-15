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

(def registration-payload-existing-email
	(json/write-str {
		:user-uuid (.toString (UUID/randomUUID)) 
		:email "anonymous@whereabts.com"}))

(def invalid-registration-payload
	(json/write-str {
		:invalid "value" 
		:email "tester@testland.fi"}))

(def registration-payload-wrong-uuid
	(json/write-str {
		:user-uuid "blaa-123-abc" 
		:email "another@testland.fi"}))

(def gcm-registration-payload (json/write-str {:gcm-id "123abc"}))
(def invalid-gcm-registration-payload (json/write-str {:some-other "asdasd"}))

(def anonymous-registration-api (str whereabts-api-testsrv "/register_user"))
(def gcm-id-registration-api (str whereabts-api-testsrv "/register_gcm"))

(defn- post-as-public-user [payload]
	(http/post anonymous-registration-api
		(whereabts-api-request-public-user payload)))

(defn- post-as-unauth-user [payload]
	(http/post anonymous-registration-api
		(whereabts-api-request ["invalid@creds.com" "blaaah"] payload)))

(defn- post-as-anon-user [payload]
	(http/post gcm-id-registration-api
		(whereabts-api-request ["anonymous@whereabts.com" "550e8400-e29b-41d4-a716-446655440000"] payload)))

(background (before :facts (setup-db)))

(fact "should response with HTTP Created When POSTing valid user registration payload" :functional
	(:status (post-as-public-user registration-payload)) => 201)

(fact "should response with Bad Request When POSTing invalid formatted  user registration payload" :functional
	(:status (post-as-public-user invalid-registration-payload)) => 400)

(fact "should response with Bad Request when POSTing user registration payload with wrong uuid" :functional
	(:status (post-as-public-user registration-payload-wrong-uuid)) => 400)

(fact "should response with Authentication failed when POSTing user registration payload with invalid credentials" :functional
	(:status (post-as-unauth-user registration-payload)) => 401)

(fact "should response with Http Created when POSTing for registration of GCM Id for user" :functional
	(:status (post-as-anon-user gcm-registration-payload)) => 201)

(fact "should reponse with Http Bad Request when POSTing invalid payload for registration of GCM Id" :functional
	(:status (post-as-anon-user invalid-gcm-registration-payload)) => 400)

(fact "should response with HTTP created when POSTing registration payload with existin email" :functional
	(:status (post-as-public-user registration-payload-existing-email)) => 201)