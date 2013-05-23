(ns whereabts.models.reply
	(:use
		[whereabts.db]
		[whereabts.models.util]
		[validateur.validation]))

(def replies-coll "replies")
(def reply-validation
	(validation-set
		(presence-of :message_id)))

(defn find-reply-by-id [id]
	(db-find-one-by-id replies-coll (obj-id id)))

(defn save-new-reply [reply]
	(if (valid? reply-validation reply)
	(db-insert replies-coll reply)
	(throw (IllegalArgumentException. "Invalid reply!"))))