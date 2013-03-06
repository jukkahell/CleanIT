package fi.cleanit.domain

import fi.cleanit.auth.AuthenticateService

class FamilyController {

    AuthenticateService authenticateService;
    
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [familyInstanceList: Family.list(params), familyInstanceTotal: Family.count()]
    }

    def create = {
        def familyInstance = new Family()
        User userInstance = new User();
        familyInstance.properties = params
        userInstance.properties = params;
        return [familyInstance: familyInstance, userInstance: userInstance]
    }

    def save = {
        def familyInstance = new Family(params)
        params.points = 0;
        User userInstance = new User(params);
        if (!userInstance.hasErrors() && !familyInstance.hasErrors()) {
            familyInstance.addToUsers(userInstance).save(flush:true)
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'family.label', default: 'Family'), familyInstance.id])}"
            forward(action: "login", params: params);
        }
        else {
            render(view: "create", model: [familyInstance: familyInstance])
        }
    }

    def show = {
        def familyInstance = Family.get(params.id)
        if (!familyInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'family.label', default: 'Family'), params.id])}"
            redirect(action: "list")
        }
        else {
            [familyInstance: familyInstance]
        }
    }

    def edit = {
        def familyInstance = Family.get(params.id)
        if (!familyInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'family.label', default: 'Family'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [familyInstance: familyInstance]
        }
    }

    def update = {
        def familyInstance = Family.get(params.id)
        if (familyInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (familyInstance.version > version) {
                    
                    familyInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'family.label', default: 'Family')] as Object[], "Another user has updated this Family while you were editing")
                    render(view: "edit", model: [familyInstance: familyInstance])
                    return
                }
            }
            familyInstance.properties = params
            if (!familyInstance.hasErrors() && familyInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'family.label', default: 'Family'), familyInstance.id])}"
                redirect(action: "show", id: familyInstance.id)
            }
            else {
                render(view: "edit", model: [familyInstance: familyInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'family.label', default: 'Family'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def familyInstance = Family.get(params.id)
        if (familyInstance) {
            try {
                familyInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'family.label', default: 'Family'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'family.label', default: 'Family'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'family.label', default: 'Family'), params.id])}"
            redirect(action: "list")
        }
    }

    def login = {
        Family f = authenticateService.login(params.email, params.password, true);
        if (f) {
            flash.message = "${message(code:'loginSucceeded', default:'Login succeeded! Welcome ' + f.familyName)}"
            redirect(url:grailsApplication.config.grails.serverURL+"?lang="+f.locale.language);
        } else {
            flash.message = "${message(code:'loginFailed', default:'Login failed')}"
            redirect(url:grailsApplication.config.grails.serverURL)
        }
    }

    def logout = {
        authenticateService.logout();
        redirect(url:grailsApplication.config.grails.serverURL);
    }
}
