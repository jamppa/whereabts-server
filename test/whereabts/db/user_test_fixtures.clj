(ns whereabts.db.user-test-fixtures
	(:import [org.bson.types ObjectId]))

(def test-user-a {
	:_id (ObjectId. "509d513f61395f0ebbd5e38a")
	:user-uuid "550e8400-e29b-41d4-a716-446655440000"
	:email "anonymous@whereabts.com"
	:role "email"
	:created-at 1364642721970
	:last-seen-at 1364642721970})

(def test-user-b 
	(merge test-user-a {
		:_id (ObjectId. "509d513f61395f0ebbd5e38b")
		:user-uuid "550e8400-e29b-41d4-a716-446655440001"
		:email "user@test.com"}))