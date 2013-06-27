(ns whereabts.models.category
	(:use
		[whereabts.db] 
		[whereabts.models.util]))

(def categories-coll "categories")

(defn find-category-by-id [id]
	(db-find-one-by-id categories-coll (obj-id id)))

(defn find-category-by-key [k]
	(db-find-one categories-coll {:key k}))