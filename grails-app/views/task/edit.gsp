

<%@ page import="org.joda.time.DateTime; fi.cleanit.domain.Task" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'task.label', default: 'Task')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:javascript library="task" />
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${taskInstance}">
            <div class="errors">
                <g:renderErrors bean="${taskInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${taskInstance?.id}" />
                <g:hiddenField name="version" value="${taskInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="name"><g:message code="task.name.label" default="Name" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${taskInstance?.name}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="task.description.label" default="Description" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskInstance, field: 'description', 'errors')}">
                                    <g:textArea cols="30" rows="5" name="description" value="${taskInstance?.description}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="day"><g:message code="task.day.label" default="Day" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskInstance, field: 'day', 'errors')}">
                                    <g:datePicker name="day" noSelection="['':'']" default="none" years="${(DateTime.now().getYear()..DateTime.now().getYear()+5)}" value="${(taskInstance.dates.find({ it.day != null })?.day?.getTime())}"/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label><g:message code="task.dayOfMonth.label" default="Day Of Month" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskInstance.dates, field: 'dayOfMonth', 'errors')}">
                                    <div class="dayBoxContainer">
                                        <g:each in="${1..31}">
                                            <div id="day-${it}" class="dayBox dayOfMonth ${taskInstance.dates.dayOfMonth?.contains(it) ? 'activeBox' : ''}">${it}</div>
                                        </g:each>
                                        <g:each in="${taskInstance.dates}">
                                            <g:if test="${it.dayOfMonth}">
                                                <input type='hidden' id="dayOfMonth-${it.dayOfMonth}" name='dayOfMonth' value='${it.dayOfMonth}' />
                                            </g:if>
                                        </g:each>
                                    </div>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label><g:message code="task.dayOfWeek.label" default="Day Of Week" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskInstance, field: 'dayOfWeek', 'errors')}">
                                    <div class="dayBoxContainer">
                                        <g:each in="${1..7}">
                                            <div id="day-${it}" class="dayBox dayOfWeek ${taskInstance.dates.dayOfWeek?.contains(it) ? 'activeBox' : ''}">${it}</div>
                                        </g:each>
                                        <g:each in="${taskInstance.dates}">
                                            <g:if test="${it.dayOfWeek}">
                                                <input type='hidden' id="dayOfWeek-${it.dayOfWeek}" name='dayOfWeek' value='${it.dayOfWeek}' />
                                            </g:if>
                                        </g:each>
                                    </div>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label><g:message code="task.dayOfYear.label" default="Day Of Year" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskInstance.dates, field: 'dayOfYear', 'errors')}">
                                    <div class="dayBoxContainer">
                                        <div id="dayOfYearContainer">
                                            <g:each in="${1..365}" var="day">
                                                <date:isFirstDayOfMonth dayOfYear="${day}">
                                                    <div class="monthBox">
                                                    <div style="text-align: center; width:150px; height: 30px;">
                                                        <div class="monthLeft ${day < 31 ? 'disabled' : ''}"><img src="${g.resource(dir:'images/arrow_left.png')}" alt="<" title="Prev" /></div>
                                                        <div class="monthName"><date:printMonth dayOfYear="${day}" /></div>
                                                        <div class="monthRight ${day > 334 ? 'disabled' : ''}"><img src="${g.resource(dir:'images/arrow_right.png')}" alt="<" title="Prev" /></div>
                                                    </div>
                                                    <div class="clear"></div>
                                                </date:isFirstDayOfMonth>
                                                <div id="day-${day}" class="dayBox dayOfYear ${taskInstance.dates.dayOfYear?.contains(day) ? 'activeBox' : ''}"><date:printDay dayOfYear="${day}" /></div>
                                                <date:isLastDayOfMonth dayOfYear="${day}">
                                                    </div>
                                                </date:isLastDayOfMonth>
                                            </g:each>
                                            <g:each in="${taskInstance.dates}">
                                                <g:if test="${it.dayOfYear}">
                                                    <input type='hidden' name='dayOfYear' id="dayOfYear-${it.dayOfYear}" value='${it.dayOfYear}' />
                                                </g:if>
                                            </g:each>
                                        </div>
                                    </div>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="points"><g:message code="task.points.label" default="Minutes to accomplish task" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskInstance, field: 'points', 'errors')}">
                                    <g:textField name="points" value="${fieldValue(bean: taskInstance, field: 'points')}" />
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="priority"><g:message code="task.priority.label" default="Priority" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: taskInstance, field: 'priority', 'errors')}">
                                    <g:select name="priority" from="${fi.cleanit.enums.Priority?.values()}" keys="${fi.cleanit.enums.Priority?.values()*.name()}" value="${taskInstance?.priority?.name()}" valueMessagePrefix="priority" />
                                </td>
                            </tr>
                            </tr>

                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
