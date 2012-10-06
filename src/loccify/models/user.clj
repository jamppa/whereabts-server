(ns loccify.models.user
	(:use [loccify.db])
	(:import [org.bson.types ObjectId]))

(declare query-details)
(def user-collection-name "users")

(defn create-user [user]
	(db-insert user-collection-name user))

(defn find-user-by-id [id]
	(db-find (query-details :find-one {:_id (ObjectId. id)} user-collection-name)))

(defn- query-details [type query collection-name]
	{:find-type type :query query :collection collection-name})