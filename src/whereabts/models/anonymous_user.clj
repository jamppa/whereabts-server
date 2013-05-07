(ns whereabts.models.anonymous-user
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]))

(def anonymous-user-coll "anonymous_users")
(def anonymous-whereabts-user 
	{:email "anonymous@whereabts.com" :user-id "ae129325a4db22faab7771f10b39a8af"})

(def anonymous-user-validations
	(validation-set
		(presence-of :user-uuid)
		(presence-of :email)))

(defn find-anonymous-user-by-id [id]
	(db-find-one-by-id anonymous-user-coll (obj-id id)))

(defn save-anonymous-user [usr]
	(if (valid? anonymous-user-validations usr)
		(db-insert anonymous-user-coll usr)
		(throw (IllegalArgumentException. "Invalid anonymous user!"))))