(ns whereabts.core.registration
	(:use
		[whereabts.models.anonymous-user]
		[whereabts.models.util]))

(defn register-anonymous-user [usr]
	(let [already-registered (find-anonymous-user usr)]
		(if (not (nil? already-registered))
			already-registered
			(save-anonymous-user (created-now usr)))))