(ns whereabts.core.categories-test
	(:use
		[midje.sweet]
		[whereabts.core.categories]
		[whereabts.models.category]))

(def category {:_id 1 :key "traffic"})
(def obj-with-category-key {:category-key "traffic"})
(def obj-with-category-id (merge obj-with-category-key {:category_id 1}))

(fact "should add correct category to object from the key"
	(with-category obj-with-category-key) => obj-with-category-id
	(provided (find-category-by-key "traffic") => category :times 1))

(fact "should not add any category when category-key is not available"
	(with-category {}) => {}
	(provided (find-category-by-key nil) => nil :times 1))