(ns whereabts.core.auth
	(:use 
		[whereabts.models.anonymous-user]
		[whereabts.models.util]))

(defn- public-whereabts-user? [email pwd]
	(and 
		(= email (public-whereabts-user :email))
		(= pwd (public-whereabts-user :user-id))))

(defn authenticated-now [user]
	(when user
		(update-anonymous-user (last-seen-now user))))

(defn authenticated-now-async [usr-agent]
	(send-off usr-agent authenticated-now))

(defn authenticate-whereabts-anon-user [email uuid]
	(if (public-whereabts-user? email uuid) 
		public-whereabts-user 
		nil))

(defn authenticate-anon-user [email uuid]
	(let [found-user (find-anonymous-user (by-uuid-and-email uuid email))]
		  (authenticated-now-async (agent found-user))
		  found-user))

(defn authenticate [email uuid]
	(let [usr (authenticate-whereabts-anon-user email uuid)]
		(if (nil? usr)
			(authenticate-anon-user email uuid)
			usr)))