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
		(length-of :nick :within (range 1 21))
		(length-of :title :within (range 1 41) :allow-blank true)
		(presence-of :message)
		(length-of :message :within (range 1 251))
		(presence-of [:loc :lon])
		(presence-of [:loc :lat])
		(presence-of :created-at)))

(defn new-anon-message [msg-candidate]
	{
		:nick (:nick msg-candidate)
		:title (:title msg-candidate)
		:message (:message msg-candidate)
		:loc (:loc msg-candidate)
		:created-at (:created-at msg-candidate)})

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