(ns whereabts.core.auth
	(:use [whereabts.models.anonymous-user]))

(defn- anon-whereabts-user? [email pwd]
	(and 
		(= email (anonymous-whereabts-user :email)) (= pwd (anonymous-whereabts-user :user-uuid))))

(defn authenticate [email pwd]
	(if (anon-whereabts-user? email pwd) 
		anonymous-whereabts-user 
		nil))