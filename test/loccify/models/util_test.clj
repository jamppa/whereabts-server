(ns loccify.models.util-test
	(:use 
		[loccify.models.util]
		[midje.sweet])
	(:import [org.bson.types ObjectId]))

(def obj {})

(fact "should merge object with current timestamp"
	(contains? (created-now obj) :created-at) => truthy)

(fact "should merge object with object-id"
	(contains? (with-obj-id obj) :_id) => truthy)

(fact "should instantiate bson object-id with given hex-string"
	(.toString (obj-id "507f191e810c19729de860ea")) => "507f191e810c19729de860ea")

(fact "should give object-id as string"
	(obj-id-as-str (ObjectId. "507f191e810c19729de860ea")) => "507f191e810c19729de860ea")