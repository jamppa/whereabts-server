(ns whereabts.models.feedback-test
	(:use 
		[midje.sweet]
		[whereabts.models.feedback]
		[whereabts.models.util]
		[whereabts.db-helper]
		[whereabts.db.test-fixtures]
		[whereabts.db.user-test-fixtures]))

(background (before :facts (setup-test-db)))

(def new-feedback {
	:user_id (:_id anonymous-user-a)
	:message "message of new feedback"
	:vote 0
	:created-at (System/currentTimeMillis)})

(def feedback-too-long-message 
	(merge new-feedback 
		{:message (clojure.string/join "" (repeat (+ feedback-length 1) "s"))}))

(def feedback-message-missing (dissoc new-feedback :message))
(def feedback-vote-missing (dissoc new-feedback :vote))
(def feedback-creation-ts-missing (dissoc new-feedback :created-at))
(def feedback-user-missing (dissoc new-feedback :user_id))

(fact "should find a feedback by its id"
	(find-feedback-by-id "509d513f61395f0ebbd5e37a") => test-feedback-a)

(fact "should find nil when trying to find nonexisting feedback by id"
	(find-feedback-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should save new feedback and return it"
	(let [saved-feedback (save-feedback new-feedback)]
		(find-feedback-by-id (obj-id-as-str saved-feedback)) => saved-feedback))

(fact "should not save invalid feedback with too long message"
	(save-feedback feedback-too-long-message) => (throws IllegalArgumentException))

(fact "should not save invalid feedback with message missing"
	(save-feedback feedback-message-missing) => (throws IllegalArgumentException))

(fact "should not save invalid feedback with vote count missing"
	(save-feedback feedback-vote-missing) => (throws IllegalArgumentException))

(fact "should not save invalid feedback with missing creation timestamp"
	(save-feedback feedback-creation-ts-missing) => (throws IllegalArgumentException))

(fact "should not save invalid feedback without user id"
	(save-feedback feedback-user-missing) => (throws IllegalArgumentException))