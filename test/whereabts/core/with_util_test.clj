(ns whereabts.core.with-util-test
	(:use
		[midje.sweet]
		[whereabts.core.with-util]
		[whereabts.models.message]))

(def obj {})
(def user {:_id "123"})
(def other-user {:_id "234"})
(def obj-with-user (merge obj {:user_id (:_id user)}))
(def message {:_id "456" :likes ["123"]})
(def obj-with-message (merge obj {:message_id (:_id message)}))

(fact "should return object with user id"
	(with-user obj user) => obj-with-user)

(fact "should return object with message id"
	(with-message obj message) => obj-with-message)

(fact "should return object with truthy ownership when user ids are the same"
	(with-ownership obj-with-user user) => (merge obj-with-user {:owns true}))

(fact "should not add ownershipt to message when user does not own it"
	(with-ownership obj-with-user other-user) => (merge obj-with-user {:owns false}))

(fact "should return message with liked-status true when users id is in likes list"
	(with-liked message user) => (merge message {:liked true}))

(def message (merge message {:likes []}))
(fact "should return message with liked-status false when users id is not in likes list"
	(with-liked message user) => (merge message {:liked false}))

(fact "should return message with liked-status false when likes list is nil"
	(with-liked {} user) => {:liked false})

(def message (merge message {:created-at 1}))
(fact "should return message with expiration time"
	(with-expiration message message-expiration-time-ms) => 
		(merge message {:expires-at (+ (:created-at message) message-expiration-time-ms)}))

(def message (merge message {:likes ["asdasdasd"]}))
(fact "should return message with likes as number"
	(with-likes-as-number message) => (merge message {:likes 1}))

(def user (assoc user :followers [(:_id other-user)]))
(fact "should return user with following as true when another users id is found from followers"
	(with-followed? user other-user) => (merge user {:followed true}))

(def user (assoc user :followers []))
(fact "should return following user with :followed false when followers id is not found from :followers"
	(with-followed? user other-user) => (merge user {:followed false}))

(def user {:following ["123" "abc"]})
(fact "should return user with followings as number"
	(with-following-as-number user) => (merge user {:following 2}))

