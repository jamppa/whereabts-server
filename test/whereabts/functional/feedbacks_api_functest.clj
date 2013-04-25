(ns whereabts.functional.feedbacks-api-functest
	(:use 
		[midje.sweet]
		[whereabts.functional.api-functest])
	(:require 
		[clj-http.client :as http]
		[clojure.data.json :as json]))

(def whereabts-feedbacks-api (str whereabts-api-testsrv "/feedbacks"))
(def feedback-payload
	(json/write-str {:vote 0 :message "cool app guys!"}))
(def invalid-feedback-payload
	(json/write-str {:vote 0}))

(defn- post-feedback-as-anon [payload]
	(http/post whereabts-feedbacks-api
		(whereabts-api-request-anon payload)))

(fact "should POST feedback-payload and response with HTTP Created" :functional
	(:status (post-feedback-as-anon feedback-payload)) => 201)

(fact "should POST invalid feedback-payload and response with HTTP Bad Request" :functional
	(:status (post-feedback-as-anon invalid-feedback-payload)) => 400)