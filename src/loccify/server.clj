(ns loccify.server
    (:use 
        [loccify.db]
        [loccify.api.signup]
        [loccify.util.middleware]
        [compojure.core]
        [ring.middleware.format-response :only [wrap-restful-response]]
        [ring.middleware.json :only [wrap-json-body]])
    (:require 
        [compojure.handler :as handler]
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
        (wrap-json-body)
        (wrap-exception-handler)
        (wrap-restful-response)))