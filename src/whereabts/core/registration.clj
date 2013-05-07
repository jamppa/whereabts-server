(ns whereabts.core.registration
	(:use
		[whereabts.models.anonymous-user]
		[whereabts.models.util]))

(defn register-anonymous-user [usr]
	(save-anonymous-user (created-now usr)))