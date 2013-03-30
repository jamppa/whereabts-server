(ns loccify.core.loccages
	(:use
		[loccify.models.loccage]
		[loccify.models.anon-loccage]))

(defn- all-loccages [loccages anon-loccages]
	{:loccages loccages :anon-loccages anon-loccages})

(defn find-all-loccages-by-bbox [bbox]
	(let [loccages (find-loccages-by-bbox bbox)
		anon-loccages (find-anon-messages-by-bbox bbox)]
		(all-loccages loccages anon-loccages)))