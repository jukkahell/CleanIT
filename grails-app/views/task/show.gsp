
<%@ page import="fi.cleanit.domain.Task" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="task.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: taskInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="task.name.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: taskInstance, field: "name")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="task.description.label" default="Description" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: taskInstance, field: "description")}</td>
                            
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="task.day.label" default="Day" /></td>

                            <td valign="top" class="value">
                                <g:each in="${taskInstance.dates.day}">
                                    <g:if test="${it}">
                                        <g:formatDate date="${it}" locale="${loggedInFamily.locale}" type="datetime" format="MEDIUM" />
                                    </g:if>
                                </g:each>
                            </td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="task.dayOfMonth.label" default="Day Of Month" /></td>

                            <td valign="top" class="value">
                                <g:each in="${taskInstance.dates.dayOfMonth}">
                                    <g:if test="${it}">
                                        <date:printDayOfMonth dayOfMonth="${it}" />
                                    </g:if>
                                </g:each>
                            </td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="task.dayOfWeek.label" default="Day Of Week" /></td>

                            <td valign="top" class="value">
                                <g:each in="${taskInstance.dates.dayOfWeek}">
                                    <g:if test="${it}">
                                        <date:printDayOfWeek dayOfWeek="${it}" />
                                    </g:if>
                                </g:each>
                            </td>

                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="task.dayOfYear.label" default="Day Of Year" /></td>

                            <td valign="top" class="value">
                                <g:each in="${taskInstance.dates.dayOfYear}">
                                    <g:if test="${it}">
                                        <date:printDayOfYear dayOfYear="${it}" locale="${loggedInFamily?.locale}" />
                                    </g:if>
                                </g:each>
                            </td>

                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="task.points.label" default="Points" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: taskInstance, field: "points")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="task.priority.label" default="Priority" /></td>
                            
                            <td valign="top" class="value">${taskInstance?.priority?.encodeAsHTML()}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${taskInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
