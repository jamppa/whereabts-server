(ns whereabts.db-test-fixtures
	(:import [org.bson.types ObjectId]))

(def test-obj-a {:_id (ObjectId. "509d513f61395f0ebbd5e32a") :a "a" :b "b"})
(def test-obj-b {:_id (ObjectId. "509d513f61395f0ebbd5e32b") :a "aa" :b "b"})

(def test-user-a {:_id (ObjectId. "509d513f61395f0ebbd5e33a") :name "dsad" :email "fdsfs@dsad.fi" :password "secret" :type "email"})
(def test-user-b {:_id (ObjectId. "509d513f61395f0ebbd5e33b") :name "teppo" :email "teppo@test.fi" :password "secret" :type "email"})

(def test-usr-message-a {
		:_id (ObjectId. "509d513f61395f0ebbd5e34a")
		:user_id (:_id test-user-a)
		:message "I am test message A"
		:loc [50.0 50.0]
		:created-at 1364642721965})

(def test-usr-message-b {
	:_id (ObjectId. "509d513f61395f0ebbd5e35a")
	:user_id (:_id test-user-a)
	:message "I am test message B"
	:loc [50.0 50.0]
	:created-at 1364642721966})

(def test-usr-message-c {
	:_id (ObjectId. "509d513f61395f0ebbd5e36a")
	:user_id (:_id test-user-a)
	:message "I am test message C"
	:loc [51.12 51.12]
	:created-at 1364642721967})

(def test-message-a {
	:_id (ObjectId. "509d513f61395f0ebbd5e36a")
	:nick "Anonymous A"
	:message "Cool message"
	:title "Cool title"
	:loc [1.0 2.0]
	:created-at 1364642721968
	:views 10})

(def test-message-b {
	:_id (ObjectId. "509d513f61395f0ebbd5e36b")
	:nick "Anonymous B"
	:message "Cool message"
	:title "Cool title"
	:loc [5.0 5.0]
	:created-at 1364642721969
	:views 11})

(def test-message-c {
	:_id (ObjectId. "509d513f61395f0ebbd5e36c")
	:nick "Anonymous C"
	:message "Cool message"
	:title "Cool title"
	:loc [45.0 26.0]
	:created-at 1364642721970
	:views 12})

(def test-feedback-a {
	:_id (ObjectId. "509d513f61395f0ebbd5e37a")
	:message "This is some cool app!"
	:vote 10
	:created-at 1364642721970})