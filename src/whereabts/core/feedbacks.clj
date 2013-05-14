(ns whereabts.core.feedbacks
    (:use
        [whereabts.models.feedback]
        [whereabts.models.util])
    (:import [whereabts.exception WhereabtsResourceNotFoundException]))

(defn- with-user [feedback user]
    (merge feedback {:user_id (:_id user)}))

(defn save-new-feedback [new-feedback user]
    (let [feedback (created-now new-feedback)]
        (save-feedback (with-user feedback user))))

(defn find-feedback [id]
    (let [found-feedback (find-feedback-by-id id)]
        (if (nil? found-feedback)
            (throw (WhereabtsResourceNotFoundException.))
            found-feedback)))