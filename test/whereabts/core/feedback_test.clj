(ns whereabts.core.feedback-test
	(:use 
		[whereabts.core.feedback]
		[whereabts.models.feedback]
		[whereabts.models.util]
		[midje.sweet])
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(def new-feedback {})
(def feedback-created-now {:created-at 123123})

(fact "should save new feedback with current timestamp"
	(save-new-feedback new-feedback) => feedback-created-now
	(provided (created-now new-feedback) => feedback-created-now :times 1)
	(provided (save-feedback feedback-created-now) => feedback-created-now :times 1))

(fact "should find feedback with id"
	(find-feedback "123abc") => new-feedback
	(provided (find-feedback-by-id "123abc") => new-feedback :times 1))

(fact "should throw exception when trying to find feedback with id that does not exist"
	(find-feedback "666abc") => (throws WhereabtsResourceNotFoundException)
	(provided (find-feedback-by-id "666abc") => nil :times 1))