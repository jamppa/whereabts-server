(ns whereabts.models.profile
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]))

(def profiles-coll "profiles")
(def profile-validation
	(validation-set
		(presence-of :nick)
		(presence-of :country)
		(presence-of :user_id)
		(length-of :description :within (range 1 100) :allow-blank true)))

(defn find-profile-by-id [id]
	(if (nil? id) nil
	(db-find-one-by-id profiles-coll (obj-id id))))

(defn save-profile [profile]
	(if (valid? profile-validation profile)
		(db-save profiles-coll profile)
		(throw (IllegalArgumentException. "invalid profile"))))