(ns whereabts.db-test
	(:use 
		[whereabts.db]
		[whereabts.db-helper]
		[whereabts.db-test-fixtures]
		[midje.sweet])
	(:import [org.bson.types ObjectId]))

(defn- query-details [type coll query]
	{:find-type type :collection coll :query query})

(background (before :facts (setup-test-db)))

(fact "should find one object from database by query-details"
	(db-find (query-details :find-one "docs" {:_id (ObjectId. "509d513f61395f0ebbd5e32a")})) => test-obj-a)

(fact "should find many objects from database by query-details"
	(db-find (query-details :find-many "docs" {:b "b"})) => [test-obj-a test-obj-b])

(fact "should insert object to database with object-id"
	(let [inserted-obj (db-insert "docs" {:a "aaa" :b "bbb"})]
		(= inserted-obj (db-find (query-details :find-one "docs" {:_id (:_id inserted-obj)}))) => truthy))

(fact "should find one object by query details"
	(db-find-one "docs" {:_id (ObjectId. "509d513f61395f0ebbd5e32a")}) => test-obj-a)
