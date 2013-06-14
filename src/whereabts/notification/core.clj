(ns whereabts.notification.core
	(:use [whereabts.models.util])
	(:require [taoensso.carmine :as carmine]))

(def conn-pool (carmine/make-conn-pool))
(def conn-spec (carmine/make-conn-spec))

(def reply-channel "replies")

(defmacro with-carmine [& body] 
	`(carmine/with-conn conn-pool conn-spec ~@body))

(defn prepare-message-for-channel [message]
	{:message_id (obj-id-as-str message) 
		:user_id (id-as-str message :user_id)})

(defn notify-on-reply [message]
	(with-carmine (carmine/publish reply-channel 
		(prepare-message-for-channel message))))