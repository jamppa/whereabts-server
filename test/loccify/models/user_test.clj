(ns loccify.models.user-test
	(:use [loccify.models.user])
	(:use [loccify.db-helper])
	(:use [midje.sweet])
	(:import [org.bson.types ObjectId]))

(background (before :facts (setup-test-db)))

(def invalid-user {:name "sefsdf"})
(def valid-user {:name "asdasd" :email "asds@asds.fi" :type "email" :password "secret"})

(fact "should find user by id"
	(find-user-by-id "509d513f61395f0ebbd5e33a") => test-user-a)

(fact "should return nil when trying to find user by id which doesnt exist"
	(find-user-by-id "509d513f61395f0ebbd5e56a") => nil)

(fact "should not save invalid user"
	(save-user invalid-user) => nil)

(fact "should save valid user"
	(let [saved-usr (save-user valid-user)]
		(find-user-by-id (.toString (:_id saved-usr))) => saved-usr))

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