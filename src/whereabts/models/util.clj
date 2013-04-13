(ns whereabts.models.util
	(:use [clojure.string :only [split join]])
	(:require [monger.util :as util])
	(:import [org.bson.types ObjectId]))

(defn ellipsize-str [str]
	(let [words (split str #"\s+")]
		(join " " (conj (vec (take 4 words)) "..."))))

(defn obj-id [hex]
	(ObjectId. hex))

(defn with-obj-id [obj]
	(assoc obj :_id (util/object-id)))

(defn created-now [obj]
	(assoc obj :created-at (System/currentTimeMillis)))

(defn obj-id-as-str [obj]
	(.toString (:_id obj)))

(defn short-message [obj]
	(if (empty? (:title obj))
		(:message obj)
		(:title obj)))