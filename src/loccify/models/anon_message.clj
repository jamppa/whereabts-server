(ns loccify.models.anon-message
	(:refer-clojure :exclude [sort find])
	(:use
		[loccify.db]
		[loccify.models.util]
		[validateur.validation]
		[monger.query]))

(def anon-message-col "anon-messages")
(def anon-message-validation-set
	(validation-set
		(presence-of :message)
		(presence-of :nick)
		(presence-of :loc)
		(presence-of :created-at)))

(defn find-anon-message-by-id [id-str]
	(db-find
		(db-find-details
			:find-one anon-message-col {:_id (obj-id id-str)})))

(defn save-anon-message [message]
	(let [new-message (created-now message)]
		(when (valid? anon-message-validation-set new-message)
			(db-insert anon-message-col new-message))))

(defn find-anon-messages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(with-collection anon-message-col
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}}})
		(sort (sorted-map :created-at -1))
		(limit 25)))