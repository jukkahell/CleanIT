<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css/common',file:'jquery-ui.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:javascript library="jQuery" />
        <g:javascript library="jquery-ui.min" />
        <g:javascript library="jquery.ui.datepicker-fi" />

        <g:layoutHead />
    </head>
    <body>
        <div id="spinner" class="spinner" style="display:none;">
            <img src="${resource(dir:'images',file:'spinner.gif')}" alt="${message(code:'spinner.alt',default:'Loading...')}" />
        </div>
        <div id="logo"><a href="${g.createLink(controller:'default', action:'index')}"><img src="${resource(dir:'images',file:'logo.png')}" alt="CleanIt" border="0" /></a></div>
        <g:layoutBody />
    </body>
</html>