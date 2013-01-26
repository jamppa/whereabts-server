(ns loccify.models.anon-loccage-test
	(:use
		[midje.sweet]
		[loccify.models.anon-loccage]
		[loccify.db-helper]))

(def valid-anon-loccage {:message "msg" :nick "Cool Guy" :loc [45.1 56.4]})

(background (before :facts (setup-test-db)))

(fact "should find anonymous loccage by id"
	(find-anon-loccage-by-id "509d513f61395f0ebbd5e36a") => test-anon-loccage-a)

(fact "should save valid anonymous loccage"
	(let [saved-loccage (save-anon-loccage valid-anon-loccage)]
		(find-anon-loccage-by-id (.toString (:_id saved-loccage))) => saved-loccage))