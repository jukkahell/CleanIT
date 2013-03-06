package fi.cleanit.domain

import fi.cleanit.util.TaskService
import fi.cleanit.enums.ActiveTaskStatus

class TaskController {

    TaskService taskService;

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        Family f = Family.getLoggedInFamily();
        [taskInstanceList: Task.findAllByFamily(f, params), taskInstanceTotal: Task.count()]
    }

    def create = {
        def taskInstance = new Task()
        taskInstance.properties = params
        return [taskInstance: taskInstance]
    }

    def save = {
        params["family.id"] = Family.getLoggedInFamily()?.id;
        def taskInstance = new Task(params)
        List<TaskDate> dates = taskService.createDates(params);
        for (TaskDate td in dates) {
            taskInstance.addToDates(td);
        }

        if (taskInstance.save(flush: true)) {
            if (taskService.isActiveNow(taskInstance)) {
                taskService.activate(taskInstance);
            }
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'task.label', default: 'Task'), taskInstance.id])}"
            redirect(action: "show", id: taskInstance.id)
        }
        else {
            render(view: "create", model: [taskInstance: taskInstance])
        }
    }

    def show = {
        def taskInstance = Task.get(params.id)
        if (!taskInstance || taskInstance.family != Family.getLoggedInFamily()) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        }
        else {
            [taskInstance: taskInstance]
        }
    }

    def edit = {
        def taskInstance = Task.get(params.id)
        if (!taskInstance || taskInstance.family != Family.getLoggedInFamily()) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [taskInstance: taskInstance]
        }
    }

    def update = {
        def taskInstance = Task.get(params.id)
        if (taskInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (taskInstance.version > version) {
                    
                    taskInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'task.label', default: 'Task')] as Object[], "Another user has updated this Task while you were editing")
                    render(view: "edit", model: [taskInstance: taskInstance])
                    return
                }
            }
            params["family.id"] = Family.getLoggedInFamily()?.id;
            List<TaskDate> dates = taskService.createDates(params);
            List<TaskDate> oldDates = taskInstance.dates as List;
            oldDates.each {
                taskInstance.removeFromDates(it);
                it.delete();
            }
            for (TaskDate td in dates) {
                taskInstance.addToDates(td);
            }
            taskInstance.properties = params;
            if (!taskInstance.hasErrors() && taskInstance.save(flush: true)) {
                if (taskService.isActiveNow(taskInstance)) {
                    taskService.activate(taskInstance);
                }
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'task.label', default: 'Task'), taskInstance.id])}"
                redirect(action: "show", id: taskInstance.id)
            }
            else {
                render(view: "edit", model: [taskInstance: taskInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def taskInstance = Task.get(params.id)
        if (taskInstance && taskInstance.family == Family.getLoggedInFamily()) {
            try {
                taskInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'task.label', default: 'Task'), params.id])}"
            redirect(action: "list")
        }
    }

    def done = {
        Long taskId;
        List<Long> userIds;
        Family family = Family.getLoggedInFamily();

        try {
            taskId = params.id as long;
            userIds = params.list("userIds").collect { it as Long };
        } catch (NumberFormatException e) {
            flash.errors = "${g.message(code:'invalidId', default:'Invalid id!')}";
            redirect(url:grailsApplication.config.grails.serverURL);
            return;
        }

        List<User> users = User.findAllByIdInList(userIds);
        boolean allUsersInFamily = true;
        for (User u in users) {
            if (!family.users.contains(u)) {
                allUsersInFamily = false;
                break;
            }
        }

        if (family && allUsersInFamily && users.size() > 0) {
            Task task = Task.get(taskId);
            if (task) {
                ActiveTask at = ActiveTask.findByTaskAndStatus(task, ActiveTaskStatus.UNDONE);
                at.status = ActiveTaskStatus.DONE;

                for(User u in users) {
                    at.addToUsers(u);
                    u.points += (int)task.points/users.size();
                    u.save(flush:true);
                }
                at.save(flush:true);
                redirect(url:grailsApplication.config.grails.serverURL);
            } else {
                flash.message = "${g.message(code:'task.notExists', default:'Given task does not exist.')}";
                redirect(url:grailsApplication.config.grails.serverURL);
            }
        } else {
            flash.message = "${g.message(code:'task.invalidUsers', default:'Selected users are not family members.')}";
            redirect(url:grailsApplication.config.grails.serverURL);
        }
    }
}
