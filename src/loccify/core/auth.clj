(ns loccify.core.auth
	(:use [loccify.models.user]))

(defn- anon-loccify-user? [email pwd]
	(and 
		(= email (anon-loccify-user :email)) (= pwd (anon-loccify-user :password))))

(defn authenticate [email pwd]
	(if (anon-loccify-user? email pwd) 
		anon-loccify-user 
		(find-user-by-email-and-pass email pwd)))

(defn authenticated? [email pass]
	(let [user (find-user-by-email-and-pass email pass)]
		(not (nil? user))))

(defn available-username? [username]
	(nil? (find-user-by-name username)))

(defn available-email? [email]
	(nil? (find-user-by-email email)))