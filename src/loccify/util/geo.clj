(ns loccify.util.geo)

(def one-degree-in-km 111.12)
(def one-km-in-meters 1000)

(defn location [lon lat dist]
	{:lon lon :lat lat :dist dist})

(defn meters-to-degrees [m]
	(-> m
	(/ one-km-in-meters)
	(/ one-degree-in-km)))

(defn bounding-box [ll-vec ur-vec]
	{:lower-left ll-vec :upper-right ur-vec})