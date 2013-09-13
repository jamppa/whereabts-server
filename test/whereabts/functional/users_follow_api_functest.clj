(ns whereabts.functional.users-follow-api-functest
	(:use
		midje.sweet
		whereabts.functional.api-functest
		whereabts.db-helper
		whereabts.db.user-test-fixtures)
	(:require
		[clj-http.client :as http]
		[clojure.data.json :as json]))

(defn- whereabts-follow-user-api [user-id] 
	(str whereabts-api-testsrv "/user/" user-id "/followers"))

(defn post-follow-as [follower following]
	(http/post (whereabts-follow-user-api (.toString (:_id following)))
		(whereabts-api-request [(:email follower) (:user-uuid follower)] "")))

(background (before :facts (setup-db)))

(fact "should POST follow request for user" :functional
	(:status (post-follow-as test-user-b test-user-a)) => 201)