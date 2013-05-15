(ns whereabts.models.message
	(:refer-clojure :exclude [sort find])
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]
		[monger.query]))

(def messages-in-bbox-limit 20)
(def message-coll "messages")

(def message-validation-set
	(validation-set
		(presence-of :user_id)
		(presence-of :nick)
		(presence-of :message)
		(presence-of :loc)
		(presence-of :created-at)
		(presence-of :views)
		(presence-of :deleted)
		(length-of :message :within (range 1 251))
		(length-of :nick :within (range 1 21))
		(length-of :title :within (range 1 41) :allow-blank true)))

(defn new-message [msg-candidate]
	{
		:user_id (:user_id msg-candidate)
		:nick (:nick msg-candidate)
		:title (:title msg-candidate)
		:message (:message msg-candidate)
		:loc [(get-in msg-candidate [:loc :lon]) (get-in msg-candidate [:loc :lat])]
		:created-at (:created-at msg-candidate)
		:views 0
		:deleted false})

(defn find-message-by-id [id-str]
	(db-find
		(db-find-details
			:find-one message-coll {:_id (obj-id id-str)})))

(defn save-message [message]
	(let [msg-candidate (new-message (created-now message))]
		(if (valid? message-validation-set msg-candidate)
			(db-insert message-coll msg-candidate)
			(throw (IllegalArgumentException. "Could not save invalid message!")))))

(defn find-messages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(with-collection message-coll
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}}})
		(sort (sorted-map :created-at -1))
		(limit messages-in-bbox-limit)))

(defn compactify-message [msg]
	{
		:_id (:_id msg)
		:loc (:loc msg)
		:short-message (short-message msg)
		:created-at (:created-at msg)})

(defn update-message [msg]
	(if (valid? message-validation-set msg)
		(db-save message-coll msg)
		(throw (IllegalArgumentException. "Could not save invalid message!"))))

(defn delete-and-update-message [msg]
	nil)