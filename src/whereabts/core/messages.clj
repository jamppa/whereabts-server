(ns whereabts.core.messages
	(:use 
		[whereabts.models.message]
		[whereabts.models.util]
		[whereabts.core.with-util]
		[whereabts.core.replies])
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(defn- all-messages [messages ]
	{:messages messages})

(defn user-owns-message? [message user]
	(= (obj-id-as-str user) (id-as-str :user_id message)))

(defn view-message [msg]
	(let [views-so-far (:views msg)]
		(merge msg {:views (inc views-so-far)})))

(defn find-all-messages-by-bbox [bbox]
	(let [messages (find-messages-by-bbox bbox)
		  compactified (map compactify-message messages)]
		(all-messages compactified)))

(defn save-new-message [msg usr]
	(compactify-message 
		(save-message (with-user msg usr))))

(defn view-and-update-message [msg]
	(update-message (view-message msg)))

(defn find-message [id user]
	(if-let [message (find-message-by-id id)]
		(-> message 
			(with-ownership user)
			(with-replies))
		(throw (WhereabtsResourceNotFoundException.))))

(defn delete-message [id user]
	(let [message (find-message-by-id id)
		  owner (user-owns-message? message user)]
		  (if owner 
		  	(delete-and-update-message message) 
		  	message)))