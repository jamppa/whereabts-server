(defproject whereabts-server "0.1.0-SNAPSHOT"
   :description "Whereabts server and REST API"
   :dependencies 
      [[org.clojure/clojure "1.5.1"]
      [compojure "1.1.5"]
      [ring-middleware-format "0.3.0"]
      [ring-http-basic-auth "0.0.1"]
      [com.novemberain/monger "1.5.0"]]
   :plugins [[lein-ring "0.8.3"]]
   :ring {
         :handler whereabts.server/server
         :init whereabts.server/init-db-connection}
   :profiles {
      :dev {
         :dependencies [[ring-mock "0.1.3"] [midje "1.5.1"]]
         :plugins [[lein-midje "3.0.0"]]}})

