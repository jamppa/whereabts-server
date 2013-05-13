(ns whereabts.db.user-test-fixtures
	(:import [org.bson.types ObjectId]))

(def anonymous-user-a {
	:_id (ObjectId. "509d513f61395f0ebbd5e38a")
	:user-uuid "550e8400-e29b-41d4-a716-446655440000"
	:email "anonymous@whereabts.com"
	:created-at 1364642721970
	:last-seen-at 1364642721970})

(def anonymous-user-b 
	(merge anonymous-user-a {
		:_id (ObjectId. "509d513f61395f0ebbd5e38b")
		:user-uuid "550e8400-e29b-41d4-a716-446655440001"}))