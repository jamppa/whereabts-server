(ns loccify.models.loccage
	(:refer-clojure :exclude [sort find])
	(:use 
		[loccify.db]
		[loccify.util.geo]
		[loccify.models.util]
		[validateur.validation]
		[monger.query]))

(def message-coll "messages")
(def message-validation-set
	(validation-set
		(presence-of :message)
		(presence-of :user_id)
		(presence-of :loc)
		(presence-of :created-at)))

(defn find-message-by-id [id]
	(db-find (db-find-details :find-one message-coll {:_id (obj-id id)})))

(defn find-messages-near [location]
	(with-collection message-coll
		(find {:loc {"$near" [(:lon location) (:lat location)] "$maxDistance" (meters-to-degrees (:dist location))}})
		(sort (sorted-map :created-at -1))
		(limit 25)))

(defn save-message [message]
	(let [new-message (created-now message)]
	(when (valid? message-validation-set new-message)
		(db-insert message-coll new-message))))

(defn find-messages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(with-collection message-coll
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}}})
		(sort (sorted-map :created-at -1))
		(limit 25)))