(ns whereabts.api.feedbacks-api
	(:use
		[whereabts.core.feedbacks]
		[whereabts.api.api-utils]
		[compojure.core]
		[ring.util.response]
		[clojure.walk :only [keywordize-keys]]))

(defroutes feedbacks-api-routes

	(POST "/feedbacks" [:as req]
		(with-role req "anonymous"
			(let [feedback (keywordize-keys (:body req))]
			(-> (response (save-new-feedback feedback))
				(status 201)))))

	)