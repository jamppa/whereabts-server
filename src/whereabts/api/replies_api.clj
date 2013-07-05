(ns whereabts.api.replies-api
	(:use
		[whereabts.core.replies]
		[whereabts.core.messages]
		[whereabts.api.api-utils]
		[compojure.core]
		[ring.util.response]
		[clojure.walk :only [keywordize-keys]]))

(defroutes replies-api-routes

	(POST "/messages/:id/replies" [id :as req]
		(with-role req ["email"]
			(let [user (:basic-authentication req)
				  reply-payload (keywordize-keys (:body req))
				  message-to-reply (find-message id user)]
				(-> (save-reply-to-message reply-payload user message-to-reply)
					(response)
					(status 201)))))


)