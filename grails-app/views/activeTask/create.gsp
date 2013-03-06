

<%@ page import="fi.cleanit.domain.ActiveTask" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'activeTask.label', default: 'ActiveTask')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${activeTaskInstance}">
            <div class="errors">
                <g:renderErrors bean="${activeTaskInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="user"><g:message code="activeTask.user.label" default="User" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activeTaskInstance, field: 'user', 'errors')}">
                                    <g:select name="user.id" from="${fi.cleanit.domain.User.list()}" optionKey="id" value="${activeTaskInstance?.user?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="task"><g:message code="activeTask.task.label" default="Task" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activeTaskInstance, field: 'task', 'errors')}">
                                    <g:select name="task.id" from="${fi.cleanit.domain.Task.list()}" optionKey="id" value="${activeTaskInstance?.task?.id}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="date"><g:message code="activeTask.date.label" default="Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activeTaskInstance, field: 'date', 'errors')}">
                                    <g:datePicker name="date" precision="day" value="${activeTaskInstance?.date}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="status"><g:message code="activeTask.status.label" default="Status" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activeTaskInstance, field: 'status', 'errors')}">
                                    <g:select name="status" from="${fi.cleanit.enums.ActiveTaskStatus?.values()}" keys="${fi.cleanit.enums.ActiveTaskStatus?.values()*.name()}" value="${activeTaskInstance?.status?.name()}"  />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
