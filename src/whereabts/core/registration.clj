(ns whereabts.core.registration
	(:use
		[whereabts.models.anonymous-user]
		[whereabts.models.util]))

(defn anonymify [user]
	(merge user {:role "anonymous"}))

(defn register-anonymous-user [usr]
	(let [already-registered (find-anonymous-user usr)]
		(if (not (nil? already-registered))
			already-registered
			(save-anonymous-user 
				(anonymify (last-seen-now (created-now usr)))))))

(defn register-gcm-for-user [usr gcm-id]
	(update-anonymous-user (merge usr {:gcm-id gcm-id})))