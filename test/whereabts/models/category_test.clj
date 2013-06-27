(ns whereabts.models.category-test
	(:use 
		[midje.sweet]
		[whereabts.models.category]
		[whereabts.db.category-test-fixtures]
		[whereabts.db-helper]))

(background (before :facts (setup-test-db)))

(fact "should find category by its id"
	(find-category-by-id "509d513f61395f0ebbd5e50a") => test-category-a)

(fact "should not find category by its id, when one does not exist"
	(find-category-by-id "509d513f61395f0ebbd5e666") => nil)

(fact "should find category by its key"
	(find-category-by-key :sports_and_activities) => test-category-a)

(fact "should not find category by its key, when one does not exist"
	(find-category-by-key :something_non_existing) => nil)