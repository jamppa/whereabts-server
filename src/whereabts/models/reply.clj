(ns whereabts.models.reply
	(:use
		[whereabts.db]
		[whereabts.models.util]))

(def replies-coll "replies")

(defn find-reply-by-id [id]
	(db-find-one-by-id replies-coll (obj-id id)))