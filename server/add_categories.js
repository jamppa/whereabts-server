function addCategories() {
	db.categories.remove();
	db.categories.insert({"_id": 1, "key": "traffic" });
	db.categories.insert({"_id": 2, "key": "food_drinks" });
	db.categories.insert({"_id": 3, "key": "home_leisure" });
	db.categories.insert({"_id": 4, "key": "travel" });
	db.categories.insert({"_id": 5, "key": "nightlife" });
	db.categories.insert({"_id": 6, "key": "sports_activities" });
	db.categories.insert({"_id": 7, "key": "shopping" });
	db.categories.insert({"_id": 8, "key": "beauty" });
	db.categories.insert({"_id": 9, "key": "happenings" });
	db.categories.insert({"_id": 10, "key": "services" });
}

addCategories();