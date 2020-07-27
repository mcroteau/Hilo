<%@ page import="io.hilo.Catalog" %>
<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
	<title><g:message code="store.settings"/></title>
	
	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>

	<style type="text/css">
		.section{
			margin:10px 20px 30px 0px;
		}
	</style>
	
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
	
</head>
<body>

	<h2><g:message code="store.settings"/></h2>
	
	<g:if test="${flash.message}">
		<div class="alert alert-info" role="status">${flash.message}</div>
	</g:if>
	
	
	<ul class="nav nav-tabs" style="margin-bottom:30px;">
		<li class="active"><g:link uri="/configuration/settings" class="btn btn-default"><g:message code="store.settings"/></g:link></li>

		<li class="inactive"><g:link uri="/configuration/email_settings" class="btn btn-default"><g:message code="email.settings"/></g:link></li>
		
		<li class="inactive"><g:link uri="/configuration/payment_settings" class="btn btn-default"><g:message code="payment.settings"/></g:link></li>
		
		<li class="inactive"><g:link uri="/configuration/shipping_settings" class="btn btn-default"><g:message code="shipping.settings"/></g:link></li>
	</ul>
	
	
	
	<form action="save_settings" class="form-horizontal" method="post">
		
			
		<div class="form-row">
			<span class="form-label twohundred"><g:message code="company.name"/>
				<br/>
				<span class="information secondary"><g:message code="company.name.note"/>.</span>
			</span>
			<span class="input-container">
				<input type="text" class="form-control threehundred" name="storeName" value="${settings?.storeName}" style="width:370px;"/>
			</span>
			<br class="clear"/>
		</div>
		
			
		<div class="form-row">
			<span class="form-label twohundred"><g:message code="phone"/>
				<br/>
				<span class="information secondary"><g:message code="phone.note"/></span>
			</span>
			<span class="input-container">
				<input type="text" class="form-control" name="storePhone" value="${settings?.storePhone}" style="width:273px;"/>
			</span>
			<br class="clear"/>
		</div>
		
			
		<div class="form-row">
			<span class="form-label twohundred"><g:message code="company.email"/>
				<br/>
				<span class="information secondary"></span>
			</span>
			<span class="input-container">
				<input type="text" class="form-control" name="storeEmail" value="${settings?.storeEmail}" style="width:273px;"/>
			</span>
			<br class="clear"/>
		</div>





		<div class="form-row">
			<span class="form-label twohundred"><g:message code="tax.rate"/> %
				<p class="information secondary"><g:message code="tax.rate.note"/></p>
			</span>
			<span class="input-container">
				<input type="text" class="onefifty form-control" name="taxRate" value="${settings?.taxRate}"/>
			</span>
			<br class="clear"/>
		</div>


		<!--removing from store settings
		<div class="form-row">
			<span class="form-label twohundred">Shipping $
			</span>
			<span class="input-container">
				<input type="text" class="form-control onehundred" name="shipping" value="${settings?.shipping}"/>
			</span>
			<br class="clear"/>
		</div>
			-->


		<div class="form-row">
			<span class="form-label twohundred"><g:message code="meta.keywords"/>
			</span>
			<span class="input-container">
				<input type="text" class="form-control threehundred" name="keywords" value="${settings?.keywords}"/>
			</span>
			<br class="clear"/>
		</div>



		<div class="form-row">
			<span class="form-label twohundred"><g:message code="meta.description"/>
			</span>
			<span class="input-container">
				<textarea class="form-control threehundred" name="description"  style="height:100px;">${settings?.description}</textarea>
			</span>
			<br class="clear"/>
		</div>



		<div class="form-row">
			<span class="form-label twohundred"><g:message code="google.analytics"/>
				<br/>
				<span class="information secondary"><g:message code="google.analytics.settings.note"/></span>
			</span>
			<span class="input-container">
				<input type="text" class="form-control" name="googleAnalytics" value="${settings?.googleAnalytics}"/>
			</span>
			<br class="clear"/>
		</div>
		
		<div class="form-row">
			<span class="form-label twohundred"><g:message code="social.media.buttons.enabled"/>
				<br/>
				<span class="information secondary"><g:message code="social.media.buttons.enabled.note"/></span>
			</span>
			<span class="input-container">
				<input type="checkbox" ${settings?.socialMediaEnabled} name="socialMediaEnabled" id="socialMediaEnabled"/>
			</span>
			<br class="clear"/>
		</div>
		
		
		<div class="buttons-container">
			<g:link controller="configuration" action="index" class="btn btn-default"><g:message code="cancel"/></g:link>
			<g:submitButton value="${message(code:'save.settings')}" name="submit" class="btn btn-primary"/>
		</div>
		
	</form>
	
</body>
</html>