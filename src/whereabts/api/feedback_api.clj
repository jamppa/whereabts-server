(ns whereabts.api.feedback-api
	(:use
		[whereabts.core.feedbacks]
		[compojure.core]
		[ring.util.response]))

(defroutes feedback-api-routes

	(POST "/feedbacks" [:as req]
		(-> (response (save-new-feedback (:body req)))
			(status 201)))

	)