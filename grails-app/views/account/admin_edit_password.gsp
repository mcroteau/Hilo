<%@ page import="io.hilo.Account" %>
<%@ page import="io.hilo.common.RoleName" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<title><g:message code="change.password"/></title>
</head>
<body>

	
	<div class="form-outer-container">
	
	
		<div class="form-container">
			
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>

			
			<g:form method="post">
			
				<h2><g:message code="change.password"/>
					<g:link controller="account" action="admin_edit" id="${accountInstance?.id}" class="btn btn-default pull-right"><g:message code="back.to.account"/></g:link>
				</h2>

				
				<br class="clear"/>
				
				
				<div class="form-row">
					<span class="form-label full"><g:message code="username"/></span>
					<span class="input-container">${accountInstance.username}
					</span>
					<br class="clear"/>
				</div>
				

				
				<div class="form-row">
					<span class="form-label full"><g:message code="new.password"/></span>		
					<span class="input-container">
						<input type="password" class="form-control"  name="password" value=""/>			
					</span>
					<br class="clear"/>
				</div>


				<div class="buttons-container">
					<g:hiddenField name="id" value="${accountInstance?.id}" />
					<g:actionSubmit class="btn btn-primary" action="admin_update_password" value="${message(code:'update.password')}" />		
				</div>
				
			</g:form>
			
		</div>
		
	</div>
	
</body>
</html>
