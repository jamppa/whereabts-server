(ns whereabts.api.feedback-api-test
	(:use
		[midje.sweet]
		[ring.mock.request]
		[whereabts.api-helper]
		[whereabts.api.feedback-api]
		[whereabts.core.feedback]))

(def feedback-payload {})
(def saved-feedback {:_id "123123"})
(def successful-feedback-post-response (expected-res 201 saved-feedback))

(fact "should POST new feedback message"
	(feedback-api-routes 
		(whereabts-request :post "/feedbacks" feedback-payload)) => successful-feedback-post-response
	(provided (save-new-feedback feedback-payload) => saved-feedback :times 1))