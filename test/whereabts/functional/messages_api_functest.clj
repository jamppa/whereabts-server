(ns whereabts.functional.messages-api-functest
	(:use 
		[midje.sweet]
		[whereabts.functional.api-functest])
	(:require 
		[clj-http.client :as http]
		[clojure.data.json :as json]))

(def whereabts-api-messages (str whereabts-api-testsrv "/messages"))
(defn- whereabts-api-messages-by-bbox [ll-lon ll-lat ur-lon ur-lat]
	(str whereabts-api-messages "/" ll-lon "/" ll-lat "/" ur-lon "/" ur-lat))

(def message-as-json 
	(json/write-str {:nick "teppo" :title "cool title!" :message "yey, cool things!" :loc [34.122 45.434]}))

(def message-with-empty-title-as-json
	(json/write-str {:nick "teppo" :title "" :message "yey, cool things!" :loc [34.122 45.434]}))

(def invalid-message-as-json 
	(json/write-str {:message "yey, cool things!" :loc [34.122 45.434]}))

(fact "should response with HTTP Created when POST new message as an anonymous whereabts user" :functional
	(:status (http/post whereabts-api-messages 
		(whereabts-api-request-anon message-as-json))) => 201)

(fact "should reponse with HTTP Created when POST new message with empty title as an anonymous user" :functional
	(:status (http/post whereabts-api-messages 
		(whereabts-api-request-anon message-as-json))) => 201)

(fact "should response with HTTP Bad Request when trying to POST invalid message" :functional
	(:status (http/post whereabts-api-messages
		(whereabts-api-request-anon invalid-message-as-json))) => 400)

(fact "should response with HTTP Unauthorized when trying to POST message with invalid credentials" :functional
	(:status (http/post whereabts-api-messages
		(whereabts-api-request ["invalid@user.com" "blaah"] message-as-json))) => 401)

(fact "should response with HTTP OK when GETing all messages by bounding box" :functional
	(:status (http/get (whereabts-api-messages-by-bbox 24.987 60.255 24.989 60.260)
		(whereabts-api-request-anon ""))) => 200)