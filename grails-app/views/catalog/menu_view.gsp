
<%@ page import="io.hilo.Catalog" %>
<%@ page import="io.hilo.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title><g:message code="catalogs.menu.view"/></title>
		<style type="text/css">
			#menu-view-container{
				padding:20px;
				background:#f8f8f8;
				border:solid 1px #ddd;
			}
			#menu-view-container ul li{
				list-style:none !important;
				padding:3px 0px;
			}
		</style>
	</head>
	<body>

		<div id="list-catalog" class="content scaffold-list" role="main">
			
			<h2 class=""><g:message code="catalogs.menu.view"/>

				<g:link controller="catalog" action="create" class="btn btn-primary pull-right" style="margin-bottom:10px;"><g:message code="new.catalog"/></g:link>
				<br class="clear"/>
				<g:link controller="catalog" action="list" class="btn btn-default pull-right" style="display:inline-block;margin-right:5px"><g:message code="back.to.list.view"/></g:link>
				<br class="clear"/>
			</h2>
			<p class="instructions"><g:message code="how.the.menu.message"/></p>
			
			<g:if test="${flash.error}">
				<div class="alert alert-danger" role="status">${flash.error}</div>
			</g:if>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<div id="menu-view-container">
				${raw(catalogMenuString)}
			</div>

		</div>	
	</body>
</html>
