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

(defn save-loccage [loccage]
	(when (valid? (create-validation-set-for-loccage) loccage)
		(db-insert loccage-collection-name loccage)))
