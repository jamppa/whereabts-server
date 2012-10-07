(ns loccify.models.loccage
	(:use [loccify.db])
	(:import [org.bson.types ObjectId]))

(def loccage-collection-name "loccages")

(defn save-loccage [loccage]
	(db-insert loccage-collection-name loccage))
