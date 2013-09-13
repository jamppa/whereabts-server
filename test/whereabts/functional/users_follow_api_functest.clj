(ns whereabts.functional.users-follow-api-functest
	(:use
		midje.sweet
		whereabts.functional.api-functest
		whereabts.db-helper
		whereabts.db.user-test-fixtures)
	(:require
		[clj-http.client :as http]
		[clojure.data.json :as json])
	(:import [org.bson.types ObjectId]))

(defn- whereabts-follow-user-api [user-id] 
	(str whereabts-api-testsrv "/user/" user-id "/followers"))

(defn- post-follow-as [follower following]
	(http/post (whereabts-follow-user-api (.toString (:_id following)))
		(whereabts-api-request [(:email follower) (:user-uuid follower)] "")))

(defn- post-unfollow-as [follower following]
	(http/delete (whereabts-follow-user-api (.toString (:_id following)))
		(whereabts-api-request [(:email follower) (:user-uuid follower)] "")))

(background (before :facts (setup-db)))

(fact "should POST follow request for user" :functional
	(:status (post-follow-as test-user-b test-user-a)) => 201)

(fact "should POST follow request for nonexisting user and return http not found"
	(:status (post-follow-as test-user-b {:_id (ObjectId.)})) => 404)

(fact "should send DELETE request for unfollowing a user"
	(:status (post-unfollow-as test-user-b test-user-a)) => 200)