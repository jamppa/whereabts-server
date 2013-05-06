(ns whereabts.core.auth
	(:use [whereabts.models.user]))

(defn- anon-whereabts-user? [email pwd]
	(and 
		(= email (anon-whereabts-user :email)) (= pwd (anon-whereabts-user :user-id))))

(defn authenticate [email pwd]
	(if (anon-whereabts-user? email pwd) 
		anon-whereabts-user 
		(find-user-by-email-and-pass email pwd)))

(defn available-username? [username]
	(nil? (find-user-by-name username)))

(defn available-email? [email]
	(nil? (find-user-by-email email)))