(ns whereabts.core.profiles
	(:use
		[whereabts.core.users]
		[whereabts.core.with-util]
		[whereabts.models.profile])
	(:import 
		[org.bson.types ObjectId]
		[whereabts.exception WhereabtsResourceNotFoundException]))

(defn- has-profile? [user]
	(= (type (:profile_id user)) org.bson.types.ObjectId))

(defn update-profile [user profile]
	(save-profile 
		(merge profile {:_id (:profile_id user)})))

(defn create-profile [user profile]
	(let [saved-profile (save-profile (with-user profile user))]
		(set-profile-for-user user saved-profile)
	saved-profile))

(defn save-user-profile [user profile]
	(if (has-profile? user)
		(update-profile user profile)
		(create-profile user profile)))

(defn find-user-profile [user]
	(when (not (has-profile? user)) (throw (WhereabtsResourceNotFoundException.)))
	(let [profile-id (:profile_id user)]
		(find-profile-by-id (.toString profile-id))))