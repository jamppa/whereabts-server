(ns whereabts.api.profiles-api
	(:use
		[whereabts.api.api-utils]
		[whereabts.core.profiles]
		[whereabts.core.profiles-search]
		[compojure.core]
		[ring.util.response]
		[clojure.walk :only [keywordize-keys]]))

(defn- extract-profile [req]
	(let [body (keywordize-keys (:body req))]
		(:profile body)))

(defn- users-response-body [users]
	{:users users})

(defroutes profiles-api-routes

	(POST "/users" [:as req]
		(with-role req ["email"]
			(let [user (:basic-authentication req)
				  profile (extract-profile req)]
				  (-> (response (save-user-profile user profile))
				  	(status 201)))))

	(GET "/users" [:as req]
		(with-role req ["email"]
			(-> (:basic-authentication req)
				(find-user-profile)
				(response))))

	(GET "/users/:id" [id :as req]
		(with-role req ["email"]
			(-> id
				(find-profile-of-user (:basic-authentication req))
				(response))))

	(GET "/recent/users" [:as req]
		(with-role req ["email"]
			(-> (find-recent-profiles (:basic-authentication req))
				(users-response-body)
				(response))))

)