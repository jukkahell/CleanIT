package fi.cleanit.domain

import fi.cleanit.enums.Priority

class Task {

    Date dateCreated;
    Date lastUpdated;
    
    static belongsTo = ["family":Family];
    static hasMany = ["dates":TaskDate];

    String name;
    String description;

    Priority priority = Priority.MEDIUM;

    int points = 1;

    static constraints = {
        name        (nullable: false, blank: false, maxSize: 70);
        description (nullable: true, maxSize: 4000);
    }

    String toString() {
        return name;
    }
}
