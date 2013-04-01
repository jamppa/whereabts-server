(ns loccify.core.loccages
	(:use
		[loccify.models.message]
		[loccify.models.anon-message]))

(defn- all-messages [loccages anon-loccages]
	{:loccages loccages :anon-loccages anon-loccages})

(defn find-all-messages-by-bbox [bbox]
	(let [loccages (find-messages-by-bbox bbox)
		anon-loccages (find-anon-messages-by-bbox bbox)]
		(all-messages loccages anon-loccages)))