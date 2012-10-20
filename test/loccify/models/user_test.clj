(ns loccify.models.user-test
	(:use [midje.sweet])
	(:use [midje.util :only [testable-privates]])
	(:use [loccify.models.user]))

(testable-privates loccify.models.user create-query-details)

(def expected-query-details {:find-type :find-one :query {:key "val"} :collection "coll"})

(fact "should create correct query details map"
	(create-query-details :find-one {:key "val"} "coll") => expected-query-details)