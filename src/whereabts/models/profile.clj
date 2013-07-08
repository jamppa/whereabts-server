(ns whereabts.models.profile
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]))

(def profiles-coll "profiles")

(defn find-profile-by-id [id]
	(db-find-one-by-id profiles-coll (obj-id id)))