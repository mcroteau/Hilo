<%@ page import="io.hilo.Account" %>
<%@ page import="io.hilo.common.RoleName" %>
<%@ page import="io.hilo.State" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()%>


<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
		<title><g:message code="edit.account"/></title>

	</head>
	<body>


	<div class="form-outer-container">
	
	
		<div class="form-container">
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>

			<h2><g:message code="edit.account"/>			

				<g:link controller="account" action="admin_list" params="[admin:false]" class="btn btn-default pull-right"><g:message code="back.to.list"/></g:link>
				
				<div style="display:inline-block;width:10px;height:10px;" class="pull-right"></div>

				<g:link controller="account" action="account_activity" id="${accountInstance?.id}" class="btn btn-default pull-right"><g:message code="activity"/></g:link>

				<br class="clear"/>
			</h2>

			<br class="clear"/>
			
			
			
			<g:hasErrors bean="${accountInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${accountInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			
			
			<g:form method="post" class="form-horizontal" >
			
				<g:hiddenField name="id" value="${accountInstance?.id}" />
				
				
				<g:if test="${accountInstance.username == 'admin'}">
					<div class="form-row">
						<span class="form-label full">UUID</span>
						<span class="input-container">
							<g:textField type="uuid" name="uuid" value="${accountInstance?.uuid}" class="form-control twofifty"/>
						</span>
						<span class="information"><g:message code="manually.update.uuid.message"/></span>
						<br class="clear"/>
					</div>
				</g:if>

				<div class="form-row">
					<span class="form-label full"><g:message code="username"/></span>
					<span class="input-container">
						${accountInstance.username}
					</span>
					<br class="clear"/>
				</div>
				

				<div class="form-row">
					<span class="form-label full"><g:message code="email"/></span>
					<span class="input-container">
						<g:textField type="email" name="email" required="" value="${accountInstance?.email}" class="form-control twofifty"/>
					</span>
					<br class="clear"/>
				</div>
				


				<div class="form-row">
					<span class="form-label full"><g:message code="name"/></span>
					<span class="input-container">
						<g:textField class="form-control twohundred"  name="name" value="${accountInstance?.name}"/>
					</span>
					<br class="clear"/>
				</div>


				<div class="form-row">
					<span class="form-label full"><g:message code="address1"/></span>
					<span class="input-container">
						<g:textField class="threehundred form-control"  name="address1" value="${accountInstance?.address1}"/>
					</span>
					<br class="clear"/>
				</div>
				
				
				
				<div class="form-row ">
					<span class="form-label full"><g:message code="address2"/></span>
					<span class="input-container">
						<g:textField class="threehundred form-control"  name="address2" value="${accountInstance?.address2}"/>
					</span>
					<br class="clear"/>
				</div>




				<div class="form-row">
					<span class="form-label full"><g:message code="city"/></span>
					<span class="input-container">
						<g:textField class="twotwentyfive form-control"  name="city" value="${accountInstance?.city}"/>
					</span>
					<br class="clear"/>
				</div>
				
				
				
				
				<div class="form-row">
				  	<label for="country" class="form-label full"><g:message code="country"/></label>
					<span class="input-container">
						<g:select name="country.id"
								from="${countries}"
								value="${accountInstance?.country?.id}"
								optionKey="id" 
								optionValue="name"
								class="form-control"
								id="countrySelect"/>
					</span>
					<br class="clear"/>
				</div>
				
				
				
				
				<div class="form-row">
					<span class="form-label full"><g:message code="state"/></span>
					<span class="input-container">	
						<g:select name="state.id"
								from="${State.list()}"
								value="${accountInstance?.state?.id}"
								optionKey="id" 
								optionValue="name" 
								class="form-control"
								id="stateSelect"/>
					</span>
					<br class="clear"/>	
				</div>
				
				
				

				<div class="form-row">
					<span class="form-label full"><g:message code="zip"/></span>
					<span class="input-container">
						<g:textField class="onefifty form-control"  name="zip" value="${accountInstance?.zip}"/>
					</span>
					<br class="clear"/>
				</div>
				


				<div class="form-row">
					<span class="form-label full"><g:message code="phone"/></span>
					<span class="input-container">
						<g:textField class="twofifty form-control"  name="phone" value="${accountInstance?.phone}"/>
					</span>
					<br class="clear"/>
				</div>

				

				<div class="form-row">
					<span class="form-label full"><g:message code="is.administrator"/></span>
					<span class="input-container">
						
						<g:checkBox name="admin" 
						value="${accountInstance.hasAdminRole}" checked="${accountInstance.hasAdminRole}"/>
						
						<span class="information"><g:message code="all.accounts.message"/></span>		

					</span>
					<br class="clear"/>
				</div>	
				
				

				<div class="buttons-container">
				
					<g:link action="admin_edit_password" class="btn btn-default" id="${accountInstance.id}" style="margin-right:5px;"><g:message code="change.password"/></g:link>
					
					<g:if test="${accountInstance.username != 'admin'}">
						<g:actionSubmit class="btn btn-danger" action="admin_delete" value="${message(code:'delete')}" formnovalidate="" onclick="return confirm('Are you sure?');" />
					</g:if>
					
					<g:actionSubmit class="btn btn-primary" action="admin_update" value="${message(code:'update')}" />
					
		
				</div>
				
			</g:form>

			<div>
				<g:if test="${accountInstance.emailOptIn == false}">
					<g:form controller="newsletter" action="admin_opt_in" method="post" id="${accountInstance.id}">
						<input type="submit" value="${message(code:'opt.in.emails')}" class="btn btn-default"/>
					</g:form>
				</g:if>
				<g:else>
					<g:form controller="newsletter" action="admin_opt_out" method="post" id="${accountInstance.id}">
						<input type="submit" value="${message(code:'opt.out.emails')}" class="btn btn-default"/>
					</g:form>
				</g:else>
			</div>
	
		</div>
	
	</div>	
	
	
	<script type="text/javascript" src="${resource(dir:'js/country_states.js')}"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){
			//TODO:object orient this
			countryStatesInit("${applicationService.getContextName()}", ${accountInstance?.state?.id})
		})
	</script>
	
	</body>
</html>
