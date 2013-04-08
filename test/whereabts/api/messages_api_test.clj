(ns whereabts.api.messages-api-test
	(:use
		[whereabts.api.messages-api]
		[whereabts.core.messages]
		[midje.sweet]
		[ring.mock.request]
		[whereabts.api-helper]
		[whereabts.util.geo]))

(def bbox (bounding-box [1.23 1.23] [5.0 5.0]))
(def messages {:messages [] :anon-messages []})
(def msg-payload {:something "and something"})
(def anonymous-msg (merge msg-payload {:msg-type :anonymous}))
(def new-anonymous-msg {})

(def expected-res-for-messages (expected-res 200 messages))
(def expected-res-for-new-anon-message (expected-res 201 new-anonymous-msg))

(fact "should GET all messages inside bounding box"
	(messages-api-routes (request :get "/messages/1.23/1.23/5.0/5.0")) => expected-res-for-messages
	(provided (find-all-messages-by-bbox bbox) => messages :times 1))

(fact "should POST anonymous new message"
	(messages-api-routes 
		(whereabts-request :post "/messages" msg-payload)) => expected-res-for-new-anon-message
	(provided
		(anonymous-message msg-payload) => anonymous-msg :times 1
		(save-new-message anonymous-msg) => new-anonymous-msg :times 1))