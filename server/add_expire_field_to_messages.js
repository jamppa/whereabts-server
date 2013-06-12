function addExpireFieldToMessages() {
	db.messages.find().forEach(
		function(doc){
			if(doc["expire-time"] == null){
				db.messages.update({"_id":doc._id}, {"$set":{"expire-time": 86400000, "expires-at": Date.now() + 86400000}})
			}
		});	
}

addExpireFieldToMessages();