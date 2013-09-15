(ns whereabts.models.profile
	(:refer-clojure :exclude [sort find])
	(:use
		whereabts.db
		whereabts.models.util
		validateur.validation
		monger.query
		monger.operators))

(def profiles-coll "profiles")
(def profile-validation
	(validation-set
		(presence-of :nick)
		(presence-of :country)
		(presence-of :user_id)
		(length-of :description :within (range 1 100) :allow-blank true)
		(length-of :photo :within (range 1 250) :allow-blank true)))

(defn find-profile-by-id [id]
	(db-find-one-by-id profiles-coll (obj-id id)))

(defn find-profile-by-user-id [user-id]
	(db-find-one profiles-coll {:user_id user-id}))

(defn find-profiles-by-user-ids [user-ids]
	(with-collection profiles-coll
		(find {:user_id {$in (map obj-id user-ids)}})))

(defn save-profile [profile]
	(if (valid? profile-validation profile)
		(db-save profiles-coll profile)
		(throw (IllegalArgumentException. "invalid profile"))))