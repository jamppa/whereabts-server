(ns whereabts.models.anonymous-user-test
	(:use
		[whereabts.models.anonymous-user]
		[whereabts.models.util]
		[whereabts.db.user-test-fixtures]
		[whereabts.db-helper]
		[midje.sweet]))

(def new-anonymous-user {
	:user-uuid "550e8400-e29b-41d4-a716-446655440000" 
	:email "anonymous@whereabts.com" 
	:created-at (System/currentTimeMillis)})

(def new-anonymous-user-missing-uuid (dissoc new-anonymous-user :user-uuid))
(def new-anonymous-user-missing-email (dissoc new-anonymous-user :email))
(def new-anonymous-user-missing-creationtime (dissoc new-anonymous-user :created-at))
(def new-anonymous-user-too-short-uuid (merge new-anonymous-user {:user-uuid "123-abc"}))

(background (before :facts (setup-test-db)))

(fact "should find anonymous user by id"
	(find-anonymous-user-by-id "509d513f61395f0ebbd5e38a") => anonymous-user-a)

(fact "should save new anonymous user"
	(let [saved-anonymous-user (save-anonymous-user new-anonymous-user)]
		(find-anonymous-user-by-id (obj-id-as-str saved-anonymous-user)) => saved-anonymous-user))

(fact "should not save invalid anonymous user missing uuid"
	(save-anonymous-user new-anonymous-user-missing-uuid) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous user with too short uuid"
	(save-anonymous-user new-anonymous-user-too-short-uuid) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous user missing email"
	(save-anonymous-user new-anonymous-user-missing-email) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous user missing creation time"
	(save-anonymous-user new-anonymous-user-missing-creationtime) => (throws IllegalArgumentException))