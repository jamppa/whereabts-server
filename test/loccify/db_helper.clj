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

(defn insert-test-objects [coll objs]
	(doseq [obj objs]
		(monger-col/insert coll obj)))

(defn clear-collections [colls]
	(doseq [coll colls]
		(monger-col/remove coll)))

(defn populate-test-db []
	(clear-collections ["docs" "users" "loccages"])
	(insert-test-objects "docs" [test-obj-a test-obj-b])
	(insert-test-objects "users" [test-user-a])
	(insert-test-objects "loccages" [test-loccage-a test-loccage-b test-loccage-c]))

(defn setup-test-db []
	(connect-to-test-db)
	(populate-test-db)
	(create-geospatial-idxs ["loccages"]))