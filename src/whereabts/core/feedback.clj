(ns whereabts.core.feedback
    (:use
        [whereabts.models.feedback]
        [whereabts.models.util]))

(defn save-new-feedback [new-feedback]
    (let [the-feedback (created-now new-feedback)]
        (save-feedback the-feedback)))