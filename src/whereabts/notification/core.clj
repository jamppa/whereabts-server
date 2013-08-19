(ns whereabts.notification.core
	(:require [taoensso.carmine :as carmine]))

(def conn-pool (carmine/make-conn-pool))
(def conn-spec (carmine/make-conn-spec))

(defmacro with-carmine [& body] 
	`(carmine/with-conn conn-pool conn-spec ~@body))

(defn publish-message [channel message]
	(with-carmine (carmine/publish channel message)))
