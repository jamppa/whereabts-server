(ns whereabts.models.anonymous-user
	(:use
		[whereabts.db]
		[whereabts.models.util]))

(def anonymous-user-coll "anonymous_users")
(def anonymous-whereabts-user 
	{:email "anonymous@whereabts.com" :user-id "ae129325a4db22faab7771f10b39a8af"})

(defn find-anonymous-user-by-id [id]
	(db-find-one-by-id anonymous-user-coll (obj-id id)))

(defn save-anonymous-user [usr]
	(db-insert anonymous-user-coll usr))