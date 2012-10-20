(ns loccify.db-test
	(:use [midje.sweet])
	(:use [loccify.db])
	(:require [monger.core :as monger]
			  [monger.collection :as monger-collection]))

(def obj-to-insert {:somekey "someval"})
(def query-find-one {:find-type :find-one :collection "coll" :query "query"})
(def query-find-many (assoc query-find-one :find-type :find-many))
(def query-result {:key "val"})

(fact "should insert object to collection and return it"
	(db-insert "loccify" obj-to-insert) => obj-to-insert
	(provided (monger-collection/insert-and-return "loccify" anything) => obj-to-insert :times 1 ))

(fact "should find one as map from collection by given query-details"
	(db-find query-find-one) => query-result
	(provided (monger-collection/find-one-as-map "coll" "query") => query-result :times 1))

(fact "should find many as maps from collection by given query-details"
	(db-find query-find-many) => query-result
	(provided (monger-collection/find-maps "coll" "query") => query-result :times 1))