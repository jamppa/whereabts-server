(ns whereabts.core.registration
	(:use
		[whereabts.core.users]
		[whereabts.models.user]
		[whereabts.models.util]))

(defn register-user [usr]
	(let [already-registered (find-user (by-email (:email usr)))]
		(if (not (nil? already-registered))
			already-registered
			(save-user usr))))

(defn register-gcm-for-user [usr gcm-id]
	(when (nil? gcm-id) (throw (IllegalArgumentException. "invalid GCM id!")))
	(update-user (merge usr {:gcm-id gcm-id})))