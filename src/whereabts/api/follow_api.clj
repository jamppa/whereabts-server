(ns whereabts.api.follow-api
	(:use 
		whereabts.api.api-utils
		compojure.core
		ring.util.response
		[clojure.walk :only [keywordize-keys]]))

(defroutes follow-api-routes

	(POST "/user/:id/followers" [:as req]
		(-> (response)
			(status 201)))

	)