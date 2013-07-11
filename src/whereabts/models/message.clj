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
		(presence-of :message)
		(presence-of :loc)
		(presence-of :created-at)
		(presence-of :updated-at)
		(presence-of :expires-at)
		(presence-of :expire-time)
		(presence-of :views)
		(presence-of :deleted)
		(presence-of :category_id)
		(length-of :message :within (range 1 251))
		(length-of :title :within (range 1 41) :allow-blank true)))

(defn new-message [msg-candidate]
	(merge 
		(select-keys msg-candidate 
			[:user_id :title :message :created-at :updated-at :expires-at :expire-time :category_id])
		{:views 0 :deleted false 
		 :loc [
		 	(get-in msg-candidate [:loc :lon]) 
		 	(get-in msg-candidate [:loc :lat])]}))

(defn message-to-update [message]
	(select-keys message 
		[:_id :user_id :message :title :loc :created-at :updated-at :views :deleted :expires-at :expire-time :category_id]))

(defn find-message-by-id [id-str]
	(db-find-one-by-id message-coll (obj-id id-str)))

(defn save-message [message]
	(let [msg-candidate (new-message (updated-now (created-now message)))]
		(if (valid? message-validation-set msg-candidate)
			(db-insert message-coll msg-candidate)
			(throw (IllegalArgumentException. "Could not save invalid message!")))))

(defn find-messages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(with-collection message-coll
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}} 
			:deleted false 
			:expires-at {"$gt" (System/currentTimeMillis)}})
		(sort (sorted-map :updated-at -1))
		(limit messages-in-bbox-limit)))

(defn find-messages-by-bbox-and-category [{ll-vec :lower-left ur-vec :upper-right} category]
	(with-collection message-coll
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}} 
			:deleted false 
			:expires-at {"$gt" (System/currentTimeMillis)}
			:category_id category})
		(sort (sorted-map :updated-at -1))
		(limit messages-in-bbox-limit)))

(defn compactify-message [msg]
	(merge (select-keys msg [:_id :loc :updated-at :created-at :category_id]) 
		{:short-message (short-message msg)}))

(defn update-message [msg]
	(let [msg-to-update (message-to-update msg)]
		(if (valid? message-validation-set msg-to-update)
			(db-save message-coll msg-to-update)
			(throw (IllegalArgumentException. "Could not save invalid message!")))))

(defn delete-and-update-message [msg]
	(update-message (merge msg {:deleted true})))