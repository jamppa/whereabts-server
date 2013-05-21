(ns whereabts.models.anonymous-user
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]))

(def anonymous-user-coll "anonymous_users")
(def anonymous-whereabts-user 
	{:email "anonymous@whereabts.com" :user-id "ae129325a4db22faab7771f10b39a8af" :role "public"})

(def anonymous-user-validations
	(validation-set
		(presence-of :user-uuid)
		(presence-of :email)
		(presence-of :created-at)
		(presence-of :last-seen-at)
		(presence-of :role)
		(length-of :user-uuid :is 36)
		(inclusion-of :email :in #{(:email anonymous-whereabts-user)})))
		(inclusion-of :role :in #{"anonymous"})

(defn by-uuid-and-email [uuid email]
	{:user-uuid uuid :email email})

(defn find-anonymous-user-by-id [id]
	(db-find-one-by-id anonymous-user-coll (obj-id id)))

(defn find-anonymous-user [query-map]
	(db-find-one anonymous-user-coll query-map))

(defn save-anonymous-user [usr]
	(if (valid? anonymous-user-validations usr)
		(db-insert anonymous-user-coll usr)
		(throw (IllegalArgumentException. "Invalid anonymous user!"))))

(defn update-anonymous-user [usr]
	(if (valid? anonymous-user-validations usr)
		(db-save anonymous-user-coll usr)
		(throw (IllegalArgumentException. "Invalid anonymous user!"))))