(ns whereabts.core.users
	(:use
		[whereabts.models.user]
		[whereabts.models.profile]
		[whereabts.models.util]))

(defn with-email-role [user]
	(merge user {:role "email"}))

(defn with-no-profile [user]
	(merge user {:profile_id ""}))

(defn save-user [user]
	(-> user
		(created-now)
		(last-seen-now)
		(with-email-role)
		(save-new-user)))

(defn find-user-by-email [email]
	(find-user (by-email email)))

(defn update-gcm-for-user [user gcm]
	(when (nil? gcm) (throw (IllegalArgumentException. "invalid gcm")))
	(update-user (merge user {:gcm-id gcm})))