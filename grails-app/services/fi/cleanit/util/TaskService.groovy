package fi.cleanit.util

import fi.cleanit.domain.Task
import org.joda.time.DateTime
import fi.cleanit.domain.ActiveTask
import fi.cleanit.domain.TaskDate
import fi.cleanit.enums.ActiveTaskStatus
import org.joda.time.Months
import org.joda.time.DateMidnight

class TaskService {

    static transactional = false;

    void activate() {
        int dayOfYear = DateTime.now().getDayOfYear();
        int dayOfMonth = DateTime.now().getDayOfMonth();
        int dayOfWeek = DateTime.now().getDayOfWeek();
        DateMidnight dayStart = DateMidnight.now();
        DateMidnight dayEnd = dayStart.plusDays(1);
        Locale locale = Locale.getDefault();
        
        List<TaskDate> dates = TaskDate.createCriteria().list() {
            or {
                eq("dayOfYear", dayOfYear);
                eq("dayOfMonth", dayOfMonth);
                eq("dayOfWeek", dayOfWeek);
                and {
                    between("day", dayStart.toCalendar(locale), dayEnd.toCalendar(locale))
                }
            }
        }

        for (TaskDate td in dates) {
            activate(td.task);
        }
    }

    void activate(Task t) {
        ActiveTask at = ActiveTask.findByTaskAndStatus(t, ActiveTaskStatus.UNDONE);
        if (!at) {
            at = new ActiveTask();
            at.date = Calendar.getInstance();
            at.task = t;
            at.family = t.family;
            at.save();
        }
    }

    boolean isActiveNow(Task t) {
        List<Integer> dayOfYears = [];
        for (TaskDate td in t.dates) {
            dayOfYears += toDaysOfYear(td)
        }
        return dayOfYears.contains(DateTime.now().getDayOfYear())
    }

    List<Integer> toDaysOfMonth(TaskDate t) {
        if (t.dayOfMonth) {
            return [t.dayOfMonth];
        } else if (t.dayOfYear) {
            return [DateTime.now().withDayOfYear(t.dayOfYear).getDayOfMonth()];
        } else if (t.dayOfWeek) {
            DateTime now = DateTime.now();
            DateTime beginningOfMonth = now.withDayOfMonth(1);
            int daysInMonth = now.dayOfMonth().getMaximumValue();
            int weeksInMonth = Math.ceil((double )(daysInMonth / 7));
            List<Integer> results = [];

            for (int i = 0; i < weeksInMonth; i++) {
                DateTime taskDate = beginningOfMonth.withDayOfWeek(t.dayOfWeek);
                if (taskDate.getMonthOfYear() == now.getMonthOfYear()) {
                    results << taskDate.getDayOfMonth();
                }
                beginningOfMonth = beginningOfMonth.plusWeeks(1);
            }
            return results;
        } else if (t.day) {
            DateTime date = new DateTime(t.day);
            return [date.getDayOfMonth()];
        }

        throw new Exception("No date specified for task ${t.id}");
    }

    List<Integer> toDaysOfWeek(TaskDate t) {
        if (t.dayOfMonth) {
            return [DateTime.now().withDayOfMonth(t.dayOfMonth).getDayOfWeek()];
        } else if (t.dayOfYear) {
            return [DateTime.now().withDayOfYear(t.dayOfYear).getDayOfWeek()];
        } else if (t.dayOfWeek) {
            return [t.dayOfWeek];
        } else if (t.day) {
            DateTime date = new DateTime(t.day);
            return [date.getDayOfWeek()];
        }

        throw new Exception("No date specified for task ${t.id}");
    }

    List<Integer> toDaysOfYear(TaskDate t) {
        if (t.dayOfMonth) {
            DateTime now = DateTime.now();
            DateTime beginningOfYear = now.withDayOfYear(1);
            DateTime endOfYear = now.withDayOfYear(now.dayOfYear().getMaximumValue());
            int monthsInYear = Months.monthsBetween(beginningOfYear, endOfYear).getMonths();
            List<Integer> results = [];

            for (int i = 0; i < monthsInYear; i++) {
                results << beginningOfYear.withDayOfMonth(t.dayOfMonth).getDayOfYear();
                beginningOfYear = beginningOfYear.plusMonths(1);
            }
            return results;
        } else if (t.dayOfYear) {
            return [t.dayOfYear];
        } else if (t.dayOfWeek) {
            DateTime now = DateTime.now();
            DateTime beginningOfYear = now.withDayOfYear(1);
            int daysInYear = now.dayOfYear().getMaximumValue();
            int weeksInYear = Math.ceil((double )(daysInYear / 7));
            List<Integer> results = [];

            for (int i = 0; i < weeksInYear; i++) {
                results << beginningOfYear.withDayOfWeek(t.dayOfWeek).getDayOfYear();
                beginningOfYear = beginningOfYear.plusWeeks(1);
            }
            return results;
        } else if (t.day) {
            DateTime date = new DateTime(t.day);
            return [date.getDayOfYear()];
        }

        throw new Exception("No date specified for task ${t.id}");
    }

    List<TaskDate> createDates(Map params) {
        List<TaskDate> dates = [];

        for (String day in params.list("dayOfWeek")) {
            TaskDate date = new TaskDate();
            date.dayOfWeek = day as int;
            dates << date;
        }

        for (String day in params.list("dayOfMonth")) {
            TaskDate date = new TaskDate();
            date.dayOfMonth = day as int;
            dates << date;
        }

        for (String day in params.list("dayOfYear")) {
            TaskDate date = new TaskDate();
            date.dayOfYear = day as int;
            dates << date;
        }

        if (params.day) {
            TaskDate date = new TaskDate();
            date.day = Calendar.getInstance();
            date.day.setTime(params.day);
            dates << date;
        }

        return dates;
    }
}
