(ns whereabts.notification.core
	(:require [taoensso.carmine :as carmine]))

(def conn-pool (carmine/make-conn-pool))
(def conn-spec (carmine/make-conn-spec))

(def reply-channel "replies")

(defmacro with-carmine [& body] 
	`(carmine/with-conn conn-pool conn-spec ~@body))

(defn notify-on-reply [message]
	(with-carmine (carmine/publish reply-channel message)))