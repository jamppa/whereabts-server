(ns whereabts.api.registration-api
	(:use
		[whereabts.core.registration]
		[compojure.core]
		[ring.util.response]
		[clojure.walk :only [keywordize-keys]]))

(defn- extract-user [req]
	(let [body (keywordize-keys (:body req))]
		{:user-uuid (:user-uuid body) :email (:email body)}))

(defroutes registration-api-routes

	(POST "/anonymousregistration" [:as req]
		(let [user (extract-user req)]
			(-> (response (register-anonymous-user user))
				(status 201))))

	)
