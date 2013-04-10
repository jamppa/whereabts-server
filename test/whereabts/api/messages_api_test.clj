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
(def msg-payload {:something "cool"})
(def anonymous-msg (merge msg-payload {:msg-type :anonymous}))

(def expected-res-for-messages (expected-res 200 messages))
(def expected-res-for-new-anon-message (expected-res 201 anonymous-msg))

(fact "should GET all messages inside bounding box"
	(messages-api-routes (request :get "/messages/1.23/1.23/5.0/5.0")) => expected-res-for-messages
	(provided (find-all-messages-by-bbox bbox) => messages :times 1))

(fact "should POST anonymous new message"
	(messages-api-routes 
		(whereabts-request :post "/messages" msg-payload)) => expected-res-for-new-anon-message
	(provided
		(anonymous-message msg-payload) => anonymous-msg :times 1
		(save-new-message anonymous-msg) => anonymous-msg :times 1))