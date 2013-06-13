(ns whereabts.core.expirations)

(def default-expiration-time 86400000)

(defn current-time-millis []
	(System/currentTimeMillis))

(defn expires-at [message]
	(if (not (nil? (:expire-time message)))
	(merge message 
		{:expires-at (+ (:expire-time message) (current-time-millis))})
	(merge message
		{:expires-at (+ default-expiration-time (current-time-millis)) 
			:expire-time default-expiration-time})))