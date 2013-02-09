(ns loccify.api-helper)

(defn loccify-request [method uri & body]
	{:request-method method :uri uri :body (first body)})

(defn expected-res [status body] {:status status :headers {} :body body})