<%@ page import="io.hilo.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title><g:message code="store.layouts"/></title>
		
		<script type="text/javascript" src="${resource(dir:'js/allow-tab.js')}"></script>
		
		<style type="text/css">
			#css-textarea,
			#layout-textarea{
				height:350px; 
				width:100%;
				font-size:12px;
				background:#f8f8f8;
				font-family: Monaco,"MonacoRegular",monospace;
			}
		</style>
		
	</head>
	<body>

		<g:if test="${flash.message}">
			<div class="alert alert-info" style="margin-top:10px;">${flash.message}</div>
		</g:if>
		
		<g:if test="${flash.error}">
			<div class="alert alert-danger" style="margin-top:10px;">${flash.error}</div>
		</g:if>
		
		
		<div class="form-group" style="margin-top:30px">
			<g:link class="btn btn-primary pull-right" controller="layout" action="create"><g:message code="new.layout"/></g:link>

			<g:link class="pull-right" controller="layout" action="edit_support_layouts"  style="display:inline-block;margin-right:30px"><g:message code="other.pages"/></g:link>
			
			<g:link class="pull-right" controller="page" action="list"  style="display:inline-block;margin-right:30px"><g:message code="pages"/></g:link>
			
			<g:link class="pull-right" controller="catalog" action="list"  style="display:inline-block;margin-right:30px"><g:message code="catalogs"/></g:link>
			
			<g:link class="pull-right" controller="product" action="list" style="display:inline-block;margin-right:30px"><g:message code="products"/></g:link>
		</div>
		
		<h2><g:message code="store.layouts"/></h2>
		<p class="instructions"><g:link controller="layout" action="edit_wrapper"><g:message code="edit.html.wrapper.note"/></g:link></p>  
			
		
		<p><g:message code="each.page.note"/>.</p>
		
			
		<g:if test="${layouts?.size() > 0}">
			<table class="table">
				<thead>
					<tr>
						<g:sortableColumn property="name" title="${message(code:'name')}" />
						<g:sortableColumn property="defaultLayout" title="${message(code:'default')}" />
						<th></th>
					</tr>
				</thead>
				<g:each in="${layouts}" var="layoutInstance">
					<tr>
						<td>${layoutInstance.name}</td>
						<td>
							<g:if test="${layoutInstance.defaultLayout}">
								<g:message code="default"/>
							</g:if>
						</td>
						<td>
							<g:link controller="layout" action="edit" params="[id: layoutInstance?.id]" class="${layoutInstance.name}-edit"><g:message code="edit"/></g:link>
						</td>
					</tr>
				</g:each>
			</table>

			<div class="btn-group">
				<g:paginate total="${layoutInstanceTotal}" />
			</div>
		</g:if>	
		<g:else>
			<div class="alert alert-info" style="margin-top:10px;"><strong><g:message code="how.did.note"/>?</strong>
			<br/><br/>
			<g:message code="you.must.have.note"/>. <g:link controller="layout" action="create" class="btn btn-primary"><g:message code="new.layout"/></g:link>
			</div>
		</g:else>
	
		
	</body>
</html>
