(ns whereabts.db.user-test-fixtures
	(:import [org.bson.types ObjectId]))

(def anonymous-user-a {
	:_id (ObjectId. "509d513f61395f0ebbd5e38a")
	:user_uuid "aaa-bbb-ccc"
	:email "anonymous@whereabts.com"
	:created-at 1364642721970})