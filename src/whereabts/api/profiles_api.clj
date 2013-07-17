(ns whereabts.api.profiles-api
	(:use
		[whereabts.api.api-utils]
		[whereabts.core.profiles]
		[compojure.core]
		[ring.util.response]
		[clojure.walk :only [keywordize-keys]]))

(defn- extract-profile [req]
	(let [body (keywordize-keys (:body req))]
		(:profile body)))

(defroutes profiles-api-routes

	(POST "/user" [:as req]
		(with-role req ["email"]
			(let [user (:basic-authentication req)
				  profile (extract-profile req)]
				  (-> (response (save-user-profile user profile))
				  	(status 201)))))

	(GET "/user" [:as req]
		(with-role req ["email"]
			(-> (:basic-authentication req)
				(find-user-profile)
				(response))))

	(GET "/user/:id" [id :as req]
		(with-role req ["email"]
			(-> id
				(find-profile-of-user)
				(response))))

)