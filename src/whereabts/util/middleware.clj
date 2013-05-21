(ns whereabts.util.middleware
    (:use 
        [ring.util.response])
    (:require [clojure.tools.logging :as logger])
    (:import 
        [whereabts.exception WhereabtsResourceNotFoundException]
        [whereabts.exception WhereabtsForbiddenException]))

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
            (catch WhereabtsResourceNotFoundException e
                (err-response 404 (.getMessage e)))
            (catch WhereabtsForbiddenException e
                (err-response 403 (.getMessage e)))
            (catch Exception e
                (.printStackTrace e)
            	(err-response 500 (.getMessage e))))))

(defn wrap-exception-logger [handler]
    (fn [req]
        (try
            (handler req)
            (catch Exception e
                (logger/error e "Error occured while handling request") 
                (throw e)))))
