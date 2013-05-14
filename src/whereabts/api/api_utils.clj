(ns whereabts.api.api-utils
	(:import [whereabts.exception WhereabtsForbiddenException]))

(defn check-role [req role]
	(let [user (:basic-authentication req)
		  user-role (:role user)]
		(when-not (= user-role role)
			(throw (WhereabtsForbiddenException.)))))

(defmacro with-role [req role & form]
    `(let [] (check-role ~req ~role) ~@form))