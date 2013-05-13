(ns whereabts.core.auth
	(:use [whereabts.models.anonymous-user]))

(defn- anon-whereabts-user? [email pwd]
	(and 
		(= email (anonymous-whereabts-user :email)) (= pwd (anonymous-whereabts-user :user-id))))

(defn authenticate-whereabts-anon-user [email pwd]
	(if (anon-whereabts-user? email pwd) 
		anonymous-whereabts-user 
		nil))