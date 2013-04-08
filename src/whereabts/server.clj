(ns whereabts.server
    (:use 
        [whereabts.core.auth]
        [whereabts.db]
        [whereabts.api.signup]
        [whereabts.api.messages-api]
        [whereabts.util.middleware]
        [whereabts.models.message]
        [whereabts.models.anon-message]
        [compojure.core]
        [ring.middleware.format-response :only [wrap-restful-response]]
        [ring.middleware.json :only [wrap-json-body]]
        [ring.middleware.http-basic-auth])
    (:require 
        [compojure.handler :as handler]
        [compojure.route :as route]))

(defn init-db-connection []
	(db-connect)
	(db-geospatialize [message-coll anon-message-coll]))

(defn init-app []
    (init-db-connection))

(defroutes api-routes
    (context "/api" [] signup-routes messages-api-routes)
    (route/not-found "Move on, nothing to see here..."))

(def server
    (-> (handler/api api-routes)
        (wrap-json-body)
        (wrap-exception-handler)
        (wrap-restful-response)
        (wrap-require-auth authenticate "Authentication failed!")))