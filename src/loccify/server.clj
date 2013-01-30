(ns loccify.server
    (:use 
        [loccify.db]
        [loccify.api.signup]
        [loccify.util.middleware]
        [loccify.models.loccage]
        [loccify.models.anon-loccage]
        [compojure.core]
        [ring.middleware.format-response :only [wrap-restful-response]]
        [ring.middleware.json :only [wrap-json-body]])
    (:require 
        [compojure.handler :as handler]
        [compojure.route :as route]))

(defn init-db-connection []
	(db-connect)
	(db-geospatialize [loccage-col anon-loccage-col]))

(defn init-app []
    (init-db-connection))

(defroutes api-routes
    (context "/api" [] signup-routes)
    (route/not-found "go away!"))

(def server
    (-> (handler/api api-routes)
        (wrap-json-body)
        (wrap-exception-handler)
        (wrap-restful-response)))