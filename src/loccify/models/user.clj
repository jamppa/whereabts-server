(ns loccify.models.user
	(:use [loccify.db]
		  [validateur.validation])
	(:import [org.bson.types ObjectId]))

(defn- create-validation-set-for-user []
	(validation-set 
		(presence-of :name)
		(presence-of :email)
		(presence-of :password)
		(presence-of :type)))

(def user-collection-name "users")
(def user-validation-set (create-validation-set-for-user))

(defn- create-query [type query]
	{:find-type type 
		:query query 
		:collection user-collection-name})
 
(defn save-user [user]
	(when (valid? user-validation-set user) 
		(db-insert user-collection-name user)))

(defn find-user-by-id [id]
	(db-find (create-query :find-one {:_id (ObjectId. id)})))

(defn find-user-by-email-and-pass [email pass]
	(db-find (create-query :find-one {:email email :password pass})))

(defn find-user-by-name [name]
	(db-find (create-query :find-one {:name name})))

(defn find-user-by-email [email]
	(db-find (create-query :find-one {:email email})))