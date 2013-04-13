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

(fact "should get short message from message object using actual message ellipsized when title is empty"
	(short-message obj-without-title) => "This is the message..."
	(provided (ellipsize-str-max-len "This is the message" 30) => "This is the message..." :times 1))

(fact "should ellipsize string using first four words out of the string"
	(ellipsize-str-max-words "This is some cool string you know" 4) => "This is some cool ...")

(fact "should ellipsize string using first available words when it contains only couple of words"
	(ellipsize-str-max-words "This is" 4) => "This is")
	
(fact "should ellipsize string using given maximum length"
	(ellipsize-str-max-len "This is some very looong string" 10) => "This is so...")

(fact "should not ellipsize if string fits to fiven length"
	(ellipsize-str-max-len "This is a message" 100) => "This is a message")