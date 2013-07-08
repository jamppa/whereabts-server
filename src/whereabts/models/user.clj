(ns whereabts.models.user
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]))

(def user-coll "users")
(def public-whereabts-user 
	{:email "anonymous@whereabts.com" :user-id "ae129325a4db22faab7771f10b39a8af" :role "public"})

(def user-validation
	(validation-set
		(presence-of :user-uuid)
		(presence-of :email)
		(presence-of :created-at)
		(presence-of :last-seen-at)
		(presence-of :role)
		(presence-of :profile_id)
		(length-of :user-uuid :is 36)
		(inclusion-of :role :in #{"email"})))

(defn by-uuid-and-email [uuid email]
	{:user-uuid uuid :email email})

(defn by-email [email]
	{:email email})

(defn find-user-by-id [id]
	(db-find-one-by-id user-coll (obj-id id)))

(defn find-user [query-map]
	(db-find-one user-coll query-map))

(defn save-new-user [usr]
	(if (valid? user-validation usr)
		(db-insert user-coll usr)
		(throw (IllegalArgumentException. "Invalid user"))))

(defn update-user [usr]
	(if (valid? user-validation usr)
		(db-save user-coll usr)
		(throw (IllegalArgumentException. "Invalid user"))))