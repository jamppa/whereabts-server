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

(fact "should add default category when category-key is missing"
	(with-category {}) => {:category_id 10}
	(provided (find-category-by-key nil) => nil :times 1))

(fact "should resolve category id from key"
	(resolve-category-id "traffic") => 1
	(provided (find-category-by-key "traffic") => category :times 1))

(fact "should resolve category id to nil when category with key doesnt exist"
	(resolve-category-id "nonexisting") => nil
	(provided (find-category-by-key "nonexisting") => nil :times 1))