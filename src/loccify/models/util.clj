(ns loccify.models.util
	(:use [monger.util]))

(defn with-obj-id [obj]
	(assoc obj :_id (object-id)))

(defn created-now [obj]
	(assoc obj :created-at (System/currentTimeMillis)))