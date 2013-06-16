(ns whereabts.notification.core
	(:use [whereabts.models.util])
	(:require [taoensso.carmine :as carmine]))

(def conn-pool (carmine/make-conn-pool))
(def conn-spec (carmine/make-conn-spec))

(def reply-channel "message.replies")

(defmacro with-carmine [& body] 
	`(carmine/with-conn conn-pool conn-spec ~@body))

(defn prepare-message-for-channel [reply message]
	{
		:replied_message (obj-id-as-str message) 
		:user_to_notify (id-as-str message :user_id)
		:reply (obj-id-as-str reply)})

(defn notify-on-reply [reply user message]
	(with-carmine (carmine/publish reply-channel 
		(prepare-message-for-channel reply message))))