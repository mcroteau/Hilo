
<%@ page import="io.hilo.Catalog" %>
<%@ page import="io.hilo.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Hilo :<g:message code="catalogs"/></title>
	</head>
	<body>

		<div id="list-catalog" class="content scaffold-list" role="main">
			
			<h2 class=""><g:message code="catalogs"/>
				<g:link controller="catalog" action="create" class="btn btn-primary pull-right" ><g:message code="new.catalog"/></g:link>

				<g:link controller="catalog" action="menu_view" class="btn btn-default pull-right" style="display:inline-block;margin-right:5px"><g:message code="menu.view"/></g:link>
            	
            	<g:link controller="specification" action="list" class="btn btn-default pull-right" style="margin-right:5px"><g:message code="specifications"/></g:link>
            </h2>
			
			<p class="instructions" style="margin-bottom:0px;"><g:message code="click"/> <strong>"<g:message code="menu.view"/>"</strong> <g:message code="catalogs.list.message"/></p>


			<g:if test="${flash.error}">
				<br/>
				<div class="alert alert-danger" role="status">${flash.error}</div>
			</g:if>
			
			<g:if test="${flash.message}">
				<br/>
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<g:if test="${catalogsList?.size() > 0}">
				<table class="table" style="margin-top:0px !important; padding-top:0px">
					<thead>
						<tr>
							<th><g:message code="name"/></th>
							<th># <g:message code="products"/></th>
							<th></th>
							<th></th>
						</tr>
					</thead>
					<tbody>
						<g:each in="${catalogsList}" var="catalogData">
							<tr>
								<td>${catalogData.name} &nbsp;
									<span class="secondary" style="font-size:12px;color:rgba(0,0,0,0.54)">(${raw(catalogData.path)})</span>
								</td>
								<td align="center">${catalogData.productsCount}</td>
								<td>
									<g:link controller="catalog" action="manage_positions" id="${catalogData.id}"><g:message code="update.position"/></g:link>
								</td>
								<td>
									<g:link controller="catalog" action="edit" params="[id: catalogData.id]" class="${catalogData.name}-edit"><g:message code="edit"/></g:link>
								</td>
							</tr>
						</g:each>
					</tbody>
				</table>
			</g:if>
			<g:else>
				<div class="alert alert-info"><g:message code="no.catalogs"/></div>
			</g:else>
		</div>	
	</body>
</html>
