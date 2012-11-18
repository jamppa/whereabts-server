(ns loccify.models.loccage
	(:use [loccify.db]
		  [validateur.validation])
	(:import [org.bson.types ObjectId]))

(defn- create-validation-set-for-loccage []
	(validation-set
		(presence-of :message)
		(presence-of :user_id)
		(presence-of :loc)))

(def loccage-collection-name "loccages")

(defn location [lon lat dist]
	{:lon lon :lat lat :dist dist})

(defn find-loccage-by-id [id]
	(db-find {
		:find-type :find-one 
		:collection loccage-collection-name 
		:query {:_id (ObjectId. id)}}))

(defn find-loccages-near [location]
	(db-find {
		:find-type :find-many
		:collection loccage-collection-name
		:query {:loc {"$near" [(:lon location) (:lat location)], "$maxDistance" 10} }}))

(defn save-loccage [loccage]
	(when (valid? (create-validation-set-for-loccage) loccage)
		(db-insert loccage-collection-name loccage)))
