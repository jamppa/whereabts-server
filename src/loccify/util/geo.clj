(ns loccify.util.geo)

(defn location [lon lat dist]
	{:lon lon :lat lat :dist dist})

(defn meters-to-degrees [m]
	(-> m
	(/ 1000)
	(/ 111.12)))