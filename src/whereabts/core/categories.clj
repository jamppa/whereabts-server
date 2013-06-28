(ns whereabts.core.categories
	(:use [whereabts.models.category]))

(defn with-category [obj]
	(if-let [category (find-category-by-key (:category-key obj))]
		(merge obj {:category_id (:_id category)})
		obj))