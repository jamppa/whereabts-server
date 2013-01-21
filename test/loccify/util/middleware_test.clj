(ns loccify.util.middleware-test
	(:use 
		[loccify.util.middleware]
		[midje.sweet]
		[ring.mock.request]))

(defn- expected-res [status body] {:status status :headers {} :body body})

(def handler-throws-iae (fn [req] (throw (IllegalArgumentException. "failed!"))))
(def expected-bad-request-res (expected-res 400 {:reason "failed!"}))

(fact "should give http bad request response when IllegalArgumentException catched"
	(let [handler (wrap-exception-handler handler-throws-iae)
		  response (handler (request :get "/some"))]
		  (= response expected-bad-request-res) => truthy))