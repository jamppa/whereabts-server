(ns whereabts.functional.feedbacks-api-functest
	(:use 
		[midje.sweet]
		[whereabts.functional.api-functest]
		[whereabts.db-helper])
	(:require 
		[clj-http.client :as http]
		[clojure.data.json :as json]))

(def whereabts-feedbacks-api (str whereabts-api-testsrv "/feedbacks"))
(def feedback-payload
	(json/write-str {:vote 0 :message "cool app guys!"}))
(def invalid-feedback-payload
	(json/write-str {:vote 0}))

(defn- post-feedback-as-user [payload]
	(http/post whereabts-feedbacks-api
		(whereabts-api-request ["anonymous@whereabts.com" "550e8400-e29b-41d4-a716-446655440000"] payload)))

(defn- post-feedback-as-public-user [payload]
	(http/post whereabts-feedbacks-api
		(whereabts-api-request-public-user payload)))

(background (before :facts (setup-db)))

(fact "should POST feedback-payload and response with HTTP Created" :functional
	(:status (post-feedback-as-user feedback-payload)) => 201)

(fact "should POST invalid feedback-payload and response with HTTP Bad Request" :functional
	(:status (post-feedback-as-user invalid-feedback-payload)) => 400)