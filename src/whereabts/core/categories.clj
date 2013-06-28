(ns whereabts.core.categories
	(:use [whereabts.models.category]))

(def default-category-id 10)

(defn with-category [obj]
	(if-let [category (find-category-by-key (:category-key obj))]
		(merge obj {:category_id (:_id category)})
		(merge obj {:category_id default-category-id})))