(ns whereabts.functional.feedbacks-api-functest
	(:use 
		[midje.sweet]
		[whereabts.functional.api-functest])
	(:require 
		[clj-http.client :as http]
		[clojure.data.json :as json]))


