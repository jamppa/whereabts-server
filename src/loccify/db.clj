(ns loccify.db
    (:use 
        [loccify.models.util]
        [monger.result :only [ok?]])
    (:require 
        [monger.core :as monger]
        [monger.collection :as monger-col]
        [monger.json]))

(def ^:dynamic *loccify-db* "loccify")

(defmulti db-find :find-type)

(defn db-find-details [type col query]
    {:find-type type :collection col :query query})

(defn db-connect []
    (monger/connect!)
    (monger/set-db! (monger/get-db *loccify-db*)))

(defn db-geospatialize [collections]
    (doseq [coll collections]
        (monger-col/ensure-index coll {:loc "2d"})))
    
(defn db-insert [collection obj]
    (let [obj-with-id (with-obj-id obj)]
        (if (ok? (monger-col/insert collection obj-with-id))
        obj-with-id
        (throw (Exception. "db write failed!")))))

(defmethod db-find :find-one [find-details]
    (monger-col/find-one-as-map (:collection find-details) (:query find-details)))

(defmethod db-find :find-many [find-details]
    (doall (monger-col/find-maps (:collection find-details) (:query find-details))))