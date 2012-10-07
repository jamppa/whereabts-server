(ns loccify.models.user
	(:use [loccify.db]
			[validateur.validation])
	(:import [org.bson.types ObjectId]))

(declare create-query-details)
(declare create-validation-set-for-user)
 
(def user-collection-name "users")
(def user-validation-set (create-validation-set-for-user))

(defn save-user [user]
	(when (valid? user-validation-set user) 
		(db-insert user-collection-name user)))

(defn find-user-by-id [id]
	(db-find 
		(create-query-details :find-one {:_id (ObjectId. id)} user-collection-name)))

(defn- create-query-details [type query collection-name]
	{:find-type type :query query :collection collection-name})

(defn- create-validation-set-for-user [] 
	(validation-set 
		(presence-of :name)
		(presence-of :email)
		(presence-of :_id)))