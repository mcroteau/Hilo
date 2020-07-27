<%@ page import="io.hilo.ApplicationService" %>
<!-- //TODO: MOVE HOME PAGE OUT OF ApplicationService -->
<%@ page import="io.hilo.Page" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()
%>

${raw(applicationService.getHeader("Home"))}

<g:if test="${flash.message}">
	<div class="message" role="status">${flash.message}</div>
</g:if>

${raw(applicationService.getHomePage())}

${raw(applicationService.getFooter())}		