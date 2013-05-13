(ns whereabts.core.auth
	(:use 
		[whereabts.models.anonymous-user]
		[whereabts.models.util]))

(defn- anon-whereabts-user? [email pwd]
	(and 
		(= email (anonymous-whereabts-user :email)) (= pwd (anonymous-whereabts-user :user-id))))

(defn authenticated-now [user]
	(when user
		(last-seen-now user)))

(defn authenticate-whereabts-anon-user [email pwd]
	(if (anon-whereabts-user? email pwd) 
		anonymous-whereabts-user 
		nil))

(defn authenticate [email uuid]
	(authenticated-now 
		(find-anonymous-user (by-uuid-and-email uuid email))))