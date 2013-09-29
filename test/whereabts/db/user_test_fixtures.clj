(ns whereabts.db.user-test-fixtures
	(:import [org.bson.types ObjectId]))

(def profile-id-a (ObjectId. "509d513f61395f0ebbd5e58a"))
(def profile-id-b (ObjectId. "509d513f61395f0ebbd5e58b"))
(def profile-id-c (ObjectId.))

(def test-user-a {
	:_id (ObjectId. "509d513f61395f0ebbd5e38a")
	:user-uuid "550e8400-e29b-41d4-a716-446655440000"
	:email "anonymous@whereabts.com"
	:role "email"
	:created-at 1364642721970
	:last-seen-at 1364642721970
	:profile_id profile-id-a
	:following []
	:followers []})

(def test-user-b 
	(merge test-user-a {
		:_id (ObjectId. "509d513f61395f0ebbd5e38b")
		:user-uuid "550e8400-e29b-41d4-a716-446655440001"
		:email "user@test.com"
		:profile_id profile-id-b}))

(def test-profile-a
	{
		:_id profile-id-a
		:user_id (:_id test-user-a)
		:nick "testman"
		:country "fi"
		:description "testman from testland"
		:photo ""})

(def test-profile-b
	(merge test-profile-a 
		{
			:nick "seppo"
			:_id profile-id-b
			:user_id (:_id test-user-b)}))

(def test-profile-c
	(merge test-profile-a
	{
		:nick "man of the year"
		:_id profile-id-c
		:user_id (ObjectId.)
		}))

(def test-profiles [test-profile-a test-profile-b test-profile-c])