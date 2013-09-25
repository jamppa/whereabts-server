(ns whereabts.api.api-utils
	(:import [whereabts.exception WhereabtsForbiddenException]))

(defn check-role [req roles]
	(let [user (:basic-authentication req)
		  user-role (:role user)]
		(when-not (some #(= user-role %) roles)
			(throw (WhereabtsForbiddenException.)))))

(defmacro with-role [req roles & form]
    `(let [] (check-role ~req ~roles) ~@form))

(defn as-int [string]
	(Integer. string))

(defn as-long [string]
	(Long. string))