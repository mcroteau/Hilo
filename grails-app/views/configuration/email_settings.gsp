<%@ page import="io.hilo.Catalog" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title>Hilo :<g:message code="email.settings"/></title>
	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
	</style>
		
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
	
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
</head>
<body>

	<h2><g:message code="email.settings"/></h2>
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	

	<ul class="nav nav-tabs" style="margin-bottom:30px;">
		<li class="inactive"><g:link uri="/configuration/settings" class="btn btn-default"><g:message code="store.settings"/></g:link></li>

		<li class="active"><g:link uri="/configuration/email_settings" class="btn btn-default"><g:message code="email.settings"/></g:link></li>
		
		<li class="inactive"><g:link uri="/configuration/payment_settings" class="btn btn-default"><g:message code="payment.settings"/></g:link></li>
		
		<li class="inactive"><g:link uri="/configuration/shipping_settings" class="btn btn-default"><g:message code="shipping.settings"/></g:link></li>
	</ul>
	
	
	<form action="save_email_settings" class="form-horizontal">
	
		<div class="form-row">
			<span class="form-label twohundred"><g:message code="username"/></span>
			<span class="input-container">
				<input type="text" class="form-control" name="username" value="${email_settings.username}" style="width:300px"/>
			</span>
			<br class="clear"/>
		</div>

		<div class="form-row">
			<span class="form-label twohundred"><g:message code="password"/></span>
			<span class="input-container">
				<input type="password" class="form-control" name="password" value="${email_settings.password}" style="width:300px"/>
			</span>
			<br class="clear"/>
		</div>

		<div class="form-row">
			<span class="form-label twohundred"><g:message code="host"/></span>
			<span class="input-container">
				<input type="text" class="form-control" name="host" value="${email_settings.host}" style="width:300px"/>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred"><g:message code="port"/></span>
			<span class="input-container">
				<input type="text" class="form-control" name="port" value="${email_settings.port}" style="width:100px"/>
			</span>
			<br class="clear"/>
		</div>		
		
		
		<div class="form-row">
			<span class="form-label twohundred">SMTP Auth</span>
			<span class="input-container">
				<input type="checkbox" ${email_settings.auth} name="auth"/>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred">STARTTLS <g:message code="enabled"/></span>
			<span class="input-container">
				<input type="checkbox" ${email_settings.startTls} name="startTls"/>
			</span>
			<br class="clear"/>
		</div>
		
	
		<div class="form-row">
			<span class="form-label twohundred"><g:message code="admin.notification"/>
				<p class="information secondary"><g:message code="comma.separated"/></p>
			</span>
			<span class="input-container">
				<textarea class="form-control threefifty" style="height:100px;" name="adminEmail">${email_settings.adminEmail}</textarea>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred"><g:message code="support.email"/>
				<p class="information secondary"><g:message code="the.from.address"/></p>
			</span>
			<span class="input-container">
				<input type="text" class="form-control twohundred" name="supportEmail" value="${email_settings.supportEmail}"/>
			</span>
			<br class="clear"/>
		</div>
		
		
		
		<div class="buttons-container">
			<g:link controller="configuration" action="index" class="btn btn-default">Cancel</g:link>
			<g:submitButton value="Save Settings" name="submit" class="btn btn-primary"/>
		</div>
		
		
	</form>
</body>
</html>