/*
 * (c) 2011, Colonia
 * jukka@colonia.fi
 *
 * ALL RIGHTS RESERVED. This code contains material protected under
 * International and Federal Copyright Laws and Treaties. Any unauthorized
 * copy or use of this material is prohibited. No part of this code
 * may be reproduced or transmitted in any form without express written
 * permission from the author.
 *
 * @author Jukka Hell
 */

package fi.cleanit.auth;


import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH

import fi.cleanit.domain.Family;
import javax.servlet.http.HttpServletResponse
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib
import org.springframework.web.context.request.RequestContextHolder
import fi.colonia.util.CookieService

class AuthenticateService {

    GrailsApplication grailsApplication;
    CookieService cookieService;

    static transactional = true;
    static final String USERNAME_COOKIE_VALUE = "UCV";
    static final String USER_LOGIN_COOKIE_MAC_VALUE = "ULCM";

    /**
     * Implementation for user authentication.
     * Currently, simple email-password-combination is needed
     */
    Family login(String username, String password, boolean rememberMe = false) {
        Family u = Family.createCriteria().get() {
            users {
                eq('email', username)
            }
            eq('password', password?.encodeAsSHA1())
        }

        if (u) {
            login(u, rememberMe);
        }

        return u;
    }

    /**
     * Create login logics here
     * @param User u that is being logged in
     */
    void login(Family u, boolean rememberMe = false) {
        HttpServletResponse response = RequestContextHolder.getRequestAttributes().getCurrentResponse();

        String familyName = u.familyName.encodeAsBase64();
        cookieService.setCookie(USERNAME_COOKIE_VALUE, familyName, response, rememberMe);
        String ulcm = (CH.config.loginCookie.secretKey + u.familyName).encodeAsSHA1(); // User login cookie mac to verify cookie
        cookieService.setCookie(USER_LOGIN_COOKIE_MAC_VALUE, ulcm, response, rememberMe);
    }

    /**
     * Logout currently logged in user
     * @return Boolean true if logout was successful, otherwise false
     */
    boolean logout() {
        String loggedInUser = cookieService.getCookie(USERNAME_COOKIE_VALUE);

        if (loggedInUser) {
            HttpServletResponse response = RequestContextHolder.getRequestAttributes().getCurrentResponse();
            cookieService.deleteCookie(USERNAME_COOKIE_VALUE, response);
            cookieService.deleteCookie(USER_LOGIN_COOKIE_MAC_VALUE, response);
            return true;
        }
        return false;
    }

    static Family getLoggedInFamily() {
        CookieService cs = CookieService.getInstance();
        String username = cs.getCookie(USERNAME_COOKIE_VALUE);
        String ulcm = cs.getCookie(USER_LOGIN_COOKIE_MAC_VALUE); // User login cookie mac

        if (username) {
            username = new String(username.decodeBase64());
            String neededMac = (CH.config.loginCookie.secretKey + username).encodeAsSHA1();
            if (neededMac == ulcm) {
                Family u = Family.findByFamilyName(username);
                if (u) {
                    return u;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    /**
     * Check if user is human.
     * @param String answer that is given
     * @param String captcha number to identify which question was answered
     * @return boolean True if succeeded, otherwise false
     */
    boolean checkCaptcha(String answer, String captchaNro) {
        answer = answer.toUpperCase().trim();
        boolean correctAnswer = false;

        ApplicationTagLib g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib');
        String answerString = g.message(code:"captcha.answer${captchaNro}");
        if (answerString.contains(",")) {
            List<String> answerOptions = answerString.split(",");
            if (answerOptions.contains(answer)) {
                correctAnswer = true;
            }
        } else if (answerString == answer) {
            correctAnswer = true;
        }

        return answerString && answerString != "" && correctAnswer
    }
}
