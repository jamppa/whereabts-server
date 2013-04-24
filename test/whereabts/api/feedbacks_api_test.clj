(ns whereabts.api.feedbacks-api-test
	(:use
		[midje.sweet]
		[ring.mock.request]
		[whereabts.api-helper]
		[whereabts.api.feedbacks-api]
		[whereabts.core.feedbacks]))

(def feedback-payload {})
(def saved-feedback {:_id "123123"})
(def successful-feedback-post-response (expected-res 201 saved-feedback))

(fact "should POST new feedback message"
	(feedbacks-api-routes 
		(whereabts-request :post "/feedbacks" feedback-payload)) => successful-feedback-post-response
	(provided (save-new-feedback feedback-payload) => saved-feedback :times 1))