package fi.cleanit.domain

class TaskDate {

    static belongsTo = ["task":Task];

    Calendar day;
    Integer dayOfWeek;
    Integer dayOfMonth;
    Integer dayOfYear;

    static constraints = {
        dayOfMonth  (nullable: true);
        dayOfWeek   (nullable: true);
        dayOfYear   (nullable: true);
        day         (nullable: true);
    }
}
