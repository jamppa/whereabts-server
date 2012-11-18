(ns loccify.db
	(:require [monger.core :as monger]
			  [monger.collection :as monger-collection])
	(:import [org.bson.types ObjectId]))

(declare merge-obj-id)
(defmulti db-find :find-type)

(defn db-connect [db-name]
	(monger/connect!)
	(monger/set-db! (monger/get-db db-name)))

(defn db-geospatialize [collection]
	(monger-collection/ensure-index collection {:loc "2d"}))

(defn db-insert [collection obj]
	(let [obj-with-id (merge-obj-id obj)]
		(monger-collection/insert-and-return collection obj-with-id)))

(defmethod db-find :find-one [query-details]
	(monger-collection/find-one-as-map (:collection query-details) (:query query-details)))

(defmethod db-find :find-many [query-details]
	(monger-collection/find-maps (:collection query-details) (:query query-details)))

(defn- merge-obj-id [obj]
	(let [oid (ObjectId.)]
		(merge obj {:_id oid})))