(defproject whereabts-server "0.1.0-SNAPSHOT"
   :description "Whereabts server and REST API"
   :dependencies 
      [[org.clojure/clojure "1.4.0"]
      [compojure "1.1.3"]
      [ring-middleware-format "0.2.2"]
      [ring/ring-json "0.1.2"]
      [ring-http-basic-auth "0.0.1"]
      [com.novemberain/monger "1.3.4"]
      [midje "1.4.0"]]
   :plugins [[lein-ring "0.8.3"]]
   :ring {:handler loccify.server/server
            :init loccify.server/init-db-connection}
   :profiles {:dev {:dependencies [[ring-mock "0.1.3"]]}})

