function addDefaultCategoryToMessages() {
	db.messages.find().forEach(
		function(doc){
			if(doc["category_id"] == null){
				db.messages.update({"_id":doc._id}, {"$set":{"category_id": 9}});
			}
		});	
}

addDefaultCategoryToMessages();