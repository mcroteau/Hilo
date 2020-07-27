
<%@ page import="io.hilo.State" %>
<%@ page import="io.hilo.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()
%>

${raw(applicationService.getHeader("Account Info"))}




<h1>Reset Successful</h1>

<p>Successfully reset password.  Please login with new password to continue</p>



${raw(applicationService.getFooter())}

