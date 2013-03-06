package cleanit

import fi.cleanit.util.TaskService


class TaskActivationJob {
    /*
        cronExpression: "s m h D M W Y"
                         | | | | | | `- Year [optional]
                         | | | | | `- Day of Week, 1-7 or SUN-SAT, ?
                         | | | | `- Month, 1-12 or JAN-DEC
                         | | | `- Day of Month, 1-31, ?
                         | | `- Hour, 0-23
                         | `- Minute, 0-59
                         `- Second, 0-59
    */
    static triggers = {
        //simple name:'TaskActivationTrigger', startDelay:10000, repeatInterval: 30000, repeatCount: 10
        cron name: 'TaskActivationTrigger', cronExpression: "0 0 0 * * ?" // Execute once a day at midnight
    }

    TaskService taskService;

    def execute() {
        log.debug("Activating new tasks...");
        taskService.activate();
        log.debug("Done.");
    }
}
