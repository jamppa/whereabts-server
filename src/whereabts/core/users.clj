(ns whereabts.core.users
	(:use
		[whereabts.models.user]
		[whereabts.models.profile]
		[whereabts.models.util]))

(defn with-email-role [user]
	(merge user {:role "email"}))

(defn with-no-profile [user]
	(merge user {:profile_id "0"}))

(defn save-user [user]
	(-> user
		(created-now)
		(last-seen-now)
		(with-email-role)
		(with-no-profile)
		(save-new-user)))

(defn find-user-by-email [email]
	(find-user (by-email email)))

(defn update-gcm-for-user [user gcm]
	(when (nil? gcm) (throw (IllegalArgumentException. "invalid gcm")))
	(update-user (merge user {:gcm-id gcm})))

(defn set-profile-for-user [user profile]
	(update-user 
		(merge user {:profile_id (:_id profile)})))