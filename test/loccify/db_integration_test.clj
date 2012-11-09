(ns loccify.db-integration-test
	(:use [loccify.db])
	(:use [midje.sweet])
	(:use [loccify.db-helper])
	(:import [org.bson.types ObjectId]))

(background (before :facts (setup-test-db)))

(fact "should find one object from database by query-details"
	(db-find 
		{:find-type :find-one :collection "docs" :query {:_id (ObjectId. "509d513f61395f0ebbd5e32a")}}) => test-obj-a)
