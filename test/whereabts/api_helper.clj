(ns whereabts.api-helper)

(defn whereabts-request [method uri & body]
	{:request-method method :uri uri :body (first body)})

(defn whereabts-request-as-public-user [method uri & body]
	(assoc (whereabts-request method uri (first body)) :basic-authentication {:role "public"}))

(defn whereabts-request-as-anonymous-user [method uri & body]
	(assoc (whereabts-request method uri (first body)) :basic-authentication {:role "anonymous"}))

(defn expected-res [status body] {:status status :headers {} :body body})