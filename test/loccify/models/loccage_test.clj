(ns loccify.models.loccage-test
	(:use [midje.sweet])
	(:use [loccify.models.loccage])
	(:use [loccify.db])
	(:import [org.bson.types ObjectId]))

(defn create-valid-loccage []
	{:user_id (ObjectId.) :message "dägä"})

(defn create-invalid-loccage []
	{:message "dägä"})

(def valid-loccage (create-valid-loccage))
(def invalid-loccage (create-invalid-loccage))

(fact "should save valid loccage to db"
	(save-loccage valid-loccage) => valid-loccage
	(provided (db-insert "loccages" valid-loccage) => valid-loccage :times 1))

(fact "should not save invalid loccage to db"
	(save-loccage invalid-loccage) => nil
	(provided (db-insert "loccages" invalid-loccage) => nil :times 0))