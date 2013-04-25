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

(fact "should POST feedback-payload and response with HTTP Created" :functional
	(:body (http/post whereabts-feedbacks-api
		(whereabts-api-request-anon feedback-payload))) => 201)



