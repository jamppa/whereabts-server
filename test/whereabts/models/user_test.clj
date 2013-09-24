(ns whereabts.models.user-test
	(:use
		[whereabts.models.user]
		[whereabts.models.util]
		[whereabts.db.user-test-fixtures]
		[whereabts.db-helper]
		[midje.sweet]))

(def new-user {
	:user-uuid "550e8400-e29b-41d4-a716-446655440000" 
	:email "anonymous@whereabts.com"
	:role "email"
	:created-at (System/currentTimeMillis)
	:last-seen-at (System/currentTimeMillis)
	:followers []
	:following []})

(def new-user-missing-uuid (dissoc new-user :user-uuid))
(def new-user-missing-email (dissoc new-user :email))
(def new-user-missing-creationtime (dissoc new-user :created-at))
(def new-user-too-short-uuid (merge new-user {:user-uuid "123-abc"}))
(def new-user-too-long-uuid (merge new-user {:user-uuid (clojure.string/join "" (repeat 37 "s"))}))
(def new-user-wrong-creationtime (merge new-user {:created-at "1.5.2013"}))

(background (before :facts (setup-test-db)))

(fact "should find user by id"
	(find-user-by-id "509d513f61395f0ebbd5e38a") => test-user-a)

(fact "should save new user"
	(let [saved-anonymous-user (save-new-user new-user)]
		(find-user-by-id (obj-id-as-str saved-anonymous-user)) => saved-anonymous-user))

(fact "should not save invalid user missing uuid"
	(save-new-user new-user-missing-uuid) => (throws IllegalArgumentException))

(fact "should not save invalid user with too short uuid"
	(save-new-user new-user-too-short-uuid) => (throws IllegalArgumentException))

(fact "should not save invalid user with too long uuid"
	(save-new-user new-user-too-long-uuid) => (throws IllegalArgumentException))

(fact "should not save invalid user missing email"
	(save-new-user new-user-missing-email) => (throws IllegalArgumentException))

(fact "should not save invalid user missing creation time"
	(save-new-user new-user-missing-creationtime) => (throws IllegalArgumentException))

(fact "should find user by uuid and email"
	(find-user (by-uuid-and-email 
		"550e8400-e29b-41d4-a716-446655440000" "anonymous@whereabts.com")) => test-user-a)

(fact "should find user by email"
	(find-user (by-email "user@test.com")) => test-user-b)

(fact "should not find user by email when such does not exist"
	(find-user (by-email "non@existing.com")) => nil)

(fact "should not find user with uuid with one that does not exist"
	(find-user 
		(by-uuid-and-email "550e8400-e29b-41d4-a716-446655441234" "anonymous@whereabts.com")) => nil)

(fact "should find and update user"
	(let [found-user (find-user-by-id "509d513f61395f0ebbd5e38a")
		  updated-user (update-user (merge found-user {:last-seen-at 1364642721971}))]
		  (find-user-by-id "509d513f61395f0ebbd5e38a") => (merge test-user-a {:last-seen-at 1364642721971})))

(fact "should throw exception when trying to update invalid anonymous user"
	(let [found-user (find-user-by-id "509d513f61395f0ebbd5e38a")]
		  (update-user (merge found-user {:user-uuid "123-abc"})) => (throws IllegalArgumentException)))

(fact "should delete user by user oid"
	(delete-user-by-id (:_id test-user-a))
	(find-user-by-id (.toString (:_id test-user-a))) => nil)
