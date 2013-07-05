(ns whereabts.api.messages-api
	(:use
		[whereabts.api.api-utils]
		[compojure.core]
		[ring.util.response]
		[whereabts.core.messages]
		[whereabts.core.with-util]
		[whereabts.util.geo]
		[clojure.walk :only [keywordize-keys]]))

(defn view-message-async [msg-agent]
	(send-off msg-agent view-and-update-message))

(defroutes messages-api-routes

	(GET "/messages/:ll-lon/:ll-lat/:ur-lon/:ur-lat" [ll-lon ll-lat ur-lon ur-lat :as req]
		(with-role req ["email"]
		(let [ll-vec [(read-string ll-lon) (read-string ll-lat)]
			  ur-vec [(read-string ur-lon) (read-string ur-lat)]]
		(response (find-all-messages-by-bbox (bounding-box ll-vec ur-vec))))))

	(GET "/messages/:ll-lon/:ll-lat/:ur-lon/:ur-lat/:category" [ll-lon ll-lat ur-lon ur-lat category :as req]
		(with-role req ["email"]
		(let [ll-vec [(read-string ll-lon) (read-string ll-lat)]
			  ur-vec [(read-string ur-lon) (read-string ur-lat)]]
		(response (find-all-messages-by-bbox-and-category 
			(bounding-box ll-vec ur-vec) category)))))

	(GET "/messages/:id" [id :as req]
		(with-role req ["email" "public"]
		(let [user (:basic-authentication req)
			  message (find-message id user)]
			  (view-message-async (agent message))
			  (response message))))

	(POST "/messages" [:as req]
		(with-role req ["email"]
		(let [message (keywordize-keys (:body req))
			  user (:basic-authentication req)]
			(status (response (save-new-message message user)) 201))))

	(DELETE "/messages/:id" [id :as req]
		(with-role req ["email"]
			(response (delete-message id (:basic-authentication req)))))
)

