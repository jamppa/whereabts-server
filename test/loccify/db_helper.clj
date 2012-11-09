(ns loccify.db-helper
	(:require [monger.core :as monger] 
		[monger.collection :as monger-col])
	(:import [org.bson.types ObjectId]))

(def test-db-name "loccify_test")
(def test-obj-a {:_id (ObjectId. "509d513f61395f0ebbd5e32a") :a "a" :b "b"})
(def test-obj-b {:_id (ObjectId. "509d513f61395f0ebbd5e32b") :a "aa" :b "bb"})

(defn connect-to-test-db []
	(monger/connect!)
	(monger/set-db! (monger/get-db test-db-name)))

(defn populate-test-db []
	(monger-col/remove "docs")
	(monger-col/insert "docs" test-obj-a)
	(monger-col/insert "docs" test-obj-b))

(defn setup-test-db []
	(connect-to-test-db)
	(populate-test-db))