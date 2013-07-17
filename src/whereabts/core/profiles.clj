(ns whereabts.core.profiles
	(:use
		[whereabts.core.users]
		[whereabts.core.with-util]
		[whereabts.models.profile]
		[whereabts.models.util]
		[whereabts.models.user])
	(:import 
		[org.bson.types ObjectId]
		[whereabts.exception WhereabtsResourceNotFoundException]))

(defn- has-profile? [user]
	(contains? user :profile_id))

(defn update-profile [user profile]
	(let [profile-id (:profile_id user)
		  user-id (:_id user)]
	(save-profile 
		(merge profile {:_id profile-id :user_id user-id}))))

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

(defn find-profile-of-user [user-id]
	(if-let [user (find-user-by-id user-id)]
		(merge user {:user-profile (find-user-profile user)})
		(throw (WhereabtsResourceNotFoundException.))))

(defn with-user-profile [obj]
	(if (contains? obj :user_id)
		(merge obj {:user-profile (find-profile-by-user-id (:user_id obj))})
		obj))