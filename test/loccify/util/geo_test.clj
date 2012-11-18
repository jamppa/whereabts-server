(ns loccify.util.geo-test
	(:use [loccify.util.geo])
	(:use [midje.sweet]))

(fact "should create location-object"
	(location 10.0 10.0 500) => {:lon 10.0, :lat 10.0, :dist 500})
