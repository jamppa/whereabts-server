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

	(POST "/userprofile" [:as req]
		(with-role req ["email"]
			(let [user (:basic-authentication req)
				  profile (extract-profile req)]
				  (-> (response (save-user-profile user profile))
				  	(status 201)))))

)