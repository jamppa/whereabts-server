(ns loccify.exception.SignUpException
	(:gen-class :extends java.lang.RuntimeException))

(defn -init [msg]
	[[msg]])