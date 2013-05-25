(ns whereabts.core.with-util-test
	(:use
		[midje.sweet]
		[whereabts.core.with-util]))

(def obj {})
(def user {:_id 123})
(def other-user {:_id 234})
(def obj-with-user (merge obj {:user_id (:_id user)}))
(def message {:_id 456})
(def obj-with-message (merge obj {:message_id (:_id message)}))

(fact "should return object with user id"
	(with-user obj user) => obj-with-user)

(fact "should return object with message id"
	(with-message obj message) => obj-with-message)

(fact "should return object with truthy ownership when user ids are the same"
	(with-ownership obj-with-user user) => (merge obj-with-user {:owns true}))

(fact "should not add ownershipt to message when user does not own it"
	(with-ownership obj-with-user other-user) => (merge obj-with-user {:owns false}))