(ns whereabts.core.with-util
	(:use [whereabts.models.util]))

(defn with-user [obj user]
	(merge obj {:user_id (:_id user)}))

(defn with-message [obj message]
	(merge obj {:message_id (:_id message)}))

(defn with-ownership [obj user]
	(if (= (id-as-str :user_id obj) (obj-id-as-str user))
		(merge obj {:owns true})
		(merge obj {:owns false})))

(defn with-liked [message user]
	(if (some #{(obj-id-as-str user)} (:likes message))
		(merge message {:liked true})
		(merge message {:liked false})))
