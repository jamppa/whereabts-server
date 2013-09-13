(ns whereabts.core.users-follow
	(:use
		whereabts.models.user
		whereabts.models.util)
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(defn add-follower [user follower]
	(-> (assoc-in user [:followers] 
		(vec (set (conj (:followers user) (obj-id-as-str follower)))))
		(update-user)))

(defn add-following [user following]
	(-> (assoc-in user [:following]
		(vec (set (conj (:following user) (obj-id-as-str following)))))
		(update-user)))

(defn follow-user [user-following-id user-follower]
	(if-let [user (find-user-by-id user-following-id)]
		(do 
			(add-follower user user-follower)
			(add-following user-follower user))
		(throw (WhereabtsResourceNotFoundException.))))

(defn remove-follower [user follower]
	nil)

(defn remove-following [user following]
	nil)

(defn unfollow-user [user-following-id user-follower]
	(if-let [user-following (find-user-by-id user-following-id)]
		(do
			(remove-follower user-following user-follower)
			(remove-following user-follower user-following))
		(throw (WhereabtsResourceNotFoundException.))))