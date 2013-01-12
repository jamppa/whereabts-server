(ns loccify.api.signup
	(:use 
		[loccify.core.auth]
		[loccify.core.signup]
		[compojure.core]
		[ring.util.response])
	(:import [loccify.exception SignUpException]))

(defn- reason [msg] {:reason msg})

(defroutes signup-routes

	(GET "/user/available/:name" [name]
		(response {:name name :available (available-username? name)}))
	
	(GET "/email/available/:email" [email]
		(response {:email email :available (available-email? email)}))

	(POST "/signup" {user-details :params}
		(try
			(response (signup user-details))
		(catch SignUpException e
			(-> (response (reason (.getMessage e)))
				(status 400)))))
	)
