(ns loccify.models.loccage
	(:use 
		[loccify.db]
		[loccify.util.geo]
		[validateur.validation])
	(:import [org.bson.types ObjectId]))

(def loccage-validation-set
	(validation-set
		(presence-of :message)
		(presence-of :user_id)
		(presence-of :loc)))

(def loccage-collection-name "loccages")

(defn find-loccage-by-id [id]
	(db-find {
		:find-type :find-one 
		:collection loccage-collection-name 
		:query {:_id (ObjectId. id)}}))

(defn find-loccages-near [location]
	(db-find {
		:find-type :find-many
		:collection loccage-collection-name
		:query {:loc {
			"$near" [(:lon location) (:lat location)] 
			"$maxDistance" (meters-to-degrees (:dist location))}}}))

(defn save-loccage [loccage]
	(when (valid? loccage-validation-set loccage)
		(db-insert loccage-collection-name loccage)))
