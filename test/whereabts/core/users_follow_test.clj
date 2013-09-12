(ns whereabts.core.users-follow-test
	(:use
		midje.sweet
		whereabts.core.users-follow
		whereabts.models.user)
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(def user-id "some")
(def user {})
(def user-follower {})

(fact "should follow user by adding follower and following"
	(follow-user user-id user-follower) => anything
	(provided
		(find-user-by-id user-id) => user :times 1
		(add-follower user user-follower) => anything :times 1
		(add-following user-follower user) => anything :times 1))

(fact "should throw exception when trying to follow user that does not exist"
	(follow-user user-id user-follower) => (throws WhereabtsResourceNotFoundException)
	(provided
		(find-user-by-id user-id) => nil :times 1
		(add-follower user user-follower) => anything :times 0
		(add-following user-follower user) => anything :times 0))