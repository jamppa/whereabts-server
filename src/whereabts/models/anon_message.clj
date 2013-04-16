(ns whereabts.models.anon-message
	(:refer-clojure :exclude [sort find])
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]
		[monger.query]))

(def anon-message-coll "messages")

(defn- anon-message-validation-set []
	(validation-set
		(presence-of :nick)
		(presence-of :message)
		(presence-of [:loc :lon])
		(presence-of [:loc :lat])
		(presence-of :created-at)
		(length-of :message :within (range 1 251))
		(length-of :nick :within (range 1 21))
		(length-of :title :within (range 1 41) :allow-blank true)
		(numericality-of [:loc :lon])
		(numericality-of [:loc :lat])))

(defn new-anon-message [msg-candidate]
	{
		:nick (:nick msg-candidate)
		:title (:title msg-candidate)
		:message (:message msg-candidate)
		:loc [(get-in msg-candidate [:loc :lon]) (get-in msg-candidate [:loc :lat])]
		:created-at (:created-at msg-candidate)})

(defn find-anon-message-by-id [id-str]
	(db-find
		(db-find-details
			:find-one anon-message-coll {:_id (obj-id id-str)})))

(defn save-anon-message [message]
	(let [msg-candidate (created-now message)]
		(if (valid? (anon-message-validation-set) msg-candidate)
			(db-insert anon-message-coll (new-anon-message msg-candidate))
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