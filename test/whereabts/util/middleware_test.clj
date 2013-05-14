(ns whereabts.util.middleware-test
	(:use 
		[whereabts.util.middleware]
		[midje.sweet]
		[ring.mock.request])
	(:import 
		[whereabts.exception WhereabtsResourceNotFoundException]
		[whereabts.exception WhereabtsForbiddenException]))

(defn- expected-res [status body] {:status status :headers {} :body body})

(def handler-throws-iae (fn [req] (throw (IllegalArgumentException. "failed!"))))
(def handler-throws-e (fn [req] (throw (Exception. "failed!"))))
(def handler-throws-wrnfe (fn [req] (throw (WhereabtsResourceNotFoundException. "failed!"))))
(def handler-throws-wrfbdne (fn [req] (throw (WhereabtsForbiddenException. "failed!"))))
(def expected-bad-request-res (expected-res 400 {:reason "failed!"}))
(def expected-internal-server-err-res (expected-res 500 {:reason "failed!"}))
(def expected-not-found-res (expected-res 404 {:reason "failed!"}))
(def expected-forbidden-res (expected-res 403 {:reason "failed!"}))

(fact "should give http bad request response when IllegalArgumentException catched"
	(let [handler (wrap-exception-handler handler-throws-iae)
		  response (handler (request :get "/some"))]
		  (= response expected-bad-request-res) => truthy))

(fact "should give http internal server error response when Exception is catched"
	(let [handler (wrap-exception-handler handler-throws-e)
		  response (handler (request :get "/some"))]
		  (= response expected-internal-server-err-res) => truthy))

(fact "should give http not found response when WhereabtsResourceNotFoundException is catched"
	(let [handler (wrap-exception-handler handler-throws-wrnfe)
		  response (handler (request :get "/some"))]
		  (= response expected-not-found-res) => truthy))

(fact "should give http forbidden response when WhereabtsForbiddenException is catched"
	(let [handler (wrap-exception-handler handler-throws-wrfbdne)
		  response (handler (request :get "/some"))]
		  (= response expected-forbidden-res) => truthy))