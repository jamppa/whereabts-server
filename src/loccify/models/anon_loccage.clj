(ns loccify.models.anon-loccage
	(:use
		[loccify.db]
		[loccify.models.util]
		[validateur.validation]))

(def anon-loccage-col "anon-loccages")
(def anon-loccage-validation
	(validation-set
		(presence-of :message)
		(presence-of :nick)
		(presence-of :loc)
		(presence-of :created-at)))

(defn find-anon-loccage-by-id [id-str]
	(db-find
		(db-find-details
			:find-one anon-loccage-col {:_id (obj-id id-str)})))

(defn save-anon-loccage [loccage]
	(let [new-loccage (created-now loccage)]
		(when (valid? anon-loccage-validation new-loccage)
			(db-insert anon-loccage-col new-loccage))))