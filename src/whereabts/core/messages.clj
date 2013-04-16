(ns whereabts.core.messages
	(:use
		[whereabts.models.message]))

(defn- all-messages [messages ]
	{:messages messages})

(defn find-all-messages-by-bbox [bbox]
	(let [messages (find-messages-by-bbox bbox)
		  compactified (map compactify-message messages)]
		(all-messages compactified)))

(defn save-new-message [msg]
	(save-message msg))