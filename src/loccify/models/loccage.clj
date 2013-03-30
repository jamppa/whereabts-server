(ns loccify.models.loccage
	(:refer-clojure :exclude [sort find])
	(:use 
		[loccify.db]
		[loccify.util.geo]
		[loccify.models.util]
		[validateur.validation]
		[monger.query]))

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
	(with-collection loccage-col
		(find {:loc {"$near" [(:lon location) (:lat location)] "$maxDistance" (meters-to-degrees (:dist location))}})
		(sort (sorted-map :created-at -1))
		(limit 25)))

(defn save-loccage [loccage]
	(let [new-loccage (created-now loccage)]
	(when (valid? loccage-validation-set new-loccage)
		(db-insert loccage-col new-loccage))))

(defn find-loccages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(with-collection loccage-col
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}}})
		(sort (sorted-map :created-at -1))
		(limit 25)))