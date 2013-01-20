(ns loccify.models.util-test
	(:use 
		[loccify.models.util]
		[midje.sweet]))

(def obj {})

(fact "should merge object with current timestamp"
	(contains? (created-now obj) :created-at) => truthy)

(fact "should merge object with object-id"
	(contains? (with-obj-id obj) :_id) => truthy)