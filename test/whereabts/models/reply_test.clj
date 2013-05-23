(ns whereabts.models.reply-test
	(:use
		[midje.sweet]
		[whereabts.models.reply]
		[whereabts.db-helper]
		[whereabts.db.reply-test-fixtures]))

(def new-reply 
	(merge (dissoc test-reply-a :_id) {:replymessage "Yyyeaaahhh!"}))

(background (before :facts (setup-test-db)))

(fact "should find reply by its id"
	(find-reply-by-id "509d513f61395f0ebbd5e40a") => test-reply-a)

; (fact "should save new reply message"
; 	(let [saved (save-new-reply new-reply)]))