(ns loccify.core.auth
	(:use loccify.models.user))

(defn authenticated? [email pass]
	(let [user (find-user-by-email-and-pass email pass)]
		(not (nil? user))))

(defn available-username? [username]
	(nil? (find-user-by-name username)))

(defn available-email? [email]
	(nil? (find-user-by-email email)))