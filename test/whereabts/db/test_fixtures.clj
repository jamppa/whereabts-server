(ns whereabts.db.test-fixtures
	(:import [org.bson.types ObjectId]))

(def test-obj-a {:_id (ObjectId. "509d513f61395f0ebbd5e32a") :a "a" :b "b"})
(def test-obj-b {:_id (ObjectId. "509d513f61395f0ebbd5e32b") :a "aa" :b "b"})

(def test-message-a {
	:_id (ObjectId. "509d513f61395f0ebbd5e36a")
	:user_id (ObjectId. "509d513f61395f0ebbd5e38a")
	:message "Cool message"
	:loc [1.0 2.0]
	:created-at (- (System/currentTimeMillis) (* 1000 60 60 11))
	:updated-at (System/currentTimeMillis)
	:views 10
	:likes []
	:category "happenings"
	:photo false})

(def test-message-b {
	:_id (ObjectId. "509d513f61395f0ebbd5e36b")
	:user_id (ObjectId. "509d513f61395f0ebbd5e38a")
	:message "Cool message"
	:loc [5.0 5.0]
	:created-at (- (System/currentTimeMillis) (* 1000 60 60 6))
	:updated-at (System/currentTimeMillis)
	:views 11
	:likes ["509d513f61395f0ebbd5e38a"]
	:category "happenings"
	:photo true})

(def test-message-c {
	:_id (ObjectId. "509d513f61395f0ebbd5e36c")
	:user_id (ObjectId. "509d513f61395f0ebbd5e38b")
	:message "Cool message"
	:loc [45.0 26.0]
	:created-at (- (System/currentTimeMillis) 86400000)
	:updated-at (System/currentTimeMillis)
	:views 12
	:likes ["509d513f61395f0ebbd5e38a"]
	:category "happenings"
	:photo false})

(def test-message-d 
	(merge test-message-c {
		:_id (ObjectId. "509d513f61395f0ebbd5e36d")
		:loc [4.123 3.123]}))

(def test-message-e 
	(merge test-message-c {
		:_id (ObjectId. "509d513f61395f0ebbd5e36e")
		:loc [4.123 3.123]}))

(def test-feedback-a {
	:_id (ObjectId. "509d513f61395f0ebbd5e37a")
	:user_id (ObjectId. "509d513f61395f0ebbd5e38b")
	:message "This is some cool app!"
	:vote 10
	:created-at 1364642721970})