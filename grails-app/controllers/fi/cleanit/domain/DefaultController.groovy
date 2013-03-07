package fi.cleanit.domain

class DefaultController {

    def index = {
        String listType = params.listType;
        Map<Integer, List<Task>> tasks;
        int taskCount = 0;

        if (Family.getLoggedInFamily()) {
            if (listType == "month") {
                tasks = Family.getLoggedInFamily()?.monthlyTasks()
            } else {
                tasks = Family.getLoggedInFamily()?.dailyTasks();
            }
            taskCount = tasks.values().flatten().size();
        } else {
            tasks = [0:null] as Map<Integer, List<Task>>;
        }

        [tasks:tasks, taskCount:taskCount];
    }
}
