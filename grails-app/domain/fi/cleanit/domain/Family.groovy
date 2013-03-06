package fi.cleanit.domain

import fi.cleanit.enums.ActiveTaskStatus
import org.joda.time.DateTime
import fi.cleanit.util.TaskService
import fi.cleanit.auth.AuthenticateService
import org.joda.time.DateMidnight

class Family {

    Date dateCreated;
    Date lastUpdated;
    
    static hasMany = ["tasks":Task, "users":User];
    static transients = ["taskService"];
    TaskService taskService;

    String familyName;
    String password;
    Locale locale;

    boolean isAdmin = false;

    static constraints = {
        tasks       (maxSize: 1000);
        familyName  (nullable:false, blank:false);
        password    (nullable:false, blank:false, minSize: 4);
        locale      (nullable:false);
    }

    void beforeInsert() {
        if (password.size() >= 4) {
            password = password.encodeAsSHA1();
        }
    }

    void beforeUpdate() {
        // Encode password only if it's changed
        if (this.isDirty("password")) {
            password = password.encodeAsSHA1();
        }
    }

    String toString() {
        return familyName;
    }

    /**
     * Get logged in user id from cookie and use that to retrieve the instance
     * @return User that is logged in
     */
    static Family getLoggedInFamily() {
        // User authenticateService implementation to get logged in user
        return AuthenticateService.getLoggedInFamily();
    }

    Map<Integer, List<Task>> dailyTasks() {
        List<Task> tasks = ActiveTask.findAllByStatusAndFamily(ActiveTaskStatus.UNDONE, this)?.task;
        tasks = tasks.sort { it.priority.ordinal() };
        return [0:tasks];
    }

    Map<Integer, List<Task>> weeklyTasks() {
        DateTime now = DateTime.now();

        List<Integer> weekDays = 1..7;
        int firstDayOfWeekInMonth = now.getDayOfMonth() - (now.getDayOfWeek() - 1);
        int firstDayOfWeekInYear = now.getDayOfYear() - (now.getDayOfWeek() - 1);

        List<Integer> monthDays = firstDayOfWeekInMonth..firstDayOfWeekInMonth+7;
        List<Integer> yearDays = firstDayOfWeekInYear..firstDayOfWeekInYear+7;

        DateMidnight weekStart = DateMidnight.now().withDayOfWeek(1);
        DateMidnight weekEnd = weekStart.withDayOfWeek(7).plusDays(1);

        List<TaskDate> dateInstances = TaskDate.createCriteria().list() {
            or {
                "in"("dayOfWeek", weekDays);
                "in"("dayOfMonth", monthDays);
                "in"("dayOfYear", yearDays);
                between("day", weekStart.toCalendar(this.locale), weekEnd.toCalendar(this.locale))
            }
            task {
                eq("family", this);
            }
        }

        List<Task> tasks = Task.createCriteria().list() {
            eq("family", this);
            dates {
                'in'("id", dateInstances.id);
            }
        }

        Map<Integer, List<Task>> results = [:];
        for (Task t in tasks) {
            for (TaskDate td in t.dates) {
                List<Integer> daysOfWeek = taskService.toDaysOfWeek(td);
                for (int day in daysOfWeek) {
                    if (!results[day]) {
                        results[day] = [];
                    }

                    if (!results[day].contains(t)) {
                        results[day] << t;
                    }
                }
            }
        }

        return results;
    }

    Map<Integer, List<Task>> monthlyTasks() {
        DateTime now = DateTime.now();

        int firstDayOfMonthInYear = now.getDayOfYear() - (now.getDayOfMonth() - 1);
        int lastDayOfMonthInYear = firstDayOfMonthInYear + (now.dayOfMonth().getMaximumValue() - 1);
        List<Integer> yearDays = firstDayOfMonthInYear..lastDayOfMonthInYear;
        DateMidnight monthStart = DateMidnight.now().withDayOfMonth(1);
        DateMidnight monthEnd = monthStart.withDayOfMonth(now.dayOfMonth().getMaximumValue()).plusDays(1);

        List<TaskDate> dateInstances = TaskDate.createCriteria().list() {
            or {
                isNull("dayOfYear");
                "in"("dayOfYear", yearDays);
                between("day", monthStart.toCalendar(this.locale), monthEnd.toCalendar(this.locale))
            }
            task {
                eq("family", this);
            }
        }

        if (dateInstances) {
            List<Task> tasks = Task.createCriteria().list() {
                eq("family", this);
                dates {
                    'in'("id", dateInstances.id);
                }
            }
        }

        Map<Integer, List<Task>> results = [:];
        for (Task t in tasks) {
            for (TaskDate td in t.dates) {
                List<Integer> daysOfMonth = taskService.toDaysOfMonth(td);
                for (int day in daysOfMonth) {
                    if (!results[day]) {
                        results[day] = [];
                    }

                    if (!results[day].contains(t)) {
                        results[day] << t;
                    }
                }
            }
        }

        return results;
    }
}
