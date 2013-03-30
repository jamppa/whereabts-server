(ns loccify.db-helper
	(:require 
		[monger.core :as monger] 
		[monger.collection :as monger-col])
	(:use [loccify.db-test-fixtures])
	(:import [org.bson.types ObjectId]))

(def test-db-name "whereabtsdb_test")

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
	(clear-collections ["docs" "users" "loccages" "anon-loccages"])
	(insert-test-objects "docs" [test-obj-a test-obj-b])
	(insert-test-objects "users" [test-user-a test-user-b])
	(insert-test-objects "loccages" [test-loccage-a test-loccage-b test-loccage-c])
	(insert-test-objects "anon-loccages" [test-anon-message-a test-anon-message-b test-anon-message-c]))

(defn setup-test-db []
	(connect-to-test-db)
	(populate-test-db)
	(create-geospatial-idxs ["loccages" "anon-loccages"]))