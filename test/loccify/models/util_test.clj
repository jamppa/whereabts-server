(ns loccify.models.util-test
	(:use 
		[loccify.models.util]
		[midje.sweet]))

(def obj {})

(fact "should mark object with current timestamp"
	(contains? (created-now obj) :created-at) => truthy)