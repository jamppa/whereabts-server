(ns whereabts.models.user
	(:use 
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]))

(def anon-whereabts-user {:name "Anonymous" :email "anonymous@whereabts.com" :password "ae129325a4db22faab7771f10b39a8af"})
(def user-collection-name "users")
(def user-validation-set 
	(validation-set 
		(presence-of :name)
		(presence-of :email)
		(presence-of :password)
		(presence-of :created-at)))

(defn- create-query [type query]
	{:find-type type 
		:query query 
		:collection user-collection-name})
 
(defn save-user [user]
	(let [new-user (created-now user)]
	(when (valid? user-validation-set new-user) 
		(db-insert user-collection-name new-user))))

(defn find-user-by-id [id]
	(db-find (create-query :find-one {:_id (obj-id id)})))

(defn find-user-by-email-and-pass [email pass]
	(db-find (create-query :find-one {:email email :password pass})))

(defn find-user-by-name [name]
	(db-find (create-query :find-one {:name name})))

(defn find-user-by-email [email]
	(db-find (create-query :find-one {:email email})))