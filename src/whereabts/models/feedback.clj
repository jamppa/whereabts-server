(ns whereabts.models.feedback
	(:use 
		[whereabts.models.util]
		[whereabts.db]
		[validateur.validation]))

(def feedback-coll "feedbacks")
(def feedback-length 500)
(def feedback-validations
	(validation-set
		(presence-of :user_id)
		(presence-of :vote)
		(presence-of :created-at)
		(numericality-of :vote :only-integer true :equal-to 0)
		(numericality-of :created-at :only-integer true)
		(length-of :message :within (range 1 501))))

(defn find-feedback-by-id [id]
	(db-find-one feedback-coll {:_id (obj-id id)}))

(defn save-feedback [feedback]
	(if (valid? feedback-validations feedback)
		(db-insert feedback-coll feedback)
		(throw (IllegalArgumentException. "Invalid feedback!"))))
	