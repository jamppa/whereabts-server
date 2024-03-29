(ns whereabts.db-test
	(:use 
		[whereabts.db]
		[whereabts.db-helper]
		[whereabts.db.test-fixtures]
		[midje.sweet])
	(:import [org.bson.types ObjectId]))

(background (before :facts (setup-test-db)))

(fact "should insert object to database with object-id"
	(let [inserted-obj (db-insert "docs" {:a "aaa" :b "bbb"})]
		(= inserted-obj (db-find-one-by-id "docs" (:_id inserted-obj))) => truthy))

(fact "should find one object by query details"
	(db-find-one "docs" {:_id (ObjectId. "509d513f61395f0ebbd5e32a")}) => test-obj-a)

(fact "should not find one object by query details if such object does not exist"
	(db-find-one "docs" {:key "and val that doesnt exist"}) => nil)

(fact "should update document by id with given modifier"
	(let [saved-doc (db-save "docs" (merge test-obj-a {:a "aaa"}))]
		(db-find-one "docs" {:_id (ObjectId. "509d513f61395f0ebbd5e32a")}) => (merge test-obj-a {:a "aaa"})))

(fact "should update document with oid as string"
	(db-save "docs" {:_id (ObjectId. "509d513f61395f0ebbd5e32a") :a "a" :b "b" :c "c"}) => (merge test-obj-a {:c "c"}))
