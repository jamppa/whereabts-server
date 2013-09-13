(ns whereabts.core.users-follow
	(:use
		whereabts.models.user
		whereabts.models.util)
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(defn add-follower [user follower]
	(-> (assoc-in user [:followers] 
		(vec (set (conj (:followers user) (obj-id-as-str follower)))))
		(update-user)))

(defn add-following [follower user]
	nil)

(defn follow-user [user-id user-follower]
	(if-let [user (find-user-by-id user-id)]
		(do 
			(add-follower user user-follower)
			(add-following user-follower user))
		(throw (WhereabtsResourceNotFoundException.))))