(ns whereabts.functional.replies-api-functest
	(:use 
		[midje.sweet]
		[whereabts.functional.api-functest]
		[whereabts.db-helper]
		[whereabts.db.test-fixtures])
	(:require 
		[clj-http.client :as http]
		[clojure.data.json :as json]))

(def whereabts-replies-api-for-message
	(str whereabts-api-testsrv "/messages/509d513f61395f0ebbd5e36a/replies"))

(def whereabts-replies-api-for-message-not-found
	(str whereabts-api-testsrv "/messages/509d513f61395f0ebbd5e666/replies"))

(defn- post-reply-as-user [payload]
	(http/post whereabts-replies-api-for-message
		(whereabts-api-request ["anonymous@whereabts.com" "550e8400-e29b-41d4-a716-446655440000"] payload)))

(defn- post-reply-as-public-user [payload]
	(http/post whereabts-replies-api-for-message
		(whereabts-api-request-public-user payload)))

(defn- post-reply-as-invalid-user [payload]
	(http/post whereabts-replies-api-for-message
		(whereabts-api-request ["invalid@creds.com" "blaaah"] payload)))

(defn- post-reply-to-nonexisting-message [payload]
	(http/post whereabts-replies-api-for-message-not-found
		(whereabts-api-request ["anonymous@whereabts.com" "550e8400-e29b-41d4-a716-446655440000"] payload)))

(def reply-payload
	(json/write-str {:nick "jamppa" :replymessage "yeah!"}))

(background (before :facts (setup-db)))

(fact "should response HTTP Created when POSTing reply for message" :functional
	(:status (post-reply-as-user reply-payload)) => 201)

(fact "should response HTTP Not Found when POSTing relpy for nonexisting message" :functional
	(:status (post-reply-to-nonexisting-message reply-payload)) => 404)

(fact "should response HTTP Forbidden when POSTing reply as public user" :functional
	(:status (post-reply-as-public-user reply-payload)) => 403)

(fact "should reponse HTTP Unauthorized when POSTing reply as invalid user" :functional
	(:status (post-reply-as-invalid-user reply-payload)) => 401)