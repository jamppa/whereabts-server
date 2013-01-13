(ns loccify.core.auth-test
	(:use 
		[loccify.core.auth]
		[loccify.models.user]
		[midje.sweet]))

(def user {})

(fact "should be authenticated user when one is found by email and password"
	(authenticated? "teppo@test.fi" "secret") => true
	(provided (find-user-by-email-and-pass "teppo@test.fi" "secret") => user))

(fact "should not be authenticated user when one is not found by email and password"
	(authenticated? "teppo@test.fi" "secret") => false
	(provided (find-user-by-email-and-pass "teppo@test.fi" "secret") => nil))

(fact "should be available username if user is not found by it"
	(available-username? "teppo") => true
	(provided (find-user-by-name "teppo") => nil))

(fact "should not be available username if user is found by it"
	(available-username? "teppo") => false
	(provided (find-user-by-name "teppo") => user))

(fact "should be available email if user is not found by it"
	(available-email? "teppo@test.fi") => true
	(provided (find-user-by-email "teppo@test.fi") => nil))

(fact "should not be available email if user is found by it"
	(available-email? "teppo@test.fi") => false
	(provided (find-user-by-email "teppo@test.fi") => user))

(fact "should authenticate user by finding it with email and password"
	(authenticate "teppo@testaaja.fi" "secret") => user
	(provided (find-user-by-email-and-pass "teppo@testaaja.fi" "secret") => user))