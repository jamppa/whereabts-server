(ns loccify.models.anon-loccage
	(:use
		[loccify.db]
		[loccify.models.util]))

(defn find-anon-loccage-by-id [id-str]
	(db-find
		(db-find-details :find-one "anon-loccages" {:_id (obj-id id-str)})))