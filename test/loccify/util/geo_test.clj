(ns loccify.util.geo-test
	(:use [loccify.util.geo])
	(:use [midje.sweet]))

(fact "should create location-object"
	(location 10.0 10.0 500) => {:lon 10.0, :lat 10.0, :dist 500})

(fact "should convert meters to degrees"
	(meters-to-degrees 500) => (/ 0.5 111.12))

(fact "should create bounding box"
	(bounding-box [1.1 2.2] [3.3 4.4]) => {:lower-left [1.1 2.2] :upper-right [3.3 4.4]})
