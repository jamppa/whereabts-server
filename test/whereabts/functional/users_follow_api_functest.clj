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

(defn- whereabts-followings-api [user-id]
	(str whereabts-api-testsrv "/user/" user-id "/following"))

(defn- post-follow-as [follower following]
	(http/post (whereabts-follow-user-api (.toString (:_id following)))
		(whereabts-api-request [(:email follower) (:user-uuid follower)] "")))

(defn- post-unfollow-as [follower following]
	(http/delete (whereabts-follow-user-api (.toString (:_id following)))
		(whereabts-api-request [(:email follower) (:user-uuid follower)] "")))

(defn- get-followers [user auth]
	(http/get (whereabts-follow-user-api (.toString (:_id user)))
		(whereabts-api-request [(:email auth) (:user-uuid auth)] "")))

(defn- get-followings [user auth]
	(http/get (whereabts-followings-api (.toString (:_id user)))
		(whereabts-api-request [(:email auth) (:user-uuid auth)] "")))

(background (before :facts (setup-db)))

(fact "should POST follow request for user" :functional
	(:status (post-follow-as test-user-b test-user-a)) => 201)

(fact "should POST follow request for nonexisting user and return http not found" :functional
	(:status (post-follow-as test-user-b {:_id (ObjectId.)})) => 404)

(fact "should send DELETE request for unfollowing a user" :functional
	(:status (post-unfollow-as test-user-b test-user-a)) => 200)

(fact "should send GET request for user followers" :functional
	(:status (get-followers test-user-a test-user-a)) => 200)

(fact "should send GET request for user followings" :functional
	(:status (get-followings test-user-a test-user-a)) => 200)