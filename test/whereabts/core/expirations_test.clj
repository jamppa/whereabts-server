(ns whereabts.core.expirations-test
	(:use
		[midje.sweet]
		[whereabts.core.expirations]))

(def obj-to-expire {:expire-time 1000})
(def obj-to-expire-missing-expire-time {})

(fact "should add expires-at field by adding expire-time to current system time"
	(expires-at obj-to-expire) => (merge obj-to-expire {:expires-at 2000})
	(provided (current-time-millis) => 1000 :times 1))

(fact "should add expires-at field by adding default expiration time to current system time when expire-time is missing"
	(expires-at obj-to-expire-missing-expire-time) => {:expire-time default-expiration-time :expires-at (+ default-expiration-time 1000)}
	(provided (current-time-millis) => 1000 :times 1))

(fact "should give current time in milliseconds"
	(current-time-millis) => (roughly (System/currentTimeMillis)))