(ns whereabts.exception.WhereabtsForbiddenException
	(:gen-class :extends java.lang.RuntimeException))

(defn -init [msg]
	[[msg]])