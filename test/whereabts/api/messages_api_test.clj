(ns whereabts.api.messages-api-test
	(:use
		[whereabts.api.messages-api]
		[whereabts.core.messages]
		[whereabts.core.with-util]
		[whereabts.api-helper]
		[whereabts.util.geo]
		[midje.sweet]
		[ring.mock.request]))

(def bbox (bounding-box [1.23 1.23] [5.0 5.0]))
(def messages [])
(def new-msg-payload {:something "cool"})
(def message {})

(def expected-res-for-messages (expected-res 200 {:messages messages}))
(def expected-res-for-new-message (expected-res 201 message))
(def expected-res-for-message (expected-res 200 message))
(def expected-res-for-delete-message (expected-res 200 message))

(fact "should GET all messages inside bounding box"
	(messages-api-routes (whereabts-request-as-anonymous-user :get "/messages/1.23/1.23/5.0/5.0")) => expected-res-for-messages
	(provided (find-all-messages-by-bbox bbox) => messages :times 1))

(fact "should POST new message"
	(messages-api-routes 
		(whereabts-request-as-anonymous-user :post "/messages" new-msg-payload)) => expected-res-for-new-message
	(provided (save-new-message new-msg-payload email-roled-user) => message :times 1))

(fact "should GET message by its id"
	(messages-api-routes (whereabts-request-as-anonymous-user :get "/messages/123abc")) => expected-res-for-message
	(provided 
		(find-message "123abc" email-roled-user) => message :times 1))

(fact "should DELETE message by its id"
	(messages-api-routes
		(whereabts-request-as-anonymous-user :delete "/messages/123abc")) => expected-res-for-delete-message
	(provided (delete-message "123abc" email-roled-user) => message :times 1))

(fact "should POST like to existing message"
	(messages-api-routes
		(whereabts-request-as-anonymous-user :post "/messages/123abc/likes" {})) => (expected-res 201 message)
	(provided
		(like-message "123abc" email-roled-user) => message :times 1))

(fact "should GET following messages skipping some of them"
	(messages-api-routes
		(whereabts-request-as-anonymous-user :get "/messages/following/1")) => expected-res-for-messages
	(provided
		(find-following-messages email-roled-user 1) => [] :times 1))

(fact "should GET following messages skipping and older than"
	(messages-api-routes
		(whereabts-request-as-anonymous-user :get "/messages/following/5/123123")) => expected-res-for-messages
	(provided
		(find-following-messages-older-than email-roled-user 5 123123) => [] :times 1))

(fact "should GET messages of user skipping some and older than"
	(messages-api-routes
		(whereabts-request-as-anonymous-user :get "/users/123abc/messages/0/123123")) => expected-res-for-messages
	(provided
		(find-users-messages-older-than "123abc" 0 123123) => [] :times 1))