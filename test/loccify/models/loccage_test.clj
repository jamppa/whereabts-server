(ns loccify.models.loccage-test
	(:use [midje.sweet])
	(:use [loccify.models.loccage])
	(:use [loccify.db])
	(:import [org.bson.types ObjectId]))

(defn create-valid-loccage []
	{:user_id (ObjectId.) :message "dägä"})

(def valid-loccage (create-valid-loccage))

(fact "should save valid loccage to db"
	(save-loccage valid-loccage) => valid-loccage
	(provided (db-insert "loccages" valid-loccage) => valid-loccage :times 1))
