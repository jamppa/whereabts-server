(ns whereabts.core.feedback
    (:use
        [whereabts.models.feedback]
        [whereabts.models.util])
    (:import [whereabts.exception WhereabtsResourceNotFoundException]))

(defn save-new-feedback [new-feedback]
    (let [the-feedback (created-now new-feedback)]
        (save-feedback the-feedback)))

(defn find-feedback [id]
    (let [found-feedback (find-feedback-by-id id)]
        (if (nil? found-feedback)
            (throw (WhereabtsResourceNotFoundException.))
            found-feedback)))