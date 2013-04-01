(ns whereabts.core.signup-test
	(:use 
		[whereabts.core.signup]
		[whereabts.core.auth]
		[whereabts.models.user]
		[midje.sweet]))

(defn- new-user [name email password type]
	{:name name :email email :password password :type type})

(def user (new-user "teppo" "teppo@testaaja.fi" "secret" :email))

(fact "should signup a user when both name and email are available"
	(signup user) => user
	(provided (available-username? "teppo") => true)
	(provided (available-email? "teppo@testaaja.fi") => true)
	(provided (save-user user) => user))

(fact "should not signup a user when name is not available"
	(signup user) => (throws IllegalArgumentException)
	(provided (available-username? "teppo") => false)
	(provided (available-email? "teppo@testaaja.fi") => true)
	(provided (save-user user) => nil :times 0))

(fact "should not signup a user when email is not available"
	(signup user) => (throws IllegalArgumentException)
	(provided (available-username? "teppo") => true)
	(provided (available-email? "teppo@testaaja.fi") => false)
	(provided (save-user user) => nil :times 0))

(fact "should not signup a user when both email and name are not available"
	(signup user) => (throws IllegalArgumentException)
	(provided (available-username? "teppo") => false)
	(provided (available-email? "teppo@testaaja.fi") => false)
	(provided (save-user user) => nil :times 0))

(fact "should throw exception if save-user for some reason returns with nil"
	(signup user) => (throws IllegalArgumentException)
	(provided (available-username? "teppo") => true)
	(provided (available-email? "teppo@testaaja.fi") => true)
	(provided (save-user user) => nil))