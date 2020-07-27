
<%@ page import="io.hilo.BlogPost" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="io.hilo.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()
%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title><g:message code="blog.posts"/></title>
	</head>
	<body>
	
	<div class="form-container">

		<h2 class="floatleft"><g:message code="blog.posts"/></h2>
		
		<br class="clear"/>


		<g:if test="${posts}">
			
			<g:link action="create" class="btn btn-primary pull-right"><g:message code="new.blog.post"/></g:link>
		
			<br class="clear"/>
			

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>

			<g:if test="${flash.error}">
				<div class="alert alert-danger" role="status">${flash.error}</div>
			</g:if>
			
			<br style="clear:both">
			
			<table class="table">
				<thead>
					<tr>
						<g:sortableColumn property="id" title="${message(code: 'id', default: 'Id')}" />
					
						<g:sortableColumn property="title" title="${message(code: 'title', default: 'Title')}" />

						<th><g:message code="layout"/></th>
						
						<th></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${posts}" status="i" var="post">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
				
						<td><g:link action="edit" id="${post.id}">${post.id}</g:link></td>

						<td><g:link action="edit" id="${post.id}">${post.title}</g:link></td>

						<td><g:link controller="layout" action="edit" id="${post.layout.id}">${post.layout.name}</g:link></td>
					
						<td><g:link action="edit" id="${post.id}" class=""><g:message code="edit"/></g:link></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${postsTotal}" />
			</div>
		</g:if>
		<g:else>
			<div class="alert alert-info"><g:message code="no.blog.posts.message"/> <g:link action="create"><g:message code="create.first.blog.post.message"/></g:link></div>
		</g:else>
	</div>
	</body>
</html>
