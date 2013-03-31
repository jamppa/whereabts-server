(ns loccify.models.loccage
	(:refer-clojure :exclude [sort find])
	(:use 
		[loccify.db]
		[loccify.util.geo]
		[loccify.models.util]
		[validateur.validation]
		[monger.query]))

(def message-col "loccages")
(def message-validation-set
	(validation-set
		(presence-of :message)
		(presence-of :user_id)
		(presence-of :loc)
		(presence-of :created-at)))

(defn find-loccage-by-id [id]
	(db-find (db-find-details :find-one message-col {:_id (obj-id id)})))

(defn find-loccages-near [location]
	(with-collection message-col
		(find {:loc {"$near" [(:lon location) (:lat location)] "$maxDistance" (meters-to-degrees (:dist location))}})
		(sort (sorted-map :created-at -1))
		(limit 25)))

(defn save-loccage [loccage]
	(let [new-loccage (created-now loccage)]
	(when (valid? message-validation-set new-loccage)
		(db-insert message-col new-loccage))))

(defn find-messages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(with-collection message-col
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}}})
		(sort (sorted-map :created-at -1))
		(limit 25)))