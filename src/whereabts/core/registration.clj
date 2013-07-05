(ns whereabts.core.registration
	(:use
		[whereabts.models.user]
		[whereabts.models.util]))

(defn with-email-role [user]
	(merge user {:role "email"}))

(defn register-user [usr]
	(let [already-registered (find-user usr)]
		(if (not (nil? already-registered))
			already-registered
			(save-new-user 
				(with-email-role (last-seen-now (created-now usr)))))))

(defn register-gcm-for-user [usr gcm-id]
	(when (nil? gcm-id) (throw (IllegalArgumentException. "invalid GCM id!")))
	(update-user (merge usr {:gcm-id gcm-id})))