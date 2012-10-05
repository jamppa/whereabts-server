(ns loccify.models.user
	(:use [loccify.db]))

(def user-collection-name "users")

(defn create-user [user]
	(db-insert user-collection-name user))