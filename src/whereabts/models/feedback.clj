(ns whereabts.models.feedback
	(:use 
		[whereabts.models.util]
		[whereabts.db]))

(def feedback-coll "feedbacks")

(defn find-feedback-by-id [id]
	(db-find-one feedback-coll {:_id (obj-id id)}))

(defn save-feedback [feedback]
	(db-insert feedback-coll feedback))