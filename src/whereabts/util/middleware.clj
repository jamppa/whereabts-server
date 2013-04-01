(ns whereabts.util.middleware
    (:use [ring.util.response]))

(defn- reason [msg] {:reason msg})
(defn- err-response [res-status msg]
    (->
        (response (reason msg))
        (status res-status)))

(defn wrap-exception-handler [handler]
    (fn [req]
        (try
            (handler req)
            (catch IllegalArgumentException e
                (err-response 400 (.getMessage e)))
            (catch Exception e
            	(err-response 500 (.getMessage e))))))