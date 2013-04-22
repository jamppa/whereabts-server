(ns whereabts.models.feedback-test
	(:use 
		[midje.sweet]
		[whereabts.models.feedback]
		[whereabts.models.util]
		[whereabts.db-helper]
		[whereabts.db-test-fixtures]))

(background (before :facts (setup-test-db)))

(def new-feedback {
	:message "message of new feedback"
	:vote 0
	:created-at (System/currentTimeMillis)})

(fact "should find a feedback by its id"
	(find-feedback-by-id "509d513f61395f0ebbd5e37a") => test-feedback-a)

(fact "should find nil when trying to find nonexisting feedback by id"
	(find-feedback-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should save new feedback and return it"
	(let [saved-feedback (save-feedback new-feedback)]
		(find-feedback-by-id (obj-id-as-str saved-feedback)) => saved-feedback))