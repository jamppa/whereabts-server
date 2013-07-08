(ns whereabts.core.registration
	(:use
		[whereabts.core.users]))

(defn register-user [usr]
	(let [already-registered (find-user-by-email (:email usr))]
		(if (not (nil? already-registered))
			already-registered
			(save-user usr))))

(defn register-gcm-for-user [usr gcm]
	(update-gcm-for-user usr gcm))