(ns whereabts.models.user-message
	(:refer-clojure :exclude [sort find])
	(:use 
		[whereabts.db]
		[whereabts.util.geo]
		[whereabts.models.util]
		[validateur.validation]
		[monger.query]))

(def user-message-coll "user_messages")
(def message-validation-set
	(validation-set
		(presence-of :message)
		(presence-of :user_id)
		(presence-of :loc)
		(presence-of :created-at)))

(defn find-message-by-id [id]
	(db-find 
		(db-find-details :find-one user-message-coll {:_id (obj-id id)})))

(defn find-user-messages-near [location]
	(with-collection user-message-coll
		(find {:loc {"$near" [(:lon location) (:lat location)] "$maxDistance" (meters-to-degrees (:dist location))}})
		(sort (sorted-map :created-at -1))
		(limit 25)))

(defn save-user-message [message]
	(let [new-message (created-now message)]
	(when (valid? message-validation-set new-message)
		(db-insert user-message-coll new-message))))

(defn find-user-messages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(with-collection user-message-coll
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}}})
		(sort (sorted-map :created-at -1))
		(limit 25)))