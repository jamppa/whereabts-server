(ns whereabts.core.profiles-search
	(:use
		whereabts.models.profile))

(defn find-recent-profiles [{user-id :_id}]
	(let [session-user-id (.toString user-id)]
		(filter #(not (= session-user-id (.toString (:user_id %)))) 
			(find-profiles-recent 25))))

(defn find-profiles [search-str]
	[])