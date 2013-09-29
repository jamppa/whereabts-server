(ns whereabts.core.profiles-search-test
	(:use
		whereabts.core.profiles-search
		whereabts.models.profile
		midje.sweet))

(def user {:_id "123abc"})
(def found-profiles [{:user_id "123abc"} {:user_id "456fgh"}])

(fact "should find most recent profiles excluding given user"
	(find-recent-profiles user) => [{:user_id "456fgh"}]
	(provided
		(find-profiles-recent 25) => found-profiles :times 1))