(ns whereabts.functional.profiles-api-functest
	(:use 
		[midje.sweet]
		[whereabts.functional.api-functest]
		[whereabts.db-helper]
		[whereabts.db.test-fixtures])
	(:require 
		[clj-http.client :as http]
		[clojure.data.json :as json]))

(def whereabts-profiles-api (str whereabts-api-testsrv "/userprofile"))
(def profile-payload (json/write-str {:profile {:nick "jamppa" :country "fi" :description "testman from testland"}}))
(def broken-profile-payload (json/write-str {:profile {:country "fi" :description "testman from testland"}}))

(defn- post-as-user [payload]
	(http/post whereabts-profiles-api
		(whereabts-api-request ["user@test.com" "550e8400-e29b-41d4-a716-446655440001"] payload)))

(defn- post-as-public-user [payload]
	(http/post whereabts-profiles-api
		(whereabts-api-request-public-user payload)))

(defn- post-as-invalid-user [payload]
	(http/post whereabts-profiles-api
		(whereabts-api-request ["some@weird.com" "550e8400-e29b-41d4-a716-446655440666"] payload)))

(defn- post-as-user-profile-already [payload]
	(http/post whereabts-profiles-api
		(whereabts-api-request ["anonymous@whereabts.com" "550e8400-e29b-41d4-a716-446655440000"] payload)))

(background (before :facts (setup-db)))

(fact "should response http created when posting profile for user without profile" :functional
	(:status (post-as-user profile-payload)) => 201)

(fact "should response http created when posting profile for user with profile already" :functional
	(:status (post-as-user-profile-already profile-payload)) => 201)

(fact "should response http forbidden when posting profile as public user" :functional
	(:status (post-as-public-user profile-payload)) => 403)

(fact "should response http unauthorized when posting profile as invalid user" :functional
	(:status (post-as-invalid-user profile-payload)) => 401)

(fact "should reponse http bad request when posting broken profile" :functional
	(:status (post-as-user broken-profile-payload)) => 400)