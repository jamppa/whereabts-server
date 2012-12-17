(ns loccify.api.signup
	(:use [loccify.core.auth]
			[compojure.core]
			[ring.util.response]))

(defroutes signup-routes

	(GET "/user/available/:name" [name]
		(response {:name name :available (available-username? name)}))
	
	(GET "/email/available/:email" [email]
		(response {:email email :available (available-email? email)})))