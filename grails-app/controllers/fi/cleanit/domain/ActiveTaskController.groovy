package fi.cleanit.domain

class ActiveTaskController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [activeTaskInstanceList: ActiveTask.list(params), activeTaskInstanceTotal: ActiveTask.count()]
    }

    def create = {
        def activeTaskInstance = new ActiveTask()
        activeTaskInstance.properties = params
        return [activeTaskInstance: activeTaskInstance]
    }

    def save = {
        def activeTaskInstance = new ActiveTask(params)
        if (activeTaskInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'activeTask.label', default: 'ActiveTask'), activeTaskInstance.id])}"
            redirect(action: "show", id: activeTaskInstance.id)
        }
        else {
            render(view: "create", model: [activeTaskInstance: activeTaskInstance])
        }
    }

    def show = {
        def activeTaskInstance = ActiveTask.get(params.id)
        if (!activeTaskInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'activeTask.label', default: 'ActiveTask'), params.id])}"
            redirect(action: "list")
        }
        else {
            [activeTaskInstance: activeTaskInstance]
        }
    }

    def edit = {
        def activeTaskInstance = ActiveTask.get(params.id)
        if (!activeTaskInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'activeTask.label', default: 'ActiveTask'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [activeTaskInstance: activeTaskInstance]
        }
    }

    def update = {
        def activeTaskInstance = ActiveTask.get(params.id)
        if (activeTaskInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (activeTaskInstance.version > version) {
                    
                    activeTaskInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'activeTask.label', default: 'ActiveTask')] as Object[], "Another user has updated this ActiveTask while you were editing")
                    render(view: "edit", model: [activeTaskInstance: activeTaskInstance])
                    return
                }
            }
            activeTaskInstance.properties = params
            if (!activeTaskInstance.hasErrors() && activeTaskInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'activeTask.label', default: 'ActiveTask'), activeTaskInstance.id])}"
                redirect(action: "show", id: activeTaskInstance.id)
            }
            else {
                render(view: "edit", model: [activeTaskInstance: activeTaskInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'activeTask.label', default: 'ActiveTask'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def activeTaskInstance = ActiveTask.get(params.id)
        if (activeTaskInstance) {
            try {
                activeTaskInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'activeTask.label', default: 'ActiveTask'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'activeTask.label', default: 'ActiveTask'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'activeTask.label', default: 'ActiveTask'), params.id])}"
            redirect(action: "list")
        }
    }
}
