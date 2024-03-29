(ns whereabts.api.messages-api
	(:use
		[whereabts.api.api-utils]
		[compojure.core]
		[ring.util.response]
		[whereabts.core.messages]
		[whereabts.core.with-util]
		[whereabts.util.geo]
		[clojure.walk :only [keywordize-keys]]))

(defn- messages-response-body [messages]
	{:messages messages})

(defroutes messages-api-routes

	(GET "/messages/:ll-lon/:ll-lat/:ur-lon/:ur-lat" [ll-lon ll-lat ur-lon ur-lat :as req]
		(with-role req ["email"]
		(let [ll-vec [(read-string ll-lon) (read-string ll-lat)]
			  ur-vec [(read-string ur-lon) (read-string ur-lat)]]
			  (-> 
			  	(find-all-messages-by-bbox (bounding-box ll-vec ur-vec))
			  	(messages-response-body)
			  	(response)))))

	(GET "/messages/:id" [id :as req]
		(with-role req ["email" "public"]
		(let [user (:basic-authentication req)
			  message (find-message id user)]
			  (response message))))

	(POST "/messages" [:as req]
		(with-role req ["email"]
		(let [message (keywordize-keys (:body req))
			  user (:basic-authentication req)]
			(status (response (save-new-message message user)) 201))))

	(DELETE "/messages/:id" [id :as req]
		(with-role req ["email"]
			(response (delete-message id (:basic-authentication req)))))

	(POST "/messages/:id/likes" [id :as req]
		(with-role req ["email"]
			(-> id
				(like-message (:basic-authentication req))
				(response)
				(status 201))))

	(GET "/messages/following/:skip" [skip :as req]
		(with-role req ["email"]
			(-> (find-following-messages (:basic-authentication req) (as-int skip))
				(messages-response-body)
				(response))))

	(GET "/messages/following/:skip/:older-than" [skip older-than :as req]
		(with-role req ["email"]
			(-> (find-following-messages-older-than (:basic-authentication req) (as-int skip) (as-long older-than))
				(messages-response-body)
				(response))))

	(GET "/users/:id/messages/:skip/:older-than" [id skip older-than :as req]
		(with-role req ["email"]
			(-> (find-users-messages-older-than id (as-int skip) (as-long older-than))
				(messages-response-body)
				(response))))
)

