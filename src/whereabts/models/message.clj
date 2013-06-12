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
		(presence-of :updated-at)
		(presence-of :expires-at)
		(presence-of :views)
		(presence-of :deleted)
		(length-of :message :within (range 1 251))
		(length-of :nick :within (range 1 21))
		(length-of :title :within (range 1 41) :allow-blank true)))

(defn new-message [msg-candidate]
	(merge 
		(select-keys msg-candidate [:user_id :nick :title :message :created-at :updated-at :expires-at])
		{:views 0 :deleted false 
		 :loc [
		 	(get-in msg-candidate [:loc :lon]) 
		 	(get-in msg-candidate [:loc :lat])]}))

(defn message-to-update [message]
	(select-keys message 
		[:_id :user_id :nick :message :title :loc :created-at :updated-at :views :deleted :expires-at]))

(defn find-message-by-id [id-str]
	(db-find-one-by-id message-coll (obj-id id-str)))

(defn save-message [message]
	(let [msg-candidate (new-message (updated-now (created-now message)))]
		(if (valid? message-validation-set msg-candidate)
			(db-insert message-coll msg-candidate)
			(throw (IllegalArgumentException. "Could not save invalid message!")))))

(defn find-messages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(with-collection message-coll
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}} :deleted false})
		(sort (sorted-map :updated-at -1))
		(limit messages-in-bbox-limit)))

(defn compactify-message [msg]
	(merge 
		(select-keys msg [:_id :loc :updated-at :created-at]) 
		{:short-message (short-message msg)}))

(defn update-message [msg]
	(let [msg-to-update (message-to-update msg)]
		(if (valid? message-validation-set msg-to-update)
			(db-save message-coll msg-to-update)
			(throw (IllegalArgumentException. "Could not save invalid message!")))))

(defn delete-and-update-message [msg]
	(update-message (merge msg {:deleted true})))