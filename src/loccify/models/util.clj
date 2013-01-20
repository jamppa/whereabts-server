(ns loccify.models.util
	(:require [monger.util :as util]))

(defn with-obj-id [obj]
	(assoc obj :_id (util/object-id)))

(defn created-now [obj]
	(assoc obj :created-at (System/currentTimeMillis)))