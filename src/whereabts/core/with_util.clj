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

(defn with-expiration [message expiration]
	(let [expires-at (+ (:created-at message) expiration)]
		(merge message {:expires-at expires-at})))

(defn with-liked [message user]
	(if (some #{(obj-id-as-str user)} (:likes message))
		(merge message {:liked true})
		(merge message {:liked false})))

(defn with-likes-as-number [message]
	(merge message {:likes (count (:likes message))}))

(defn with-followed? [following follower]
	(if (some #{(obj-id-as-str follower)} (:followers following))
		(assoc following :followed true)
		(assoc following :followed false)))

(defn with-following-as-number [user]
	(assoc user :following (count (:following user))))

(defn with-followers-as-number [user]
	(assoc user :followers (count (:followers user))))