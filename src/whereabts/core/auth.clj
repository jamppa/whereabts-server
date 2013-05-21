(ns whereabts.core.auth
	(:use 
		[whereabts.models.anonymous-user]
		[whereabts.models.util]))

(defn- anon-whereabts-user? [email pwd]
	(and 
		(= email (anonymous-whereabts-user :email))
		(= pwd (anonymous-whereabts-user :user-id))))

(defn authenticated-now [user]
	(when user
		(update-anonymous-user (last-seen-now user))))

(defn authenticate-whereabts-anon-user [email uuid]
	(if (anon-whereabts-user? email uuid) 
		anonymous-whereabts-user 
		nil))

(defn authenticate-anon-user [email uuid]
	(let [found-user (find-anonymous-user (by-uuid-and-email uuid email))]
		  (authenticated-now found-user)
		  found-user))

(defn authenticate [email uuid]
	(let [usr (authenticate-whereabts-anon-user email uuid)]
		(if (nil? usr)
			(authenticate-anon-user email uuid)
			usr)))