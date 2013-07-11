(ns whereabts.db.test-fixtures
	(:import [org.bson.types ObjectId]))

(def test-obj-a {:_id (ObjectId. "509d513f61395f0ebbd5e32a") :a "a" :b "b"})
(def test-obj-b {:_id (ObjectId. "509d513f61395f0ebbd5e32b") :a "aa" :b "b"})

(def test-message-a {
	:_id (ObjectId. "509d513f61395f0ebbd5e36a")
	:user_id (ObjectId. "509d513f61395f0ebbd5e38a")
	:message "Cool message"
	:title "Cool title"
	:loc [1.0 2.0]
	:created-at 1364642721968
	:updated-at 1364642721968
	:expire-time 5000
	:expires-at (+ (System/currentTimeMillis) 5000)
	:views 10
	:deleted false
	:category_id 1})

(def test-message-b {
	:_id (ObjectId. "509d513f61395f0ebbd5e36b")
	:user_id (ObjectId. "509d513f61395f0ebbd5e38a")
	:message "Cool message"
	:title "Cool title"
	:loc [5.0 5.0]
	:created-at 1364642721969
	:updated-at 1364642721968
	:expire-time 5000
	:expires-at (+ (System/currentTimeMillis) 5000)
	:views 11
	:deleted false
	:category_id 2})

(def test-message-c {
	:_id (ObjectId. "509d513f61395f0ebbd5e36c")
	:user_id (ObjectId. "509d513f61395f0ebbd5e38b")
	:message "Cool message"
	:title "Cool title"
	:loc [45.0 26.0]
	:created-at 1364642721970
	:updated-at 1364642721968
	:expire-time 5000
	:expires-at (+ (System/currentTimeMillis) 5000)
	:views 12
	:deleted false
	:category_id 2})

(def test-message-d 
	(merge test-message-a {
		:_id (ObjectId. "509d513f61395f0ebbd5e36d")
		:loc [4.123 3.123]
		:deleted true}))

(def test-message-e 
	(merge test-message-a {
		:_id (ObjectId. "509d513f61395f0ebbd5e36e")
		:loc [4.123 3.123]
		:expires-at (- (System/currentTimeMillis) 5000)}))

(def test-feedback-a {
	:_id (ObjectId. "509d513f61395f0ebbd5e37a")
	:user_id (ObjectId. "509d513f61395f0ebbd5e38b")
	:message "This is some cool app!"
	:vote 10
	:created-at 1364642721970})