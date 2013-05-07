(ns whereabts.models.anonymous-user-test
	(:use
		[whereabts.models.anonymous-user]
		[whereabts.models.util]
		[whereabts.db.user-test-fixtures]
		[whereabts.db-helper]
		[midje.sweet]))

(def new-anonymous-user {
	:user_uuid "123-abc" 
	:email "anonymous@whereabts.com" 
	:created-at (System/currentTimeMillis)})

(background (before :facts (setup-test-db)))

(fact "should find anonymous user by id"
	(find-anonymous-user-by-id "509d513f61395f0ebbd5e38a") => anonymous-user-a)

(fact "should save new anonymous user"
	(let [saved-anonymous-user (save-anonymous-user new-anonymous-user)]
		(find-anonymous-user-by-id (obj-id-as-str saved-anonymous-user)) => saved-anonymous-user))