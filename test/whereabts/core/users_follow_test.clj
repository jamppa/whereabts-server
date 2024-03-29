(ns whereabts.core.users-follow-test
	(:use
		midje.sweet
		whereabts.core.users-follow
		whereabts.models.user
		whereabts.models.profile
		whereabts.notification.follow-notification)
	(:import [whereabts.exception WhereabtsResourceNotFoundException]))

(def user-id "some")

(def user-follower {:_id "123abc" :following []})
(def user {:_id "456abc" :followers []})
(def user-after-followed (merge user {:followers [(:_id user-follower)]}))

(fact "should follow user by adding follower and following"
	(follow-user user-id user-follower) => anything
	(provided
		(find-user-by-id user-id) => user :times 1
		(add-follower user user-follower) => anything :times 1
		(add-following user-follower user) => anything :times 1
		(publish-follow-message user-follower user) => anything :times 1))

(fact "should throw exception when trying to follow user that does not exist"
	(follow-user user-id user-follower) => (throws WhereabtsResourceNotFoundException)
	(provided
		(find-user-by-id user-id) => nil :times 1
		(add-follower user user-follower) => anything :times 0
		(add-following user-follower user) => anything :times 0))

(fact "should add follower for user"
	(add-follower user user-follower) => user-after-followed
	(provided
		(update-user user-after-followed) => user-after-followed :times 1))

(def user-already-followed user-after-followed)
(fact "should not add double follower for user"
	(add-follower user-already-followed user-follower) => user-already-followed
	(provided
		(update-user user-already-followed) => user-already-followed :times 1))

(def follower-after-following (merge user-follower {:following [(:_id user)]}))
(fact "should add following for user"
	(add-following user-follower user) => follower-after-following
	(provided
		(update-user follower-after-following) => follower-after-following :times 1))

(fact "should not add double following for user"
	(add-following follower-after-following user) => follower-after-following
	(provided
		(update-user follower-after-following) => follower-after-following :times 1))

(fact "should unfollow user by removing follower and following"
	(unfollow-user user-id user-follower) => anything
	(provided
		(find-user-by-id user-id) => user :times 1
		(remove-follower user user-follower) => anything :times 1
		(remove-following user-follower user) => anything :times 1))

(fact "should throw exception when trying to unfollow user that does not exist"
	(unfollow-user user-id user-follower) => (throws WhereabtsResourceNotFoundException)
	(provided
		(find-user-by-id user-id) => nil :times 1
		(remove-follower user user-follower) => anything :times 0
		(remove-following user-follower user) => anything :times 0))

(fact "should remove user follower from user following"
	(remove-follower user-after-followed user-follower) => user
	(provided
		(update-user user) => user :times 1))

(fact "should remove user following from user follower"
	(remove-following follower-after-following user-after-followed) => user-follower
	(provided
		(update-user user-follower) => user-follower :times 1))

(def follower-profile {:nick "testman"})
(def followers (:followers user-after-followed))
(fact "should find followers of a user"
	(find-followers-of-user user-id) => [follower-profile]
	(provided
		(find-user-by-id user-id) => user-after-followed :times 1
		(find-profiles-by-user-ids followers) => [follower-profile] :times 1))

(def following-profile follower-profile)
(def user-after-following (merge user-after-followed {:following [(:_id user-follower)]}))
(def followings (:following user-after-following))
(fact "should find followings of a user"
	(find-followings-of-user user-id) => [following-profile]
	(provided
		(find-user-by-id user-id) => user-after-following :times 1
		(find-profiles-by-user-ids followings) => [following-profile] :times 1))

(fact "should throw exception when finding followings of a user that doesnt exist"
	(find-followings-of-user user-id) => (throws WhereabtsResourceNotFoundException)
	(provided
		(find-user-by-id user-id) => nil :times 1))