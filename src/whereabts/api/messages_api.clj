(ns whereabts.api.messages-api
	(:use
		[compojure.core]
		[ring.util.response]
		[whereabts.core.messages]
		[whereabts.util.geo]
		[clojure.walk :only [keywordize-keys]]))

(defroutes messages-api-routes

	(GET "/messages/:ll-lon/:ll-lat/:ur-lon/:ur-lat" [ll-lon ll-lat ur-lon ur-lat]
		(let [ll-vec [(read-string ll-lon) (read-string ll-lat)]
			  ur-vec [(read-string ur-lon) (read-string ur-lat)]]
		(response (find-all-messages-by-bbox (bounding-box ll-vec ur-vec)))))

	(POST "/messages" [:as req]
		(let [message (keywordize-keys (:body req))]
			(status (response (save-new-message message)) 201)))
)

