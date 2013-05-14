(ns whereabts.functional.api-functest
	(:use
		[ring.adapter.jetty]
		[whereabts.server]))

(def whereabts-api-testsrv "http://localhost:3000/api")

(defn whereabts-api-request [auth body]
	{:accept :json
	 :content-type :json
	 :basic-auth auth
	 :body body
	 :coerce :always
	 :throw-exceptions false})

(defn whereabts-api-request-public-user [body]
	(whereabts-api-request 
		["anonymous@whereabts.com" "ae129325a4db22faab7771f10b39a8af"] body))

(defn whereabts-api-request-anon-user [creds payload]
	(whereabts-api-request creds payload))

(defonce jetty-server (run-jetty server {:port 3000 :join? false}))