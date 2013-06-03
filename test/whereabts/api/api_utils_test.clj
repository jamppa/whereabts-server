(ns whereabts.api.api-utils-test
	(:use 
		[midje.sweet]
		[whereabts.api.api-utils])
	(:import [whereabts.exception WhereabtsForbiddenException]))

(def request {:basic-authentication {:role "public"}})
(defn do-something [val] val)

(fact "should return nil when basic-authenticateds users role match"
	(check-role request ["public"]) => nil)

(fact "should throw exception when basic-authenticateds useres role does not match"
	(check-role request ["anonymous"]) => (throws WhereabtsForbiddenException))

(fact "should return nil when basic-authenticated role is in allowed roles"
	(check-role request ["anonymous" "public" "email"]) => nil)

(fact "should expand with-role macro correctly"
	(macroexpand-1 '(with-role request "public" (do-something))) => 
		(quote (clojure.core/let [] (whereabts.api.api-utils/check-role request "public") (do-something))))

(fact "should throw exception when executing body with wrong role"
	(with-role request ["anonymous"] (:executed)) => (throws WhereabtsForbiddenException))

(fact "should evaluate body when executing it with allowed role"
	(with-role request ["public" "anonymous"] (do-something {:some "val"})) => {:some "val"})