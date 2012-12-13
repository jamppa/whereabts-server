(defproject loccify "0.1.0-SNAPSHOT"
            :description "Server for location based messaging service"
            :dependencies [[org.clojure/clojure "1.4.0"]
                           [compojure "1.1.3"]
                           [ring-basic-authentication "1.0.1"]
                           [ring-middleware-format "0.2.2"]
                           [com.novemberain/monger "1.1.2"]
                           [midje "1.4.0"]]
            :plugins [[lein-ring "0.7.5"]]
            :ring {:handler loccify.server/server
            		:init loccify.server/init-db-connection})

