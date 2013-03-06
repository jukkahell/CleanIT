
<%@ page import="fi.cleanit.domain.ActiveTask" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'activeTask.label', default: 'ActiveTask')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'activeTask.id.label', default: 'Id')}" />
                        
                            <th><g:message code="activeTask.user.label" default="User" /></th>
                        
                            <th><g:message code="activeTask.task.label" default="Task" /></th>
                        
                            <g:sortableColumn property="date" title="${message(code: 'activeTask.date.label', default: 'Date')}" />
                        
                            <g:sortableColumn property="status" title="${message(code: 'activeTask.status.label', default: 'Status')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${activeTaskInstanceList}" status="i" var="activeTaskInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${activeTaskInstance.id}">${fieldValue(bean: activeTaskInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: activeTaskInstance, field: "user")}</td>
                        
                            <td>${fieldValue(bean: activeTaskInstance, field: "task")}</td>
                        
                            <td><g:formatDate date="${activeTaskInstance.date}" /></td>
                        
                            <td>${fieldValue(bean: activeTaskInstance, field: "status")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${activeTaskInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
