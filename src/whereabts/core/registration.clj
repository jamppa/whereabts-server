(ns whereabts.core.registration
	(:use
		[whereabts.core.users]))

(defn register-user [usr profile]
	(let [already-registered (find-user-by-email (:email usr))]
		(if (not (nil? already-registered))
			already-registered
			(save-user usr))))

(defn register-gcm [usr gcm]
	(update-gcm-for-user usr gcm))