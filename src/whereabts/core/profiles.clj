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

(defn filter-profile-by-user-id [user-id profiles]
	(find-first #(= (.toString (:user_id %)) (.toString user-id)) profiles))

(defn update-profile [user profile-details]
	(let [profile-id (:profile_id user)
		  user-id (:_id user)]
	(save-profile 
		(merge profile-details {:_id profile-id :user_id user-id}))))

(defn create-profile [user profile-details]
	(let [saved-profile (save-profile (with-user profile-details user))]
		(set-profile-for-user user saved-profile)
	saved-profile))

(defn save-user-profile [user profile-details]
	(if (has-profile? user)
		(update-profile user profile-details)
		(create-profile user profile-details)))

(defn find-user-profile [user]
	(when (not (has-profile? user)) (throw (WhereabtsResourceNotFoundException.)))
	(let [profile-id (:profile_id user)]
		(find-profile-by-id (.toString profile-id))))

(defn find-profile-of-user [user-id auth-user]
	(if-let [user (find-user-by-id user-id)]
		(do 
			(-> (merge user {:user-profile (find-user-profile user)})
				(with-followed? auth-user)
				(with-following-as-number)
				(with-followers-as-number)))
		(throw (WhereabtsResourceNotFoundException.))))

(defn with-user-profile 
	([obj] 
		(if (contains? obj :user_id)
			(merge obj {:user-profile (find-profile-by-user-id (:user_id obj))})
			obj))
	([obj profile]
		(when-not (nil? profile)
			(merge obj {:user-profile profile}))))

(defn with-user-profiles [objs]
	(let [user-ids (oids-as-str objs :user_id)
		  profiles (find-profiles-by-user-ids user-ids)]
		  (filter identity 
		  	(map #(with-user-profile % (filter-profile-by-user-id (:user_id %) profiles)) objs))))