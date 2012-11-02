(ns loccify.db-integration-test
	(:use [loccify.db])
	(:use [midje.sweet]))

(def user-obj {:name "teppo tepponen" :email "teppo@tepponen.fi"})

(defn- is-valid? [user-obj]
	(and
		(contains? user-obj :_id)
		(= "teppo tepponen" (:name user-obj))
		(= "teppo@tepponen.fi" (:email user-obj))))

(defn- query-details [query]
	{:find-type :find-one :collection "users" :query query})

(background (before :facts (db-connect "loccify_test")))

(fact "should insert object to database with merged object-id"
	(let [inserted-user-obj (db-insert "users" user-obj)]
		(is-valid? inserted-user-obj) => truthy))

(fact "should find object from database by its id"
	(let [inserted-user-obj (db-insert "users" user-obj)
			found-user-obj (db-find (query-details {:_id (:_id inserted-user-obj)}))]
		(= inserted-user-obj found-user-obj) => truthy))
