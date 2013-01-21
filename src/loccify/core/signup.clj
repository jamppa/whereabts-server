(ns loccify.core.signup
	(:use
		[loccify.core.auth]
		[loccify.models.user])
	(:import [loccify.exception SignUpException]))

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