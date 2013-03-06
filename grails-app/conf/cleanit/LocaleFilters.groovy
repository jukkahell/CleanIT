package cleanit

import org.springframework.web.servlet.support.RequestContextUtils as RCU
import fi.cleanit.domain.Family;

class LocaleFilters {

    def filters = {
        all(controller: '*', action: '*') {
            before = {
                params.lang = params.lang ?: "${RCU.getLocale(request)}";
                Family f = Family.getLoggedInFamily();
                if (f && params.lang != f.locale.language && request.method == 'GET') {
                    params.lang = f.locale.language;
                    redirect(controller:controllerName, action:actionName, params:params)
                }
            }
            after = {

            }
            afterView = {

            }
        }
    }

}
