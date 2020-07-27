<%@ 
page import="grails.util.Environment" 
page import="io.hilo.Account"
page import="io.hilo.Product"
page import="io.hilo.Catalog"
page import="io.hilo.ProductOption"
page import="io.hilo.ProductSpecification"
page import="io.hilo.ShoppingCart"
page import="io.hilo.Transaction"
page import="io.hilo.Page"
page import="io.hilo.Upload"
page import="io.hilo.log.CatalogViewLog"
page import="io.hilo.log.ProductViewLog"
page import="io.hilo.log.PageViewLog"
page import="io.hilo.log.SearchLog"
%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'catalog.label', default: 'Catalog')}" />
		<title><g:message code="import.export" /></title>
	</head>
	<body>
		
		<style type="text/css">
			.btn{
				margin-bottom:20px;
				width:175px;
			}
			.borderless td, .borderless th {
			    border: none !important;
			}

		</style>
		
		<div id="configuration" class="content scaffold-create" role="main">
		
			<h2><g:message code="import.export" /></h2>
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			<g:hasErrors bean="${catalogInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${catalogInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
	
			<br/>
			
			<pre style="float:left; width:300px; font-family:Arial !important; padding:0px 10px 0px 10px !important" class="">
				<table class="table borderless" style="width:260px; margin:-5px 30px -20px 10px !important;">
					<tr>
						<td><g:message code="accounts"/></td>
						<td><strong>${Account.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="catalogs"/></td>
						<td><strong>${Catalog.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="products" /></td>
						<td><strong>${Product.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="product.options"/></td>
						<td><strong>${ProductOption.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="product.specifications"/></td>
						<td><strong>${ProductSpecification.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="shopping.carts"/></td>
						<td><strong>${ShoppingCart.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="orders"/></td>
						<td><strong>${Transaction.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="pages"/></td>
						<td><strong>${Page.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="uploads"/></td>
						<td><strong>${Upload.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="catalog.view.logs"/></td>
						<td><strong>${CatalogViewLog.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="product.view.logs"/></td>
						<td><strong>${ProductViewLog.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="page.view.logs"/></td>
						<td><strong>${PageViewLog.count()}</strong></td>
					</tr>
					<tr>
						<td><g:message code="search.logs"/></td>
						<td><strong>${SearchLog.count()}</strong></td>
					</tr>
				</table>
			</pre>
			
			
			
			<div style="float:left; width:300px; margin-left:57px;">

				<g:link uri="/configuration/import_products_view" class="btn btn-default">
					<span class="glyphicon glyphicon-import"></span>
					<g:message code="import.products"/>
				</g:link>
				<br/>
				
				<g:link uri="/export/view_export" class="btn btn-default">
					<span class="glyphicon glyphicon-export"></span>
					<g:message code="export.data"/>
				</g:link>
				<br/>
				
				<g:link uri="/import/view_import" class="btn btn-default">
					<span class="glyphicon glyphicon-import"></span>
					Import Data
				</g:link>
				<br/>
				
				<g:link uri="/configuration/uploads" class="btn btn-default">
					<span class="glyphicon glyphicon-upload"></span>
					<g:message code="uploads"/>
				</g:link>
				<br/>
	        	
	        	<g:if env="development">
	        		<br/>
	        		<br/>
					<g:link uri="/development/generate_development_data" class="btn btn-default">
						<span class="glyphicon glyphicon-retweet"></span>
						<g:message code="generate.data"/>
					</g:link>
					<p class="information secondary"><g:message code="generate.data.note"/></p>
					<br/>
					<g:link uri="/development/generate_customers" class="btn btn-default">
						<span class="glyphicon glyphicon-retweet"></span>
						<g:message code="mock.customers"/>
					</g:link>
					<p class="information secondary"><g:message code="customers.generate.note"/> <a href="http://openabc.xyz" title="Seal CRM">Seal CRM (BDO)</a> <g:message code="customers.generate.note.continued"/></p>
					<br/>
				</g:if>
				
			</div>
			
			
			<br class="clear"/>
			
			<div class="align-right">
				<g:link uri="/configuration/manage_key" class="pull-right">
					Manage Data Key for Exporting to Seal CRM
				</g:link>
				<br/>
				<a href="http://openabc.xyz" title="Seal CRM"><g:message code="more.about.seal"/></a>
			</div>

			<br class="clear"/>
			
		</div>
	</body>
</html>

						
	
	