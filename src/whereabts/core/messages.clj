(ns whereabts.core.messages
	(:use [whereabts.models.message])
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(defn- all-messages [messages ]
	{:messages messages})

(defn- with-user [msg usr]
	(merge msg {:user_id (:_id usr)}))

(defn view-message [msg]
	(let [views-so-far (:views msg)]
	(merge msg {:views (inc views-so-far)})))

(defn find-all-messages-by-bbox [bbox]
	(let [messages (find-messages-by-bbox bbox)
		  compactified (map compactify-message messages)]
		(all-messages compactified)))

(defn save-new-message [msg usr]
	(compactify-message (save-message (with-user msg usr))))

(defn view-and-update-message [msg]
	(update-message (view-message msg)))

(defn find-message [id]
	(if-let [message (find-message-by-id id)]
		message
		(throw (WhereabtsResourceNotFoundException.))))