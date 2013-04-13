(ns whereabts.models.util-test
	(:use 
		[whereabts.models.util]
		[midje.sweet])
	(:import [org.bson.types ObjectId]))

(def obj {})
(def obj-with-id {:_id (ObjectId. "507f191e810c19729de860ea")})
(def obj-with-title {:title "This is message title" :message "This is the message"})
(def obj-without-title {:title "" :message "This is the message"})

(fact "should merge object with current timestamp"
	(contains? (created-now obj) :created-at) => truthy)

(fact "should merge object with object-id"
	(contains? (with-obj-id obj) :_id) => truthy)

(fact "should instantiate bson object-id with given hex-string"
	(.toString (obj-id "507f191e810c19729de860ea")) => "507f191e810c19729de860ea")

(fact "should get object-id as string from object"
	(obj-id-as-str obj-with-id) => "507f191e810c19729de860ea")

(fact "should get short message from message object using title if available"
	(short-message obj-with-title) => (:title obj-with-title))

(fact "should get short message from message object using actual message when title is empty"
	(short-message obj-without-title) => (:message obj-without-title))

(fact "should ellipsize string using first 4 words out of the string"
	(ellipsize-str "This is some cool string you know") => "This is some cool ...")