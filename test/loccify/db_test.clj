(ns loccify.db-test
	(:use [midje.sweet])
	(:use [loccify.db])
	(:require [monger.core :as monger]
			  [monger.collection :as monger-collection]))

(def obj-to-insert {:somekey "someval"})

(fact "should insert object to collection and return it"
	(db-insert "loccify-test" obj-to-insert) => obj-to-insert
	(provided (monger-collection/insert-and-return "loccify-test" anything) => obj-to-insert :times 1 ))

