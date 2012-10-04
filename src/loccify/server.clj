(ns loccify.server
  (:require [noir.server :as server])
  (:use [loccify.db]))

(server/load-views-ns 'loccify.views)
(db-connect "loccify")

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'loccify})))
	