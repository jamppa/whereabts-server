(ns loccify.models.loccage-test
	(:use 
		[loccify.models.loccage]
		[loccify.db-helper]
		[loccify.util.geo]
		[loccify.models.util]
		[loccify.db-test-fixtures]
		[midje.sweet])
	(:import [org.bson.types ObjectId]))

(def valid-msg {:user_id (ObjectId.) :message "msg" :loc [12.123 12.123]})
(def invalid-msg {:message "fdsfd" :loc [1.123 3.323]})

(background (before :facts (setup-test-db)))

(fact "should find loccage by id"
	(find-loccage-by-id "509d513f61395f0ebbd5e34a") => test-message-a)

(fact "should return nil when trying to find loccage by id which doesnt exist"
	(find-loccage-by-id "509d513f61395f0ebbd5e34f") => nil)

(fact "should save valid message"
	(let [saved-message (save-message valid-msg)]
		(find-loccage-by-id (obj-id-as-str saved-message)) => truthy))

(fact "should not save invalid message"
	(save-message invalid-msg) => nil)

(fact "should find messages near by location and distance of 2500 meters"
	(find-messages-near (location 50.0 50.0 2500)) => [test-message-b test-message-a])

(fact "should not find messages when there isnt any near by"
	(find-messages-near (location 25.0 25.0 500)) => [])

(fact "should find messages inside bounding box"
	(find-messages-by-bbox (bounding-box [0 0] [50 50])) => [test-message-b test-message-a])

(fact "sould not find any messages inside bounding box when there is no any"
	(find-messages-by-bbox (bounding-box [100 100] [125 125])) => [])