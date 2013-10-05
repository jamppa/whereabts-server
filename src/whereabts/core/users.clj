(ns whereabts.core.users
	(:use
		whereabts.models.user
		whereabts.models.profile
		whereabts.models.util
		whereabts.models.message))

(defn with-email-role [user]
	(merge user {:role "email"}))

(defn with-empty-followers [user]
	(merge user {:followers []}))

(defn with-empty-followings [user]
	(merge user {:following []}))

(defn with-messages-count [{user-id :_id :as user}]
	(merge user {:messages-count (count-messages-by-user user-id)}))

(defn save-user [user]
	(-> user
		(created-now)
		(last-seen-now)
		(with-email-role)
		(with-empty-followers)
		(with-empty-followings)
		(save-new-user)))

(defn delete-user [user]
	(delete-user-by-id (:_id user)))

(defn find-user-by-email [email]
	(find-user (by-email email)))

(defn update-gcm-for-user [user gcm]
	(when (nil? gcm) (throw (IllegalArgumentException. "invalid gcm")))
	(update-user (merge user {:gcm-id gcm})))

(defn set-profile-for-user [user profile]
	(update-user (merge user {:profile_id (:_id profile)})))