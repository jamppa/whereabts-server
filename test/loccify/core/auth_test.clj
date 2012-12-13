(ns loccify.core.auth-test
	(:use [loccify.core.auth]
			[loccify.models.user]
			[midje.sweet]))

(def auth-user {})

(fact "should be authenticated user when one is found by email and password"
	(authenticated? "teppo@test.fi" "secret") => true
	(provided (find-user-by-email-and-pass "teppo@test.fi" "secret") => auth-user))

(fact "should not be authenticated user when one is not found by email and password"
	(authenticated? "teppo@test.fi" "secret") => false
	(provided (find-user-by-email-and-pass "teppo@test.fi" "secret") => nil))
