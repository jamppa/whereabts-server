(ns whereabts.api.replies-api
	(:use
		[whereabts.core.replies]
		[whereabts.models.message]
		[whereabts.api.api-utils]
		[compojure.core]
		[ring.util.response]
		[clojure.walk :only [keywordize-keys]])
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(defn- find-message-to-reply [id]
	(if-let [message (find-message-by-id id)]
		message
		(throw (WhereabtsResourceNotFoundException.))))

(defroutes replies-api-routes

	(POST "/messages/:id/replies" [id :as req]
		(with-role req ["email"]
			(let [user (:basic-authentication req)
				  reply-payload (keywordize-keys (:body req))
				  message-to-reply (find-message-to-reply id)]
				(-> (save-reply-to-message reply-payload user message-to-reply)
					(response)
					(status 201)))))


)