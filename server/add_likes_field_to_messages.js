function addLikesFieldToMessages() { 
	db.messages.update({}, {$set: {'likes': []}}, {multi: true});
}

addLikesFieldToMessages();