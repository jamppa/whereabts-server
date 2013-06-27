(ns whereabts.db.category-test-fixtures
	(:import [org.bson.types ObjectId]))

(def test-category-a 
	{:_id (ObjectId. "509d513f61395f0ebbd5e50a") :key "sports_and_activities"})