(ns whereabts.api.registration-api
	(:use
		[whereabts.api.api-utils]
		[whereabts.core.registration]
		[compojure.core]
		[ring.util.response]
		[clojure.walk :only [keywordize-keys]]))

(defn- extract-user [req]
	(let [body (keywordize-keys (:body req))]
		{:user-uuid (:user-uuid body) :email (:email body)}))

(defn- extract-gcm-id [req]
	(let [body (keywordize-keys (:body req))]
		(:gcm-id body)))

(defroutes registration-api-routes
	
	(POST "/register_user" [:as req]
		(with-role req ["public"]
		(let [user (extract-user req)]
			(-> (response (register-user user))
				(status 201)))))

	(POST "/register_gcm" [:as req]
		(with-role req ["email"]
			(let [user (:basic-authentication req)]
				(-> (response (register-gcm user (extract-gcm-id req)))
					(status 201)))))

	)
