(ns whereabts.core.profiles-search
	(:use
		whereabts.models.profile))

(defn- valid-search-str? [search-str]
	(not (empty? search-str)))

(defn find-recent-profiles [{user-id :_id}]
	(let [session-user-id (.toString user-id)]
		(filter #(not (= session-user-id (.toString (:user_id %)))) 
			(find-profiles-recent 10))))

(defn find-profiles [search-str]
	(if (valid-search-str? search-str)
		(find-profiles-by-name search-str)
		[]))