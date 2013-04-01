(ns loccify.core.messages
	(:use
		[loccify.models.message]
		[loccify.models.anon-message]))

(defn- all-messages [messages anon-messages]
	{:messages messages :anon-messages anon-messages})

(defn find-all-messages-by-bbox [bbox]
	(let [messages (find-messages-by-bbox bbox)
		anon-messages (find-anon-messages-by-bbox bbox)]
		(all-messages messages anon-messages)))