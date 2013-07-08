(ns whereabts.models.profile
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]))

(def profiles-coll "profiles")
(def profile-validation
	(validation-set
		(presence-of :nick)
		(presence-of :country)))

(defn find-profile-by-id [id]
	(db-find-one-by-id profiles-coll (obj-id id)))

(defn find-profile-by-user [user]
	(db-find-one profiles-coll {:user_id (:_id user)}))

(defn save-profile [profile]
	(if (valid? profile-validation profile)
		(db-save profiles-coll profile)
		(throw (IllegalArgumentException. "invalid profile"))))