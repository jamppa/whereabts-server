(ns loccify.server
  (:use [loccify.db]
        [loccify.api.signup]
        [compojure.core]
        [ring.middleware.format-response :only [wrap-restful-response]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(def ^:dynamic *loccify-db* "loccify")

(defn init-db-connection []
	(db-connect *loccify-db*)
	(db-geospatialize "loccages"))

(defroutes api-routes
  (context "/api" [] signup-routes)
  (route/not-found "go away!"))

(def server
  (-> (handler/api api-routes)
      (wrap-restful-response)))

	