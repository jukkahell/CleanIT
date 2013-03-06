package fi.cleanit.domain

import fi.cleanit.enums.ActiveTaskStatus

class ActiveTask {

    static hasMany = ["users":User];
    static belongsTo = [User];
    
    Family family;
    Task task;
    Calendar date;
    ActiveTaskStatus status = ActiveTaskStatus.UNDONE;

    static constraints = {
        family      (nullable: false);
        users       (nullable: true);
        task        (nullable: false);
        date        (nullable: false);
    }

    String toString() {
        return task.name;
    }
}
