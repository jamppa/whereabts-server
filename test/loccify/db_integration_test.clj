(ns loccify.db-integration-test
	(:use [loccify.db])	
	(:use [midje.sweet])
	(:use [loccify.db-helper])
	(:import [org.bson.types ObjectId]))

(defn- query-details [type coll query]
	{:find-type type :collection coll :query query})

(background (before :facts (setup-test-db)))

(fact "should find one object from database by query-details"
	(db-find 
		(query-details :find-one "docs" {:_id (ObjectId. "509d513f61395f0ebbd5e32a")})) => test-obj-a)
