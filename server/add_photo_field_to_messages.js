function addPhotoFieldToMessages() { 
	db.messages.find().forEach(
		function(doc) { 
			if(doc["photo"] == null){
				db.messages.update({"_id":doc._id}, {"$set":{"photo":false}});
			} 
		}); 
}

addPhotoFieldToMessages();