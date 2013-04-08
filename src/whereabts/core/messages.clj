(ns whereabts.core.messages
	(:use
		[whereabts.models.message]
		[whereabts.models.anon-message]))

(defn- all-messages [messages anon-messages]
	{:messages messages :anon-messages anon-messages})

(defn anonymous-message [msg]
	(assoc msg :msg-type :anonymous))

(defn find-all-messages-by-bbox [bbox]
	(let [messages (find-messages-by-bbox bbox)
		anon-messages (find-anon-messages-by-bbox bbox)]
		(all-messages messages anon-messages)))

(defmulti save-new-message :msg-type)

(defmethod save-new-message :anonymous [msg]
	(save-anon-message msg))

(defmethod save-new-message :user [msg]
	(save-message msg))