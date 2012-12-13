(ns loccify.core.auth
	(:use loccify.models.user))

(defn authenticated? [email pass]
	(let [user (find-user-by-email-and-pass email pass)]
		(not (nil? user))))

(defn username-available? [name]
	)
