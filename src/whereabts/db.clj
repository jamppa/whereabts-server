(ns whereabts.db
    (:refer-clojure :exclude [sort find])
    (:use 
        [monger.result :only [ok?]]
        [monger.query]
        [monger.operators]
        [whereabts.models.util])
    (:require 
        [monger.core :as monger]
        [monger.collection :as monger-col]
        [monger.json]))

(def ^:dynamic *whereabts-db* "whereabtsdb")

(defn db-connect []
    (monger/connect!)
    (monger/set-db! (monger/get-db *whereabts-db*)))
    
(defn db-insert [collection obj]
    (let [obj-with-id (with-obj-id obj)]
        (if (ok? (monger-col/insert collection obj-with-id))
        obj-with-id
        (throw (Exception. "db write failed!")))))

(defn db-find-one [coll query]
    (monger-col/find-one-as-map coll query))

(defn db-find-one-by-id [coll oid]
    (monger-col/find-map-by-id coll oid))

(defn db-save [coll obj]
    (monger-col/save-and-return coll obj))
