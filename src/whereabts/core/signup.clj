(ns whereabts.core.signup
	(:use
		[whereabts.core.auth]
		[whereabts.models.user]))

(defn- can-signup? [user]
	(every? true? 
		[(available-username? (:name user)) (available-email? (:email user))]))

(defn- signup-user [usr]
	(let [saved-usr (save-user usr)]
		(if (nil? saved-usr) 
			(throw (IllegalArgumentException. "Couldn't save user!"))
			saved-usr)))

(defn signup [user]
	(if (can-signup? user) 
		(signup-user user) 
		(throw (IllegalArgumentException. "Couldn't signup user, name and/or email are not available!"))))