(ns whereabts.models.message
	(:refer-clojure :exclude [sort find])
	(:use
		whereabts.db
		whereabts.models.util
		validateur.validation
		monger.query
		monger.operators)
	(:require [monger.collection :as monger]))

(def message-expiration-time-ms (* 12 3600000)) ; 12h
(def messages-in-bbox-limit 15)
(def message-coll "messages")

(def message-validation-set
	(validation-set
		(presence-of :user_id)
		(presence-of :message)
		(presence-of :loc)
		(presence-of :created-at)
		(presence-of :updated-at)
		(presence-of :views)
		(presence-of :category)
		(presence-of :likes)
		(presence-of :photo)
		(length-of :message :within (range 1 251))))

(defn new-message [msg-candidate]
	(merge 
		(select-keys msg-candidate 
			[:user_id :message :created-at :updated-at :category])
		{:views 0 :likes [] :photo false
		 :loc [
		 	(get-in msg-candidate [:loc :lon]) 
		 	(get-in msg-candidate [:loc :lat])]}))

(defn message-to-update [msg]
	(select-keys msg 
		[:_id :user_id :message :loc :created-at :updated-at :views :likes :category :photo]))

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

(defn compactify-message [msg]
	(select-keys msg 
		[:_id :user_id :loc :updated-at :created-at :category :message]))

(defn compactify-messages [coll]
	(map compactify-message coll))

(defn update-message [msg]
	(let [msg-to-update (message-to-update msg)]
		(if (valid? message-validation-set msg-to-update)
			(db-save message-coll msg-to-update)
			(throw (IllegalArgumentException. "Could not save invalid message!")))))

(defn delete-message-by-id [msg-oid]
	(db-delete message-coll msg-oid))

(defn find-messages-by-users [user-ids skipped]
	(with-collection message-coll
		(find {:user_id {$in (map obj-id user-ids)}})
		(sort (sorted-map :created-at -1))
		(limit 10)
		(skip skipped)))

(defn find-messages-by-users-older-than [user-ids skipped older-than]
	(with-collection message-coll
		(find {:user_id {$in (map obj-id user-ids)} :created-at {$lte older-than}})
		(sort (sorted-map :created-at -1))
		(limit 20)
		(skip skipped)))

(defn count-messages-by-user [user-id]
	(monger/count message-coll {:user_id (obj-id user-id)}))