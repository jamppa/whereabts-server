(ns whereabts.models.util-test
	(:use 
		[whereabts.models.util]
		[midje.sweet])
	(:import [org.bson.types ObjectId]))

(def obj {})
(def obj-with-id {:_id (ObjectId. "507f191e810c19729de860ea") :other_id (ObjectId. "507f191e810c19729de860eb")})

(fact "should merge object with current timestamp"
	(contains? (created-now obj) :created-at) => truthy)

(fact "should merge object with object-id"
	(contains? (with-obj-id obj) :_id) => truthy)

(fact "should instantiate bson object-id with given hex-string"
	(.toString (obj-id "507f191e810c19729de860ea")) => "507f191e810c19729de860ea")

(fact "should get object-id as string from object"
	(obj-id-as-str obj-with-id) => "507f191e810c19729de860ea")
	
(fact "should stamp object with last-seen-at with current timestamp"
	(contains? (last-seen-now obj) :last-seen-at) => truthy)

(fact "should give specified object-id-field as string"
	(id-as-str obj-with-id :other_id) => "507f191e810c19729de860eb")

(fact "should update object by merging updated-at timestamp with current time"
	(keys (updated-now {})) => [:updated-at])

(def objs [
	{:k (ObjectId. "507f191e810c19729de860ea")} 
	{:k (ObjectId. "507f191e810c19729de860eb")}
	{:k (ObjectId. "507f191e810c19729de860ea")}
	{:other "stuff"}])
(fact "should extract ObjectIds distinctively from objects"
	(oids-as-str objs :k) => ["507f191e810c19729de860ea" "507f191e810c19729de860eb"])

(def objs (subvec objs 0 3))
(fact "should find first item from collection satistying predicate function"
	(find-first #(= (.toString (:k %)) "507f191e810c19729de860eb") objs) => (get objs 1))