(ns loccify.api.messages-api
	(:use
		[compojure.core]
		[ring.util.response]
		[loccify.core.messages]
		[loccify.util.geo]))

(defroutes loccage-routes

	(GET "/messages/:ll-lon/:ll-lat/:ur-lon/:ur-lat" [ll-lon ll-lat ur-lon ur-lat]
		(let [ll-vec [(read-string ll-lon) (read-string ll-lat)]
			ur-vec [(read-string ur-lon) (read-string ur-lat)]]
		(response (find-all-messages-by-bbox (bounding-box ll-vec ur-vec)))))
)

