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

(defn authenticated-now-async [usr-agent]
	(send-off usr-agent authenticated-now))

(defn authenticate-whereabts-anon-user [email pwd]
	(if (anon-whereabts-user? email pwd) 
		anonymous-whereabts-user 
		nil))

(defn authenticate [email uuid]
	(let [found-user (find-anonymous-user (by-uuid-and-email uuid email))]
		  (authenticated-now-async (agent found-user))
		  found-user))