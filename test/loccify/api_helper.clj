(ns loccify.api-helper)

(defn loccify-request [method uri & body]
	{:request-method method :uri uri :body (first body)})