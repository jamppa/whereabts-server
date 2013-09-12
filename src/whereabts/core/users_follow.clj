(ns whereabts.core.users-follow
	(:use
		whereabts.models.user)
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(defn add-follower [user follower]
	nil)

(defn add-following [follower user]
	nil)

(defn follow-user [user-id user-follower]
	(if-let [user (find-user-by-id user-id)]
		(do 
			(add-follower user user-follower)
			(add-following user-follower user))
		(throw (WhereabtsResourceNotFoundException.))))