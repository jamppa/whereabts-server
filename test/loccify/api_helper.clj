(ns loccify.api-helper)

(defn loccify-request [method uri & params]
	{:request-method method :uri uri :params (first params)})