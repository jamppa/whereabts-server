(ns whereabts.api.messages-api
	(:use
		[whereabts.api.api-utils]
		[compojure.core]
		[ring.util.response]
		[whereabts.core.messages]
		[whereabts.util.geo]
		[clojure.walk :only [keywordize-keys]]))

(defn view-message-async [msg-agent]
	(send-off msg-agent view-and-update-message))

(defroutes messages-api-routes

	(GET "/messages/:ll-lon/:ll-lat/:ur-lon/:ur-lat" [ll-lon ll-lat ur-lon ur-lat :as req]
		(with-role req "anonymous"
		(let [ll-vec [(read-string ll-lon) (read-string ll-lat)]
			  ur-vec [(read-string ur-lon) (read-string ur-lat)]]
		(response (find-all-messages-by-bbox (bounding-box ll-vec ur-vec))))))

	(GET "/messages/:id" [id :as req]
		(with-role req "anonymous"
		(let [message (find-message id)
			  user (:basic-authentication req)]
			  (view-message-async (agent message))
			  (response (message-with-ownership message user)))))

	(POST "/messages" [:as req]
		(with-role req "anonymous"
		(let [message (keywordize-keys (:body req))
			  user (:basic-authentication req)]
			(status (response (save-new-message message user)) 201))))

	(DELETE "/messages/:id" [id :as req]
		(with-role req "anonymous"
			(response (delete-message id (:basic-authentication req)))))
)

