(ns whereabts.models.reply-test
	(:use
		[midje.sweet]
		[whereabts.models.reply]
		[whereabts.models.util]
		[whereabts.db-helper]
		[whereabts.db.reply-test-fixtures]))

(def long-reply (clojure.string/join "" (repeat (+ reply-length 1) "s")))
(def new-reply 
	(merge (dissoc test-reply-a :_id) {:replymessage "Yyyeaaahhh!"}))

(def reply-missing-msg-id (dissoc new-reply :message_id))
(def reply-missing-user (dissoc new-reply :user_id))
(def reply-missing-nick (dissoc new-reply :nick))
(def reply-missing-msg (dissoc new-reply :replymessage))
(def reply-missing-timestamp (dissoc new-reply :created-at))
(def reply-too-long (merge new-reply {:replymessage long-reply}))

(background (before :facts (setup-test-db)))

(fact "should find reply by its id"
	(find-reply-by-id "509d513f61395f0ebbd5e40a") => test-reply-a)

(fact "should not find reply by its id when one does not exist"
	(find-reply-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should save new reply message"
	(let [saved (save-new-reply new-reply)]
		(find-reply-by-id (obj-id-as-str saved)) => saved))

(fact "should not save invalid reply message missing message id"
	(save-new-reply reply-missing-msg-id) => (throws IllegalArgumentException))

(fact "should not save invalid reply message missing user id"
	(save-new-reply reply-missing-user) => (throws IllegalArgumentException))

(fact "should not save invalid reply message missing nick"
	(save-new-reply reply-missing-nick) => (throws IllegalArgumentException))

(fact "should not save invalid reply message missing replymessage"
	(save-new-reply reply-missing-msg) => (throws IllegalArgumentException))

(fact "should not save invalid reply message missing creation time"
	(save-new-reply reply-missing-timestamp) => (throws IllegalArgumentException))

(fact "should not save invalid reply message with too long message"
	(save-new-reply reply-too-long) => (throws IllegalArgumentException))