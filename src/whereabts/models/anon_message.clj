(ns whereabts.models.anon-message
	(:refer-clojure :exclude [sort find])
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]
		[monger.query]))

(def anon-message-coll "anonymous_messages")
(defn- anon-message-validation-set []
	(validation-set
		(presence-of :nick)
		(length-of :title :within (range 1 30) :allow-blank true)
		(presence-of :message)
		(presence-of :loc)
		(presence-of :created-at)))

(defn find-anon-message-by-id [id-str]
	(db-find
		(db-find-details
			:find-one anon-message-coll {:_id (obj-id id-str)})))

(defn save-anon-message [message]
	(let [new-message (created-now message)]
		(if (valid? (anon-message-validation-set) new-message)
			(db-insert anon-message-coll new-message)
			(throw (IllegalArgumentException. "Could not save invalid message!")))))

(defn find-anon-messages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(with-collection anon-message-coll
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}}})
		(sort (sorted-map :created-at -1))
		(limit 25)))

(defn compactify-anon-message [msg]
	{
		:_id (:_id msg)
		:loc (:loc msg)
		:short-message (short-message msg)
		:created-at (:created-at msg)})