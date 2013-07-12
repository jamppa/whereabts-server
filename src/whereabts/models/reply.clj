(ns whereabts.models.reply
	(:refer-clojure :exclude [sort find])
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]
		[monger.query]))

(def replies-coll "replies")
(def reply-length 250)
(def reply-validation
	(validation-set
		(presence-of :message_id)
		(presence-of :user_id)
		(presence-of :replymessage)
		(presence-of :created-at)
		(length-of :replymessage :within (range 1 (+ reply-length 1)))))

(defn find-reply-by-id [id]
	(db-find-one-by-id replies-coll (obj-id id)))

(defn save-new-reply [reply]
	(if (valid? reply-validation reply)
	(db-insert replies-coll reply)
	(throw (IllegalArgumentException. "Invalid reply!"))))

(defn find-replies-by-message [message]
	(with-collection replies-coll
		(find {:message_id (:_id message)})
		(sort (sorted-map :created-at 1))))