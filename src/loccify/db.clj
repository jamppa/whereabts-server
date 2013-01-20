(ns loccify.db
	(:use 
		[loccify.models.util]
		[monger.result :only [ok?]])
	(:require [monger.core :as monger]
			  [monger.collection :as monger-col])
	(:import [org.bson.types ObjectId]))

(defmulti db-find :find-type)

(defn db-connect [db-name]
	(monger/connect!)
	(monger/set-db! (monger/get-db db-name)))

(defn db-geospatialize [collection]
	(monger-col/ensure-index collection {:loc "2d"}))

(defn db-insert [collection obj]
	(let [obj-with-id (with-obj-id obj)]
		(if (ok? (monger-col/insert collection obj-with-id))
		obj-with-id
		(throw (Exception. "db write failed!")))))

(defmethod db-find :find-one [query-details]
	(monger-col/find-one-as-map (:collection query-details) (:query query-details)))

(defmethod db-find :find-many [query-details]
	(monger-col/find-maps (:collection query-details) (:query query-details)))