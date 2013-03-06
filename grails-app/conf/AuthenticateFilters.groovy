import fi.cleanit.domain.Family

/**
 *
 * @author jukka
 */
class AuthenticateFilters {

    public filters = {
        // Do filter for all url:s
        all(controller:'*', action:'*') {
            before = {
                Family f = Family.getLoggedInFamily();
                if (f) {
                    if (f.isAdmin || isAllowedForUser(controllerName, actionName) || isAllowedForAnonymous(controllerName, actionName)) {
                        return true;
                    } else {
                        log.debug("Family ${f.id} not allowed to access /${controllerName}/${actionName}")
                        redirect(url:grailsApplication.config.grails.serverURL);
                        return false;
                    }
                } else if (isAllowedForAnonymous(controllerName, actionName)) {
                    return true;
                } else {
                    log.debug("User not allowed to access /${controllerName}/${actionName}")
                    redirect(url:grailsApplication.config.grails.serverURL);
                    return false;
                }
            }
            after = { model ->
                if (!model || controllerName == "image") {
                } else {
                    // Always attach loggedInFamily for every page (except images and video loading) since it's used almost everywhere
                    Family loggedInFamily = Family.getLoggedInFamily();
                    if (loggedInFamily) {
                        model.loggedInFamily = loggedInFamily;
                    }
                }
            }
        }
    }

    private boolean isAllowedForAnonymous(String controller, String action) {
        Map<String, ArrayList<String>> allowedForUsers = [
                "default":["*"],
                "family":["create", "save", "login"]
        ];
        return (allowedForUsers[controller] && (allowedForUsers[controller][0] == "*" || allowedForUsers[controller].contains(action)))
    }

    private boolean isAllowedForUser(String controller, String action) {
        Map<String, ArrayList<String>> allowedForUsers = [
                "user":["*"],
                "family":["show", "edit", "update", "logout"],
                "task":["*"]
        ];
        return (allowedForUsers[controller] && (allowedForUsers[controller][0] == "*" || allowedForUsers[controller].contains(action)))
    }
}

