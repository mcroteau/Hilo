<%@ page import="io.hilo.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()
%>
${raw(applicationService.getBlogHeader())}
	


	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	<g:each in="${posts}" var="post">
		<h1>${post.title}</h1>

		<%
			def content = post.content.length() > 500 ? post.content.substring(0, 500) : post.content
			def readmore = post.content.length() > 500 ? true : false
		%>
		<div class="post-content">${raw(content)}<br/>
			<g:if test="${readmore}"><g:link action="view" id="${post.id}">Read More...</g:link></g:if>
		</div>
	</g:each>	

	<div style="width:auto;float:right;margin-bottom:20px;">
		<g:paginate 
			max="7"
			maxsteps="5"
			total="${postsTotal}"
			class="pull-right" />
	</div>

	<br class="clear"/>

${raw(applicationService.getBlogFooter())}