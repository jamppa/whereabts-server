(ns loccify.api.signup
	(:use [compojure.core]
			[ring.util.response]))

(defroutes signup-routes
	(GET "/signup/user/available/:name" [name]
		(response {:name name :available true}))
	
	(GET "/signup/email/available/:email" [email]
		(response {:email email :available true})))