
<%@ page import="io.hilo.Page" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="io.hilo.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()
%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Hilo :<g:message code="pages"/></title>
	</head>
	<body>
	
		<div id="list-page" class="content scaffold-list" role="main">

			<h2 class="floatleft"><g:message code="pages"/></h2>
		
			<g:link controller="page" action="create" class="btn btn-primary pull-right"><g:message code="new.custom.page"/></g:link>
		
			<br class="clear"/>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<br style="clear:both">
			
			<table class="table">
				<thead>
					<tr>
						<g:sortableColumn property="title" title="${message(code: 'title', default: 'Title')}" />
						
						<th><g:message code="url"/></th>
						
						<th><g:message code="layout"/></th>
						
						<th></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${pageInstanceList}" status="i" var="pageInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
				
						<td><g:link action="edit" id="${pageInstance.id}">${pageInstance.title}</g:link></td>
					
						<td>/${applicationService.getContextName()}/page/store_view/${pageInstance.title} &nbsp;&nbsp;
						<a href="/${applicationService.getContextName()}/page/store_view/${URLEncoder.encode("${pageInstance.title}", "UTF-8")}" class="information" target="_blank"><g:message code="test"/></a></td>
						
						<td><g:link controller="layout" action="edit" id="${pageInstance.layout.id}">${pageInstance.layout.name}</g:link></td>
					
						<td><g:link action="edit" id="${pageInstance.id}" class=""><g:message code="edit"/></g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${pageInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
