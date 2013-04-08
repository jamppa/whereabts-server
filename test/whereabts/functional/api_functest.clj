(ns whereabts.functional.api-functest)

(def whereabts-api-testsrv "http://testsrv.whereabts.com:8080/api")

(defn whereabts-api-request [auth body]
	{:accept :json
	 :content-type :json
	 :basic-auth auth
	 :body body})

(defn whereabts-api-request-anon [body]
	(whereabts-api-request 
		["anonymous@whereabts.com" "ae129325a4db22faab7771f10b39a8af"] body))