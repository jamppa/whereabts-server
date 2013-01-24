(ns loccify.models.anon-loccage-test
	(:use
		[midje.sweet]
		[loccify.models.anon-loccage]
		[loccify.db-helper]))

(background (before :facts (setup-test-db)))

(fact "should find anonymous loccage by id"
	(find-anon-loccage-by-id "509d513f61395f0ebbd5e36a") => test-anon-loccage-a)