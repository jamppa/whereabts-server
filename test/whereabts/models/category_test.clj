(ns whereabts.models.category-test
	(:use 
		[midje.sweet]
		[whereabts.models.category]
		[whereabts.db.category-test-fixtures]
		[whereabts.db-helper]))

(background (before :facts (setup-test-db)))

(fact "should find category by its id"
	(find-category-by-id 1) => test-category-a)

(fact "should not find category by its id, when one does not exist"
	(find-category-by-id 100) => nil)

(fact "should find category by its key"
	(find-category-by-key :sports_and_activities) => test-category-a)

(fact "should not find category by its key, when one does not exist"
	(find-category-by-key :something_non_existing) => nil)

(fact "should find nil category when finding with nil key"
	(find-category-by-key nil) => nil)