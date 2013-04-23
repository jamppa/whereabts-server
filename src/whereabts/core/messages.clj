(ns whereabts.core.messages
	(:use [whereabts.models.message])
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(defn- all-messages [messages ]
	{:messages messages})

(defn find-all-messages-by-bbox [bbox]
	(let [messages (find-messages-by-bbox bbox)
		  compactified (map compactify-message messages)]
		(all-messages compactified)))

(defn save-new-message [msg]
	(compactify-message (save-message msg)))

(defn find-message [id]
	(if-let [message (find-message-by-id id)]
		message
		(throw (WhereabtsResourceNotFoundException.))))