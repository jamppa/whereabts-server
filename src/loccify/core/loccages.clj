(ns loccify.core.loccages
	(:use
		[loccify.models.message]
		[loccify.models.anon-message]))

(defn- all-loccages [loccages anon-loccages]
	{:loccages loccages :anon-loccages anon-loccages})

(defn find-all-loccages-by-bbox [bbox]
	(let [loccages (find-messages-by-bbox bbox)
		anon-loccages (find-anon-messages-by-bbox bbox)]
		(all-loccages loccages anon-loccages)))