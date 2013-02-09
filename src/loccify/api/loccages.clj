(ns loccify.api.loccages
	(:use
		[compojure.core]
		[ring.util.response]
		[loccify.core.loccages]
		[loccify.util.geo]))

(defroutes loccage-routes

	(GET "/loccages/:ll-lon/:ll-lat/:ur-lon/:ur-lat" [ll-lon ll-lat ur-lon ur-lat]
		(let [ll-vec [(read-string ll-lon) (read-string ll-lat)]
			ur-vec [(read-string ur-lon) (read-string ur-lat)]]
		(response (find-all-loccages-by-bbox (bounding-box ll-vec ur-vec)))))
)

