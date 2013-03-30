(ns loccify.models.user-test
	(:use 
		[loccify.models.user]
		[loccify.db-helper]
		[loccify.db-test-fixtures]
		[midje.sweet])
	(:import [org.bson.types ObjectId]))

(background (before :facts (setup-test-db)))

(def invalid-user {:name "sefsdf"})
(def valid-user {:name "asdasd" :email "asds@asds.fi" :password "secret"})

(fact "should find user by id"
	(find-user-by-id "509d513f61395f0ebbd5e33a") => test-user-a)

(fact "should return nil when trying to find user by id which doesnt exist"
	(find-user-by-id "509d513f61395f0ebbd5e56a") => nil)

(fact "should not save invalid user"
	(save-user invalid-user) => nil)

(fact "should save valid user"
	(let [saved-usr (save-user valid-user)]
		(find-user-by-id (.toString (:_id saved-usr))) => truthy))

(fact "should find user by email and password"
	(find-user-by-email-and-pass "teppo@test.fi" "secret") => test-user-b)

(fact "should not find user by email and password if one does not exist"
	(find-user-by-email-and-pass "non@existing.fi" "blaaah") => nil)

(fact "should not find user by email and password if password is wrong"
	(find-user-by-email-and-pass "teppo@test.fi" "wrong") => nil)

(fact "should find user by name when one exists"
	(find-user-by-name "teppo") => test-user-b)

(fact "should not find user by name when one does not exist"
	(find-user-by-name "seppo") => nil)

(fact "should find user by email if one exists"
	(find-user-by-email "teppo@test.fi") => test-user-b)

(fact "should not find user by email if one does not exist"
	(find-user-by-email "some@odd.fi") => nil)