(ns whereabts.models.message
	(:refer-clojure :exclude [sort find])
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]
		[monger.query]))

(def message-expiration-time-ms (* 3 86400000)) ; 3 days
(def messages-in-bbox-limit 20)
(def message-coll "messages")

(def message-validation-set
	(validation-set
		(presence-of :user_id)
		(presence-of :message)
		(presence-of :loc)
		(presence-of :created-at)
		(presence-of :updated-at)
		(presence-of :views)
		(presence-of :category_id)
		(length-of :message :within (range 1 251))))

(defn new-message [msg-candidate]
	(merge 
		(select-keys msg-candidate 
			[:user_id :message :created-at :updated-at :category_id])
		{:views 0 
		 :loc [
		 	(get-in msg-candidate [:loc :lon]) 
		 	(get-in msg-candidate [:loc :lat])]}))

(defn message-to-update [msg]
	(select-keys msg 
		[:_id :user_id :message :loc :created-at :updated-at :views :category_id]))

(defn find-message-by-id [id-str]
	(db-find-one-by-id message-coll (obj-id id-str)))

(defn save-message [msg-details]
	(let [msg-candidate (new-message (updated-now (created-now msg-details)))]
		(if (valid? message-validation-set msg-candidate)
			(db-insert message-coll msg-candidate)
			(throw (IllegalArgumentException. "Could not save invalid message!")))))

(defn find-messages-by-bbox [{ll-vec :lower-left ur-vec :upper-right}]
	(with-collection message-coll
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}} 
			:created-at {"$gte" (- (System/currentTimeMillis) message-expiration-time-ms)}})
		(sort (sorted-map :updated-at -1))
		(limit messages-in-bbox-limit)))

(defn find-messages-by-bbox-and-category [{ll-vec :lower-left ur-vec :upper-right} category]
	(with-collection message-coll
		(find {:loc {"$within" {"$box" [ll-vec ur-vec]}}  
			:created-at {"$gte" (- (System/currentTimeMillis) message-expiration-time-ms)}
			:category_id category})
		(sort (sorted-map :updated-at -1))
		(limit messages-in-bbox-limit)))

(defn compactify-message [msg]
	(select-keys msg 
		[:_id :loc :updated-at :created-at :category_id :message]))

(defn update-message [msg]
	(let [msg-to-update (message-to-update msg)]
		(if (valid? message-validation-set msg-to-update)
			(db-save message-coll msg-to-update)
			(throw (IllegalArgumentException. "Could not save invalid message!")))))

(defn delete-message-by-id [msg-oid]
	(db-delete message-coll msg-oid))