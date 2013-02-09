(ns loccify.core.loccages-test
	(:use
		[midje.sweet]
		[loccify.models.loccage]
		[loccify.models.anon-loccage]
		[loccify.core.loccages]
		[loccify.util.geo]))

(def loccages [{:message ""}])
(def anon-loccages [{:message ""}])
(def expected-all-loccages {:loccages loccages :anon-loccages anon-loccages})

(def bbox (bounding-box [1 1] [1 1]))

(fact "should find all loccages by bounding box"
	(find-all-loccages-by-bbox bbox) => expected-all-loccages
	(provided (find-loccages-by-bbox bbox) => loccages :times 1)
	(provided (find-anon-loccages-by-bbox bbox) => anon-loccages :times 1))