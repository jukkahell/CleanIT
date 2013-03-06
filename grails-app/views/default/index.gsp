<%@ page import="org.joda.time.DateTime; fi.cleanit.domain.Family" %>
<html>
    <head>
        <title>Welcome to CleanIt</title>
        <meta name="layout" content="main" />
        <g:javascript library="task" />
    </head>
    <body>
        <div id="ajaxErrors" class="errors hidden">
            
        </div>

        <div id="nav">
            <g:render template="/default/panels" model="${[loggedInFamily:loggedInFamily]}" />
        </div>
        <div id="pageBody">
            
            <div class="dialog">
                <g:if test="${loggedInFamily}">
                    <div class="mainMenuButtons">
                        <g:link controller="task" action="create">
                            <img class="left" src="${g.resource(dir:'images', file:'icon_add.png')}" alt="${g.message(code:'add', default:'Add')}" title="${g.message(code:'add', default:'Add')}" />
                            <span class="left mainMenuButtonText">${g.message(code:'task.new.label', default:'New Task')}</span>
                        </g:link>
                        <img class="separator" src="${g.resource(dir:'images', file:'separator.png')}" alt="|" />
                        <g:link controller="task" action="list">
                            <img class="left" src="${g.resource(dir:'images', file:'icon_list.png')}" alt="${g.message(code:'list', default:'List')}" title="${g.message(code:'list', default:'List')}" />
                            <span class="left mainMenuButtonText">${g.message(code:'task.list.label', default:'Tasks')}</span>
                        </g:link>
                        <g:if test="${params.listType != 'month'}">
                            <img class="separator" src="${g.resource(dir:'images', file:'separator.png')}" alt="|" />
                            <g:link controller="default" action="index" params="${['listType':'month']}">
                                <img class="left" src="${g.resource(dir:'images', file:'calendar.png')}" alt="${g.message(code:'add', default:'Add')}" title="${g.message(code:'add', default:'Add')}" />
                                <span class="left mainMenuButtonText">${g.message(code:'task.monthView', default:'Month view')}</span>
                            </g:link>
                        </g:if>
                        <g:if test="${params.listType == 'month' || params.listType == 'week'}">
                            <img class="separator" src="${g.resource(dir:'images', file:'separator.png')}" alt="|" />
                            <g:link controller="default" action="index">
                                <img class="left" src="${g.resource(dir:'images', file:'calendar.png')}" alt="${g.message(code:'add', default:'Add')}" title="${g.message(code:'add', default:'Add')}" />
                                <span class="left mainMenuButtonText">${g.message(code:'task.dailyView', default:'Month view')}</span>
                            </g:link>
                        </g:if>

                        <div class="clear"></div>
                    </div>

                    <g:if test="${params.listType == 'month'}">
                        <h2><g:message code="task.monthly" default="This month's tasks" />:</h2>
                        <div id="calendar">
                            <div id="weekdays">
                                <date:weekdays>
                                    <div class="weekday">${it}</div>
                                </date:weekdays>
                            </div>
                            <g:set var="daysBeforeFirstDay" value="${date.daysFromMondayUntilFirstDayInMonth() as int}" />
                            <g:set var="daysInMonth" value="${date.daysInMonth() as int}" />

                            <g:each in="${0..<daysBeforeFirstDay}"><div class="calendarDay disabledCalendarDay" style="border-top:1px solid #599E00;"></div></g:each>
                            <g:each in="${1..daysInMonth}" status="i" var="task">
                                <div class="calendarDay ${i+1 == DateTime.now().getDayOfMonth() ? 'currentCalendarDay' : ''}" style="${(i > 0 && (i+daysBeforeFirstDay+1) % 7 == 0) || i+1 == daysInMonth ? 'border-right: 1px solid #599E00;' : ''} ${i < 7-daysBeforeFirstDay ? 'border-top: 1px solid #599E00;' : ''}">
                                    <b>${i+1}</b>
                                    <ul class="tasks">
                                        <g:each in="${tasks[i+1]}" status="j" var="t">
                                            <li class="${j >= 6 ? 'hidden' : ''}">-<text:truncate text="${t.name}" max="17" /></li>
                                        </g:each>
                                    </ul>
                                    <g:if test="${tasks[i+1]?.size() > 6}">
                                        <div class="moreArrow">
                                            <img src="${g.resource(dir:'images', file:'more.png')}" alt="..." />
                                        </div>
                                    </g:if>
                                </div>
                            </g:each>
                        </div>
                    </g:if>
                    <g:else>
                        <h2><g:message code="task.daily" default="Today's tasks" />:</h2>
                        <ul id="dailyTaskList">
                            <g:each in="${tasks[0]}" status="i" var="task">
                                <li class="${task.description ? 'accordable' : ''} taskRow ${i == 0 ? 'first' : ''} ${i == taskCount-1 ? 'last' : ''}">
                                    ${task} <span style="font-size:9px">(${task.points}min)</span>
                                    <div class="buttonIcon taskDone">
                                        <img class="doneBtn" src="${g.resource(dir:'images', file:'done.png')}" alt="${g.message(code:'markAsDone', default:'Mark as done')}" title="${g.message(code:'markAsDone', default:'Mark as done')}" />
                                        <ul class="familyUsers">
                                            <g:each in="${loggedInFamily.users}" var="user" status="j">
                                                <li id="task-${task.id}-${user.id}" class="userSelection ${j == 0 ? 'first' : ''}">
                                                    ${user}
                                                </li>
                                            </g:each>
                                            <li class="last" style="cursor:default; min-width: 100px; padding-left: 15px;">
                                                <form id="taskForm-${task.id}" method="post" action="${g.createLink(controller:'task', action:'done')}">
                                                    <g:hiddenField name="id" value="${task.id}" />
                                                    <g:submitButton class="markTaskDone" id="taskDone-${task.id}" name="ok" value="OK"/>
                                                </form>
                                            </li>
                                        </ul>
                                    </div>
                                </li>
                                <g:if test="${task.description}">
                                    <li class="taskDescription">${task.description}</li>
                                </g:if>
                            </g:each>
                        </ul>
                    </g:else>
                </g:if>
                <g:else>
                    <img src="${g.resource(dir:'images', file:'hero.png')}" alt="${g.message(code:'heroAlt', default:'Start organizing your TODO list here to accomplish everything in time!')}"/>
                </g:else>
            </div>
        </div>
    </body>
</html>
