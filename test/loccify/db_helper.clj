(ns loccify.db-helper
	(:require [monger.core :as monger] 
		[monger.collection :as monger-col])
	(:import [org.bson.types ObjectId]))

(def test-db-name "loccify_test")
(def test-obj-a {:_id (ObjectId. "509d513f61395f0ebbd5e32a") :a "a" :b "b"})
(def test-obj-b {:_id (ObjectId. "509d513f61395f0ebbd5e32b") :a "aa" :b "b"})
(def test-user-a {:_id (ObjectId. "509d513f61395f0ebbd5e33a") :name "dsad" :email "fdsfs@dsad.fi"})
(def test-loccage-a {:_id (ObjectId. "509d513f61395f0ebbd5e34a") 
						:user_id (ObjectId. "509d513f61395f0ebbd5e33a") :message "asd" :loc [50.011 50.011]})
(def test-loccage-b {:_id (ObjectId. "509d513f61395f0ebbd5e35a") 
						:user_id (ObjectId. "509d513f61395f0ebbd5e33a") :message "asd" :loc [50.012 50.012]})
(def test-loccage-c {:_id (ObjectId. "509d513f61395f0ebbd5e36a") 
						:user_id (ObjectId. "509d513f61395f0ebbd5e33a") :message "asd" :loc [51.12 51.12]})

(defn connect-to-test-db []
	(monger/connect!)
	(monger/set-db! (monger/get-db test-db-name)))

(defn create-geospatial-idxs [collections]
	(doseq [coll collections]
		(monger-col/ensure-index coll {:loc "2d"})))

(defn populate-test-db []
	(monger-col/remove "docs")
	(monger-col/remove "users")
	(monger-col/remove "loccages")
	(monger-col/insert "docs" test-obj-a)
	(monger-col/insert "docs" test-obj-b)
	(monger-col/insert "users" test-user-a)
	(monger-col/insert "loccages" test-loccage-a)
	(monger-col/insert "loccages" test-loccage-b)
	(monger-col/insert "loccages" test-loccage-c))

(defn setup-test-db []
	(connect-to-test-db)
	(populate-test-db)
	(create-geospatial-idxs ["loccages"]))