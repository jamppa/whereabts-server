(ns loccify.api.signup
	(:use 
		[loccify.core.auth]
		[loccify.core.signup]
		[compojure.core]
		[ring.util.response]
		[monger.util]
		[clojure.walk :only [keywordize-keys]]))

(defn- name-availability [name is-avail] {:name name :available is-avail})
(defn- email-availability [email is-avail] {:email email :available is-avail})

(defroutes signup-routes

	(GET "/user/available/:name" [name]
		(response (name-availability name (available-username? name))))
	
	(GET "/email/available/:email" [email]
		(response (email-availability email (available-email? email))))

	(POST "/signup" [:as req]
		(response (signup (keywordize-keys (req :body)))))
	
	)
