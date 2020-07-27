<%@ page import="io.hilo.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()
%>
${raw(applicationService.getBlogHeader(postInstance))}
	
	<style type="text/css">
		.post-content{
			overflow:hidden;
		}
	</style>

	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	<h1>${postInstance.title}</h1>
	<div class="post-content">${raw(postInstance.content)}</div>

${raw(applicationService.getBlogFooter())}