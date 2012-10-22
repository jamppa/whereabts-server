(ns loccify.models.user-test
	(:use [midje.sweet])
	(:use [midje.util :only [testable-privates]])
	(:use [loccify.models.user])
	(:use [loccify.db]))

(testable-privates loccify.models.user create-query-details)

(def expected-query-details {:find-type :find-one :query {:key "val"} :collection "coll"})
(def valid-user {:name "teppo" :email "teppo@test.fi"})
(def invalid-user {:name "teppo"})

(fact "should create correct query details map"
	(create-query-details :find-one {:key "val"} "coll") => expected-query-details)

(fact "should save valid user to db and return it"
	(save-user valid-user) => valid-user
	(provided (db-insert "users" valid-user) => valid-user :times 1))

(fact "should not save invalid user to db"
	(save-user invalid-user) => nil
	(provided (db-insert "users" invalid-user) => nil :times 0))