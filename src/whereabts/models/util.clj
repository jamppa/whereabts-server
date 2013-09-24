(ns whereabts.models.util
	(:use [clojure.string :only [split join]])
	(:require [monger.util :as util])
	(:import [org.bson.types ObjectId]))
		
(defn obj-id [hex]
	(ObjectId. (.toString hex)))

(defn with-obj-id [obj]
	(assoc obj :_id (util/object-id)))

(defn created-now [obj]
	(assoc obj :created-at (System/currentTimeMillis)))

(defn updated-now [obj]
	(merge obj {:updated-at (System/currentTimeMillis)}))

(defn last-seen-now [obj]
	(merge obj {:last-seen-at (System/currentTimeMillis)}))

(defn obj-id-as-str [obj]
	(if (nil? (:_id obj))
		""
		(.toString (:_id obj))))

(defn id-as-str [obj field]
	(.toString (field obj)))

(defn oids-as-str [coll oid-field]
	(->> 
		(map oid-field coll)
		(filter identity)
		(map #(.toString %))
		(distinct)))

(defn find-first [f coll]
	(first (filter f coll)))