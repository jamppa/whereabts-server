(ns loccify.models.loccage
	(:use 
		[loccify.db]
		[loccify.util.geo]
		[loccify.models.util]
		[validateur.validation])
	(:import [org.bson.types ObjectId]))


(def loccage-collection-name "loccages")
(def loccage-validation-set
	(validation-set
		(presence-of :message)
		(presence-of :user_id)
		(presence-of :loc)
		(presence-of :created-at)))

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
	(let [new-loccage (created-now loccage)]
	(when (valid? loccage-validation-set new-loccage)
		(db-insert loccage-collection-name new-loccage))))
