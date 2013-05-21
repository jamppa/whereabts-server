(ns whereabts.server
    (:use 
        [whereabts.core.auth]
        [whereabts.db]
        [whereabts.api.messages-api]
        [whereabts.api.feedbacks-api]
        [whereabts.api.registration-api]
        [whereabts.util.middleware]
        [whereabts.models.message]
        [compojure.core]
        [ring.middleware.json :only [wrap-json-body]]
        [ring.middleware.json :only [wrap-json-response]]
        [ring.middleware.basic-authentication])
    (:require 
        [compojure.handler :as handler]
        [compojure.route :as route]))

(defn init-db-connection []
	(db-connect))

(defroutes public-routes
    (context "/api" [] 
        registration-api-routes))

(defroutes user-routes
    (context "/api" []
        messages-api-routes feedbacks-api-routes))

(defroutes api-routes
    public-routes 
    user-routes
    (route/not-found "Move on, nothing to see here..."))

(def server
    (-> (handler/api api-routes)
        (wrap-json-body)
        (wrap-exception-logger)
        (wrap-exception-handler)
        (wrap-json-response)
        (wrap-basic-authentication authenticate)))
