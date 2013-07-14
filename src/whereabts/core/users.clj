(ns whereabts.core.users
	(:use
		[whereabts.models.user]
		[whereabts.models.profile]
		[whereabts.models.util]))

(defn with-email-role [user]
	(merge user {:role "email"}))

(defn with-no-profile [user]
	(merge user {:profile_id ""}))

(defn with-profile [user]
	(if (contains? user :profile_id)
		user
		(with-no-profile user)))

(defn save-user [user]
	(-> user
		(created-now)
		(last-seen-now)
		(with-email-role)
		(save-new-user)
		(with-no-profile)))

(defn find-user-by-email [email]
	(if-let [user (find-user (by-email email))]
		(with-profile user)))

(defn update-gcm-for-user [user gcm]
	(when (nil? gcm) (throw (IllegalArgumentException. "invalid gcm")))
	(with-profile 
		(update-user (merge user {:gcm-id gcm}))))

(defn set-profile-for-user [user profile]
	(update-user 
		(merge user {:profile_id (:_id profile)})))