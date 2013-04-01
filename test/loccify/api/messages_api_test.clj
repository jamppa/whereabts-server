(ns loccify.api.messages-api-test
	(:use
		[loccify.api.messages-api]
		[loccify.core.messages]
		[midje.sweet]
		[ring.mock.request]
		[loccify.api-helper]
		[loccify.util.geo]))

(def bbox (bounding-box [1.23 1.23] [5.0 5.0]))
(def messages {:messages [] :anon-messages []})
(def expected-res-for-messages (expected-res 200 messages))

(fact "should GET all messages inside bounding box"
	(messages-api-routes (request :get "/messages/1.23/1.23/5.0/5.0")) => expected-res-for-messages
	(provided (find-all-messages-by-bbox bbox) => messages :times 1))