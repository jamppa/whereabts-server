(ns whereabts.api-helper)

(def email-roled-user {:role "email" :_id "some"})
(def public-roled-user {:role "public"})

(defn whereabts-request [method uri & body]
	{:request-method method :uri uri :body (first body)})

(defn whereabts-request-as-public-user [method uri & body]
	(assoc (whereabts-request method uri (first body)) :basic-authentication public-roled-user))

(defn whereabts-request-as-anonymous-user [method uri & body]
	(assoc (whereabts-request method uri (first body)) :basic-authentication email-roled-user))

(defn expected-res [status body] {:status status :headers {} :body body})