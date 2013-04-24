(ns whereabts.api.feedbacks-api
	(:use
		[whereabts.core.feedbacks]
		[compojure.core]
		[ring.util.response]))

(defroutes feedbacks-api-routes

	(POST "/feedbacks" [:as req]
		(-> (response (save-new-feedback (:body req)))
			(status 201)))

	)