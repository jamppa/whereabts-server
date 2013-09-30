function getUser(email, createdAt) {
	return {
		"user-uuid": "550e8400-e29b-41d4-a716-446655440000",
		"email": email,
		"followers": [],
		"following": [],
		"created-at": createdAt,
		"last-seen-at": createdAt,
		"role": "email"
	}
}

function getUserProfile(userId, nick, desc) {
	return {
		"user_id": userId,
		"nick": nick,
		"country": "fi",
		"description": desc,
		"photo": ""
	}
}

function randomString(length, chars) {
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.round(Math.random() * (chars.length - 1))];
    return result;
}

function randomEmail() {
	return randomString(10, 'abcdefghijklmnopqrstuvxyzåäö') + '@test.fi';
}

function randomNick() {
	return randomString(20, 'abcdefghijklmnopqrstuvxyzåäö');
}

function randomDescription() {
	return randomString(35, 'abcdefghijklmnopqrstuvxyzåäö');	
}

function insertTestUsers(numOfUsers) {
	db.users.remove();
	for(i = 0; i < numOfUsers; i++){
		db.users.insert(getUser(randomEmail(), new Date().getTime()));
	}
}

function insertTestUserProfiles() {
	db.profiles.remove();
	db.users.find().forEach(
		function(doc) {
			db.profiles.insert(getUserProfile(doc["_id"], randomNick(), randomDescription()));
		});
	db.profiles.find().forEach(
		function(doc) {
			db.users.update({_id: doc["user_id"]}, {$set: {profile_id: doc["_id"]}});
		});
}

insertTestUsers(100);
insertTestUserProfiles();