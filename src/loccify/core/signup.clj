(ns loccify.core.signup
	(:use
		[loccify.core.auth]
		[loccify.models.user]))

(defn- can-signup? [user]
	(every? true? 
		[(available-username? (:name user)) (available-email? (:email user))]))

(defn signup [user]
	(if (can-signup? user) (save-user user) nil))