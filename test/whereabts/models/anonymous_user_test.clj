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
	:role "anonymous"
	:created-at (System/currentTimeMillis)
	:last-seen-at (System/currentTimeMillis)})

(def new-anonymous-user-missing-uuid (dissoc new-anonymous-user :user-uuid))
(def new-anonymous-user-missing-email (dissoc new-anonymous-user :email))
(def new-anonymous-user-missing-creationtime (dissoc new-anonymous-user :created-at))
(def new-anonymous-user-too-short-uuid (merge new-anonymous-user {:user-uuid "123-abc"}))
(def new-anonymous-user-too-long-uuid (merge new-anonymous-user {:user-uuid (clojure.string/join "" (repeat 37 "s"))}))
(def new-anonymous-user-wrong-email (merge new-anonymous-user {:email "iam@wrong.com"}))
(def new-anonymous-user-wrong-creationtime (merge new-anonymous-user {:created-at "1.5.2013"}))

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

(fact "should not save invalid anonymous user with too long uuid"
	(save-anonymous-user new-anonymous-user-too-long-uuid) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous user missing email"
	(save-anonymous-user new-anonymous-user-missing-email) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous user with wrong email"
	(save-anonymous-user new-anonymous-user-wrong-email) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous user missing creation time"
	(save-anonymous-user new-anonymous-user-missing-creationtime) => (throws IllegalArgumentException))

(fact "should not save invalid anonymous user with creation time as a string"
	(save-anonymous-user new-anonymous-user-wrong-creationtime) => (throws IllegalArgumentException))

(fact "should find anonymous user by uuid and email"
	(find-anonymous-user 
		(by-uuid-and-email "550e8400-e29b-41d4-a716-446655440000" "anonymous@whereabts.com")) => anonymous-user-a)

(fact "should not find anonymous user with uuid with one that does not exist"
	(find-anonymous-user 
		(by-uuid-and-email "550e8400-e29b-41d4-a716-446655441234" "anonymous@whereabts.com")) => nil)

(fact "should find and update anonymous user"
	(let [found-user (find-anonymous-user-by-id "509d513f61395f0ebbd5e38a")
		  updated-user (update-anonymous-user (merge found-user {:last-seen-at 1364642721971}))]
		  (find-anonymous-user-by-id "509d513f61395f0ebbd5e38a") => (merge anonymous-user-a {:last-seen-at 1364642721971})))

(fact "should throw exception when trying to update invalid anonymous user"
	(let [found-user (find-anonymous-user-by-id "509d513f61395f0ebbd5e38a")]
		  (update-anonymous-user (merge found-user {:user-uuid "123-abc"})) => (throws IllegalArgumentException)))