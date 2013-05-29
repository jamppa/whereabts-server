function addUpdatedFieldToMessages() { 
	db.messages.find().forEach(
		function(doc) { 
			if(doc["updated-at"] == null){
				db.messages.update({"_id":doc._id}, {"$set":{"updated-at":doc["created-at"]}});
			} 
		}); 
}

addUpdatedFieldToMessages();