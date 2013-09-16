(ns whereabts.core.users-follow
	(:use
		whereabts.models.user
		whereabts.models.profile
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

(defn remove-follower [user-following user-follower]
	(-> (assoc-in user-following [:followers]
		(vec (disj (set (:followers user-following)) (obj-id-as-str user-follower))))
		(update-user)))

(defn remove-following [user-follower user-following]
	(-> (assoc-in user-follower [:following]
		(vec (disj (set (:following user-follower)) (obj-id-as-str user-following))))
		(update-user)))

(defn unfollow-user [user-following-id user-follower]
	(if-let [user-following (find-user-by-id user-following-id)]
		(do
			(remove-follower user-following user-follower)
			(remove-following user-follower user-following))
		(throw (WhereabtsResourceNotFoundException.))))

(defn find-followers-of-user [user-id]
	(if-let [user (find-user-by-id user-id)]
		(find-profiles-by-user-ids (:followers user))))