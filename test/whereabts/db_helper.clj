(ns whereabts.db-helper
	(:require 
		[monger.core :as monger] 
		[monger.collection :as monger-col])
	(:use 
		[whereabts.db.test-fixtures]
		[whereabts.db.user-test-fixtures])
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
	(clear-collections ["docs" "users" "anonymous_users" "messages" "user_messages" "feedbacks"])
	(insert-test-objects "docs" [test-obj-a test-obj-b])
	(insert-test-objects "users" [test-user-a test-user-b])
	(insert-test-objects "anonymous_users" [anonymous-user-a])
	(insert-test-objects "user_messages" [test-usr-message-a test-usr-message-b test-usr-message-c])
	(insert-test-objects "messages" [test-message-a test-message-b test-message-c])
	(insert-test-objects "feedbacks" [test-feedback-a]))

(defn setup-test-db []
	(connect-to-test-db)
	(populate-test-db)
	(create-geospatial-idxs ["messages" "user_messages"]))