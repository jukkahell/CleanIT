package fi.cleanit.domain

class User {

    Date dateCreated;
    Date lastUpdated;
    
    static belongsTo = ["family":Family];
    static hasMany = ["doneTasks":ActiveTask];

    String firstName;
    String lastName;
    String email;

    int points = 0;

    static constraints = {
        firstName       (nullable: false, blank: false);
        lastName        (nullable: false, blank: false);
        email           (nullable: false, blank: false, email: true);
    }

    String toString() {
        return firstName;
    }
}
