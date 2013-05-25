(ns whereabts.api.replies-api-test
	(:use
		[whereabts.api.replies-api]
		[whereabts.core.replies]
		[whereabts.core.messages]
		[whereabts.core.with-util]
		[midje.sweet]
		[ring.mock.request]
		[whereabts.api-helper]))

(def new-reply-payload
	{:nick "jamppa" :replymessage "yeah!"})

(def message-to-reply {:_id 123})
(def saved-reply 
	(merge new-reply-payload {:_id 123 :user_id 123 :message_id 123}))

(def expected-res-for-new-reply (expected-res 201 saved-reply))

(fact "should POST new reply to message from user"
	(replies-api-routes
		(whereabts-request-as-anonymous-user :post "/messages/123abc/replies" new-reply-payload)) => expected-res-for-new-reply
	(provided (find-message "123abc" anonymous-roled-user) => message-to-reply :times 1)
	(provided (save-reply new-reply-payload anonymous-roled-user message-to-reply) => saved-reply :times 1))