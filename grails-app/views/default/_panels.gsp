<%--
  Created by IntelliJ IDEA.
  User: Hell
  Date: 16.9.2011
  Time: 3:13
  To change this template use File | Settings | File Templates.
--%>

<g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
</g:if>

<g:if test="${loggedInFamily}">
    <div class="homePagePanel">
        <div class="panelTop"></div>

        <div class="panelBody">
            <h1><g:message code="loggedIn" default="Welcome"/> <g:link controller="family" action="show" id="${loggedInFamily.id}">${loggedInFamily}</g:link></h1>
            <g:link controller="family" action="logout"><g:message code="logout" default="Logout" /></g:link>
        </div>

        <div class="panelBtm"></div>
    </div>
    
    <div class="homePagePanel">
        <div class="panelTop"></div>

        <div class="panelBody">
            <h1><g:message code="family.timeSpent" default="Time spent" /></h1>
            <ul>
                <g:each in="${loggedInFamily.users.sort({ it.points }).reverse()}" var="user">
                    <li><g:link controller="user" action="show" id="${user.id}">${user}</g:link> - ${user.points}</li>
                </g:each>
            </ul>
            <g:link controller="user" action="create">
                <img class="left" src="${g.resource(dir:'images', file:'icon_addUser.png')}" alt="" />
                <span style="float:left; margin: 3px 0 0 5px;"><g:message code="addUser" default="Add new member" /></span>
            </g:link>
            <br />
        </div>

        <div class="panelBtm"></div>
    </div>
</g:if>
<g:else>
    <div class="homePagePanel">
        <div class="panelTop"></div>

        <div class="panelBody">
            <h1><g:message code="login" default="Login"/></h1>
            <g:form controller="family" action="login">
                <g:message code="user.email.label" default="Email"/>:<br/>
                <input type="text" name="email" value=""/><br/>
                <g:message code="password" default="Password"/>:<br/>
                <input type="password" name="password" value=""/>
                <input type="submit" value="${message(code: 'login', default: 'Login')}"/>
            </g:form>
            <g:link controller="family" action="create"><g:message code="family.register" default="Register" /></g:link>
        </div>

        <div class="panelBtm"></div>
    </div>
</g:else>