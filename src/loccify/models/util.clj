(ns loccify.models.util
	(:require
		[monger.joda-time]
		[monger.json]
		[clj-time.core :as time]))

(defn created-now [obj]
	(assoc obj :created-at (time/now)))