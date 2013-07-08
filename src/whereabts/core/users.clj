(ns whereabts.core.users
	(:use
		[whereabts.models.user]
		[whereabts.models.util]))

(defn with-email-role [user]
	(merge user {:role "email"}))

(defn save-user [user]
	(-> user
		(created-now)
		(last-seen-now)
		(with-email-role)
		(save-new-user)))

(defn find-user-by-email [email]
	(find-user (by-email email)))