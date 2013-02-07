(ns loccify.models.loccage
	(:use 
		[loccify.db]
		[loccify.util.geo]
		[loccify.models.util]
		[validateur.validation]))

(def loccage-col "loccages")
(def loccage-validation-set
	(validation-set
		(presence-of :message)
		(presence-of :user_id)
		(presence-of :loc)
		(presence-of :created-at)))

(defn find-loccage-by-id [id]
	(db-find (db-find-details :find-one loccage-col {:_id (obj-id id)})))

(defn find-loccages-near [location]
	(db-find 
		(db-find-details
			:find-many loccage-col 
			{:loc {"$near" [(:lon location) (:lat location)] "$maxDistance" (meters-to-degrees (:dist location))}})))

(defn save-loccage [loccage]
	(let [new-loccage (created-now loccage)]
	(when (valid? loccage-validation-set new-loccage)
		(db-insert loccage-col new-loccage))))

(defn find-loccages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(db-find
		(db-find-details :find-many loccage-col {:loc {"$within" {"$box" [ll-vec ur-vec]}}})))