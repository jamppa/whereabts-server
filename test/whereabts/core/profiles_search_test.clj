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

(fact "should find user profiles by given search string"
	(find-profiles "asd") => found-profiles
	(provided
		(find-profiles-by-name "asd") => found-profiles :times 1))

(fact "should return empty vec when given search string is empty"
	(find-profiles "") => []
	(provided
		(find-profiles-by-name "") => anything :times 0))