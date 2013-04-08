(ns whereabts.functional.messages-api-functest
	(:use 
		[midje.sweet]
		[whereabts.functional.api-functest])
	(:require 
		[clj-http.client :as http]
		[clojure.data.json :as json]))

(def whereabts-api-messages (str whereabts-api-testsrv "/messages"))

(defn message-as-json []
	(json/write-str {:nick "teppo" :message "yey, cool things!" :loc [34.122 45.434]}))

(fact "should POST new message to service as an anonymous whereabts user" :functional
	(:status (http/post whereabts-api-messages 
		(whereabts-api-request-anon (message-as-json)))) => 201)