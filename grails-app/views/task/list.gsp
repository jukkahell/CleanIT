
<%@ page import="fi.cleanit.domain.Task" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'task.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="name" title="${message(code: 'task.name.label', default: 'Name')}" />
                        
                            <g:sortableColumn property="description" title="${message(code: 'task.description.label', default: 'Description')}" />

                            <th>${message(code: 'task.day.label', default: 'Day')}</th>

                            <th>${message(code: 'task.dayOfYear.label', default: 'Day Of Year')}</th>
                        
                            <th>${message(code: 'task.dayOfMonth.label', default: 'Day Of Month')}</th>
                        
                            <th>${message(code: 'task.dayOfWeek.label', default: 'Day Of Week')}</th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${taskInstanceList}" status="i" var="taskInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${taskInstance.id}">${fieldValue(bean: taskInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: taskInstance, field: "name")}</td>
                        
                            <td>${fieldValue(bean: taskInstance, field: "description")}</td>

                            <td>
                                <g:each in="${taskInstance.dates.day}">
                                    <g:if test="${it}">
                                        <g:formatDate date="${it}" locale="${loggedInFamily.locale}" type="datetime" format="MEDIUM" />
                                    </g:if>
                                </g:each>
                            </td>

                            <td>
                                <g:each in="${taskInstance.dates.dayOfYear}">
                                    <g:if test="${it}">
                                        <date:printDayOfYear dayOfYear="${it}" locale="${loggedInFamily?.locale}" />
                                    </g:if>
                                </g:each>
                            </td>
                        
                            <td>
                                <g:each in="${taskInstance.dates.dayOfMonth}">
                                    <g:if test="${it}">
                                        <date:printDayOfMonth dayOfMonth="${it}" />
                                    </g:if>
                                </g:each>
                            </td>
                        
                            <td>
                                <g:each in="${taskInstance.dates.dayOfWeek}">
                                    <g:if test="${it}">
                                        <date:printDayOfWeek dayOfWeek="${it}" />
                                    </g:if>
                                </g:each>
                            </td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${taskInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
