(ns loccify.api.loccages-test
	(:use
		[loccify.api.loccages]
		[loccify.core.loccages]
		[midje.sweet]
		[ring.mock.request]
		[loccify.api-helper]
		[loccify.util.geo]))

(def bbox (bounding-box [1.23 1.23] [5.0 5.0]))
(def loccages {:loccages [] :anon-loccages []})
(def expected-res-for-loccages (expected-res 200 loccages))

(fact "should response correctly when requesting all loccages inside bounding box"
	(loccage-routes (request :get "/loccages/1.23/1.23/5.0/5.0")) => expected-res-for-loccages
	(provided (find-all-loccages-by-bbox bbox) => loccages :times 1))