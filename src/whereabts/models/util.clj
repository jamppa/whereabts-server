(ns whereabts.models.util
	(:use [clojure.string :only [split join]])
	(:require [monger.util :as util])
	(:import [org.bson.types ObjectId]))

(defn ellipsize-str-max-words [str word-count]
	(let [words (split str #"\s+")]
		(if (> (count words) word-count)
		(join " " (conj (vec (take word-count words)) "..."))
		(join " " words))))
		
(defn ellipsize-str-max-len [string len]
	(if (> (count string) len)
		(str (subs string 0 len) "...")
		string))

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
		(ellipsize-str-max-len (:message obj) 30)
		(:title obj)))