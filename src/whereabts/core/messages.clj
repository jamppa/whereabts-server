(ns whereabts.core.messages
	(:use 
		[whereabts.models.message]
		[whereabts.models.reply]
		[whereabts.models.util]
		[whereabts.core.with-util]
		[whereabts.core.replies]
		[whereabts.core.categories]
		[whereabts.core.profiles]
		[whereabts.notification.like-notification])
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(defn user-owns-message? [message user]
	(= (obj-id-as-str user) 
		(id-as-str :user_id message)))

(defn find-all-messages-by-bbox [bbox]
	(->>
		(find-messages-by-bbox bbox) 
		(compactify-messages)
		(with-user-profiles)))

(defn save-new-message [msg usr]
	(compactify-message 
		(save-message 
			(with-category (with-user msg usr)))))

(defn view-and-update-message [msg]
	(update-message 
		(update-in msg [:views] inc)))

(defn find-message [id user]
	(if-let [message (find-message-by-id id)]
		(-> (view-and-update-message message) 
			(with-ownership user)
			(with-liked user)
			(with-likes-as-number)
			(with-user-profile)
			(with-replies)
			(with-expiration message-expiration-time-ms))
		(throw (WhereabtsResourceNotFoundException.))))

(defn delete-message [id user]
	(let [message (find-message-by-id id)]
		  (when (user-owns-message? message user)
		  	(delete-message-by-id (:_id message))
		  	(delete-replies-by-message message)) 
		message))

(defn add-user-to-likes [message user]
	(if (nil? (some #{(obj-id-as-str user)} (:likes message)))
		(assoc-in message [:likes] 
			(vec (conj (:likes message) (obj-id-as-str user))))
		message))

(defn like-message [id user]
	(let [message (find-message-by-id id)] 
		(-> message
			(add-user-to-likes user)
			(update-message)
			(publish-like-message user))))

(defn find-following-messages [follower page]
	(if-let [following (:following follower)]
		(-> (find-messages-by-users following page)
			(compactify-messages)
			(with-user-profiles))
		[]))