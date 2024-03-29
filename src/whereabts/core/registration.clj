(ns whereabts.core.registration
	(:use
		[whereabts.core.users]
		[whereabts.core.profiles]))

(defn register-new-user [user profile]
	(let [saved-user (save-user user)]
		(try
			(save-user-profile saved-user profile)
		(catch Exception e
			(delete-user saved-user)
			(throw e)))
		saved-user))

(defn register-user [usr profile]
	(let [already-registered (find-user-by-email (:email usr))]
		(if (not (nil? already-registered))
			already-registered
			(register-new-user usr profile))))

(defn register-gcm [usr gcm]
	(update-gcm-for-user usr gcm))