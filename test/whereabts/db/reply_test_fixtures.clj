(ns whereabts.db.reply-test-fixtures
	(:use
		[whereabts.db.test-fixtures]
		[whereabts.db.user-test-fixtures])
	(:import [org.bson.types ObjectId]))

(def test-reply-a
	{
		:_id (ObjectId. "509d513f61395f0ebbd5e40a")
		:message_id (:_id test-message-a)
		:user_id (:_id anonymous-user-a)
		:nick "jamppa"
		:replymessage "Cool!"
		:created-at 1364642721970})

(def test-reply-b 
	(merge test-reply-a 
		{:_id (ObjectId. "509d513f61395f0ebbd5e40b") 
		 :replymessage "Super Cool!"
		 :created-at 1364642721990}))