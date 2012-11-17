(ns loccify.models.loccage-test
	(:use [loccify.models.loccage])
	(:use [loccify.db-helper])
	(:use [midje.sweet])
	(:import [org.bson.types ObjectId]))

(def valid-loccage {:user_id (ObjectId.) :message "msg" :loc [12.123 12.123]})
(def invalid-loccage {:message "fdsfd" :loc [1.123 3.323]}) ;user missing

(background (before :facts (setup-test-db)))

(fact "should find loccage by id"
	(find-loccage-by-id "509d513f61395f0ebbd5e34a") => test-loccage-a)

(fact "should return nil when trying to find loccage by id which doesnt exist"
	(find-loccage-by-id "509d513f61395f0ebbd5e34f") => nil)

(fact "should save valid loccage"
	(let [saved-loccage (save-loccage valid-loccage)]
		(find-loccage-by-id (.toString (:_id saved-loccage))) => saved-loccage))