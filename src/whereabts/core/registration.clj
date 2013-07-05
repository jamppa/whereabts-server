(ns whereabts.core.registration
	(:use
		[whereabts.models.user]
		[whereabts.models.util]))

(defn anonymify [user]
	(merge user {:role "anonymous"}))

(defn register-anonymous-user [usr]
	(let [already-registered (find-user usr)]
		(if (not (nil? already-registered))
			already-registered
			(save-new-user 
				(anonymify (last-seen-now (created-now usr)))))))

(defn register-gcm-for-user [usr gcm-id]
	(when (nil? gcm-id) (throw (IllegalArgumentException. "invalid GCM id!")))
	(update-user (merge usr {:gcm-id gcm-id})))