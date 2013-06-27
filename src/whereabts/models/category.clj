(ns whereabts.models.category
	(:use
		[whereabts.db] 
		[whereabts.models.util]))

(def categories-coll "categories")

(defn find-category-by-id [id]
	(db-find-one-by-id categories-coll (obj-id id)))