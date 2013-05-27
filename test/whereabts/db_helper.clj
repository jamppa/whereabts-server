(ns whereabts.db-helper
	(:require 
		[monger.core :as monger] 
		[monger.collection :as monger-col])
	(:use
		[whereabts.db]
		[whereabts.db.test-fixtures]
		[whereabts.db.user-test-fixtures]
		[whereabts.db.reply-test-fixtures])
	(:import [org.bson.types ObjectId]))

(def test-db-name "whereabtsdb_test")

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
	(clear-collections ["docs" "anonymous_users" "messages" "feedbacks" "replies"])
	(insert-test-objects "docs" [test-obj-a test-obj-b])
	(insert-test-objects "anonymous_users" [anonymous-user-a anonymous-user-b])
	(insert-test-objects "messages" [test-message-a test-message-b test-message-c test-message-d])
	(insert-test-objects "feedbacks" [test-feedback-a])
	(insert-test-objects "replies" [test-reply-a test-reply-b]))

(defn setup-test-db []
	(binding [*whereabts-db* test-db-name]
		(db-connect)
		(populate-test-db)
		(create-geospatial-idxs ["messages"])))

(defn setup-db []
	(db-connect)
	(populate-test-db)
	(create-geospatial-idxs ["messages"]))