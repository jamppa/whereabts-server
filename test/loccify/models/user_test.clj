(ns loccify.models.user-test
	(:use [loccify.models.user])
	(:use [loccify.db-helper])
	(:use [midje.sweet])
	(:import [org.bson.types ObjectId]))

(background (before :facts (setup-test-db)))

(def invalid-user {:name "sefsdf"})
(def valid-user {:name "asdasd" :email "asds@asds.fi"})

(fact "should find user by id"
	(find-user-by-id "509d513f61395f0ebbd5e33a") => test-user-a)

(fact "should not save invalid user"
	(save-user invalid-user) => nil)

(fact "should save valid user"
	(let [saved-usr (save-user valid-user)]
		(find-user-by-id (.toString (:_id saved-usr))) => saved-usr))