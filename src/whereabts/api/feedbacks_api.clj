(ns whereabts.api.feedbacks-api
	(:use
		[whereabts.core.feedbacks]
		[compojure.core]
		[ring.util.response]
		[clojure.walk :only [keywordize-keys]]))

(defroutes feedbacks-api-routes

	(POST "/feedbacks" [:as req]
		(let [feedback (keywordize-keys (:body req))]
		(-> (response (save-new-feedback feedback))
			(status 201))))

	)