(ns whereabts.functional.messages-api-functest
	(:use 
		[midje.sweet]
		[whereabts.functional.api-functest]
		[whereabts.db-helper])
	(:require 
		[clj-http.client :as http]
		[clojure.data.json :as json]))

(def valid-anonymous-credentials ["anonymous@whereabts.com" "550e8400-e29b-41d4-a716-446655440000"])
(def whereabts-api-messages (str whereabts-api-testsrv "/messages"))

(defn- whereabts-api-messages-by-bbox [ll-lon ll-lat ur-lon ur-lat]
	(str whereabts-api-messages "/" ll-lon "/" ll-lat "/" ur-lon "/" ur-lat))

(def message-as-json 
	(json/write-str 
		{:nick "teppo" :title "cool title!" :message "yey, cool things!" :loc {:lon 24.1234 :lat 60.2323} :expire-time 5000}))
(def message-with-empty-title-as-json
	(json/write-str 
		{:nick "teppo" :title "" :message "yey, cool things!" :loc {:lon 24.5678 :lat 60.673} :expire-time 5000}))
(def invalid-message-as-json 
	(json/write-str 
		{:message "yey, cool things!" :loc {:lon 22.3123 :lat 60.1231}}))

(defn- post-message-as-user [payload]
	(http/post whereabts-api-messages
		(whereabts-api-request valid-anonymous-credentials payload)))

(background (before :facts (setup-db)))

(fact "should response with HTTP Created when POST new message as an anonymous whereabts user" :functional
	(:status (post-message-as-user message-as-json)) => 201)

(fact "should reponse with HTTP Created when POST new message with empty title as an anonymous user" :functional
	(:status (post-message-as-user message-with-empty-title-as-json)) => 201)

(fact "should response with HTTP Bad Request when trying to POST invalid message" :functional
	(:status (post-message-as-user invalid-message-as-json)) => 400)

(fact "should response with HTTP Unauthorized when trying to POST message with invalid credentials" :functional
	(:status (http/post whereabts-api-messages
		(whereabts-api-request ["invalid@user.com" "blaah"] message-as-json))) => 401)

(fact "should response with HTTP Forbidden when POSTing message with public user" :functional
	(:status (http/post whereabts-api-messages
		(whereabts-api-request-public-user message-as-json))) => 403)

(fact "should response with HTTP OK when GETting all messages by bbox as an anonymous user" :functional
	(:status (http/get (whereabts-api-messages-by-bbox 24.987 60.255 24.989 60.260)
		(whereabts-api-request valid-anonymous-credentials ""))) => 200)

(fact "should response with HTTP Unauthorized when trying to GET messages by bbox with invalid credentials" :functional
	(:status (http/get (whereabts-api-messages-by-bbox 24.987 60.255 24.989 60.260)
		(whereabts-api-request ["invalid@blaa.com" "secret"] ""))) => 401)

(fact "should response with HTTP OK when GETing message by its id" :functional
	(:status (http/get (str whereabts-api-messages "/509d513f61395f0ebbd5e36a")
		(whereabts-api-request valid-anonymous-credentials ""))) => 200)

(fact "should response with HTTP OK when GETting message by its id as public user" :functional
	(:status (http/get (str whereabts-api-messages "/509d513f61395f0ebbd5e36a")
		(whereabts-api-request-public-user ""))) => 200)

(fact "should response with HTTP Not Found when GETing message that does not exist" :functional
	(:status (http/get (str whereabts-api-messages "/516e7cfde4b025a0abeff666")
		(whereabts-api-request valid-anonymous-credentials ""))) => 404)

(fact "should response with HTTP OK when DELETING message of a user" :functional
	(:status (http/delete (str whereabts-api-messages "/509d513f61395f0ebbd5e36a") 
		(whereabts-api-request valid-anonymous-credentials ""))) => 200)

(fact "should response with HTTP Forbidden when DELETING message as a public user" :functional
	(:status (http/delete (str whereabts-api-messages "/509d513f61395f0ebbd5e36a")
		(whereabts-api-request-public-user ""))) => 403)
