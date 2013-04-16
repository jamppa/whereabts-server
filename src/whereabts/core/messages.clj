(ns whereabts.core.messages
	(:use
		[whereabts.models.message]
		[whereabts.models.user-message]))

(defn- all-messages [messages ]
	{:messages messages})

(defn anonymous-message [msg]
	(assoc msg :msg-type :anonymous))

(defn find-all-messages-by-bbox [bbox]
	(let [messages (find-anon-messages-by-bbox bbox)
		  compactified (map compactify-message messages)]
		(all-messages compactified)))

(defmulti save-new-message :msg-type)

(defmethod save-new-message :anonymous [msg]
	(save-message msg))

(defmethod save-new-message :user [msg]
	(save-user-message msg))