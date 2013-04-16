(ns whereabts.api.messages-api-test
	(:use
		[whereabts.api.messages-api]
		[whereabts.core.messages]
		[midje.sweet]
		[ring.mock.request]
		[whereabts.api-helper]
		[whereabts.util.geo]))

(def bbox (bounding-box [1.23 1.23] [5.0 5.0]))
(def messages {:messages []})
(def new-msg-payload {:something "cool"})
(def saved-new-msg {})

(def expected-res-for-messages (expected-res 200 messages))
(def expected-res-for-new-message (expected-res 201 saved-new-msg))

(fact "should GET all messages inside bounding box"
	(messages-api-routes (request :get "/messages/1.23/1.23/5.0/5.0")) => expected-res-for-messages
	(provided (find-all-messages-by-bbox bbox) => messages :times 1))

(fact "should POST anonymous new message"
	(messages-api-routes 
		(whereabts-request :post "/messages" new-msg-payload)) => expected-res-for-new-message
	(provided
		(save-new-message new-msg-payload) => saved-new-msg :times 1))