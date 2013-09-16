(ns whereabts.api.follow-api
	(:use 
		whereabts.api.api-utils
		whereabts.core.users-follow
		compojure.core
		ring.util.response
		[clojure.walk :only [keywordize-keys]]))

(defn- get-followers-response-body [followers]
	{:followers followers})

(defroutes follow-api-routes

	(POST "/user/:id/followers" [id :as req]
		(with-role req ["email"]
			(-> 
				(follow-user id (:basic-authentication req))
				(response)
				(status 201))))

	(DELETE "/user/:id/followers" [id :as req]
		(with-role req ["email"]
			(-> 
				(unfollow-user id (:basic-authentication req))
				(response)
				(status 200))))

	(GET "/user/:id/followers" [id :as req]
		(with-role req ["email"]
			(-> (find-followers-of-user id)
				(get-followers-response-body)
				(response)
				(status 200))))

	)