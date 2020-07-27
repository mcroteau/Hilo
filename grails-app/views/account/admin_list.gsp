
<%@ 
page import="io.hilo.Account" 
page import="io.hilo.log.CatalogViewLog"
page import="io.hilo.log.ProductViewLog"
page import="io.hilo.log.PageViewLog"
page import="io.hilo.log.SearchLog"
page import="io.hilo.Transaction"
%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
		<title>Hilo :<g:message code="accounts"/></title>

		<style type="text/css">
			#newsletter-signups-link{
				text-align:right;
			}
		</style>
	</head>
	<body>

		<div id="list-account" class="content scaffold-list" role="main">
		

			<g:if test="${admin}">
				<h2 class="pull-left"><g:message code="admin.accounts"/></h2>
			</g:if>
			<g:else>
				<h2 class="pull-left"><g:message code="customer.accounts"/></h2>
			</g:else>
			

			<g:if test="${admin}">
				<div style="float:right; width:402px; text-align:right ">
					<g:form action="admin_list" class="form-horizontal">
						<input type="hidden" name="admin" value="true"/>
						<input type="text" name="query" id="searchbox" class="form-control" style="width:250px;" placeholder="${message(code: 'search.placeholder.accounts', default: 'Search name, username or email')}" value="${query}"/>
						<g:submitButton name="submit" value="${message(code:'search')}" id="search" class="btn btn-info"/>
					</g:form>
				</div>
			</g:if>
			<g:else>
				<div style="float:right; width:402px; text-align:right ">
					<g:form action="admin_list" class="form-horizontal">
						<input type="hidden" name="admin" value="false"/>
						<input type="text" name="query" id="searchbox" class="form-control" style="width:250px;" placeholder="${message(code: 'search.placeholder.accounts', default: 'Search name, username or email')}" value="${query}"/>
						<g:submitButton name="submit" value="${message(code: 'search', default: 'Search')}" id="search" class="btn btn-info"/>
					</g:form>
				</div>
			</g:else>
		
			<br class="clear"/>
			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			

			<g:if test="${admin}">
				<g:form action="admin_create">
					<input type="hidden" name="admin" value="true"/>
					<g:submitButton class="btn btn-primary pull-right" name="New Account" value="${message(code: 'create.admin.user', default: 'Create New Admin User')}"/>
				</g:form>
				<p class="information secondary">${message(code: 'admin.access.note')}</p>
			</g:if>
			<g:else>
				<g:form action="admin_create">
					<input type="hidden" name="admin" value="false"/>
					<g:submitButton class="btn btn-primary pull-right" name="Create New Account" value="${message(code: 'create.customer')}"/>
				</g:form>
				<p class="information secondary">${message(code: 'customer.access.note')}</p>
			</g:else>
			
						
			<ul class="nav nav-tabs">

				<g:if test="${admin}">
			  		<li class="inactive"><g:link uri="/account/admin_list?admin=false" class="btn btn-default"><g:message code="customers"/></g:link></li>
			  	  	<li class="active"><g:link uri="/account/admin_list?admin=true" class="btn btn-default"><g:message code="admin.users"/></g:link></li>
				</g:if>
				<g:else>
			  		<li class="active"><g:link uri="/account/admin_list?admin=false" class="btn btn-default"><g:message code="customers"/></g:link></li>
			  	  	<li class="inactive"><g:link uri="/account/admin_list?admin=true" class="btn btn-default"><g:message code="admin.users"/></g:link></li>
				</g:else>
			</ul>
			
			
			<g:if test="${accountInstanceList}">
			
			
				<table class="table">
					<thead>
						<tr>
							<!-- TODO: make sortable, may require refactoring Account hasMany to include hasMany roles/authorities -->
							<g:sortableColumn property="name" title="${message(code:'name')}" params="${[admin:admin]}"/>
							
							<g:sortableColumn property="email" title="${message(code:'email.username')}" params="${[admin:admin]}"/>

							<g:sortableColumn property="catalogViews" title="${message(code:'catalog.views')}  (${CatalogViewLog.count()})" params="${[admin:admin]}"/>

							<g:sortableColumn property="productViews" title="${message(code:'product.views')} (${ProductViewLog.count()})" params="${[admin:admin]}"/>

							<g:sortableColumn property="pageViews" title="${message(code:'page.views')} (${PageViewLog.count()})" params="${[admin:admin]}"/>						
							
							<g:sortableColumn property="searches" title="${message(code:'searches')} (${SearchLog.count()})" params="${[admin:admin]}"/>

							<g:sortableColumn property="orders" title="${message(code:'orders')} (${Transaction.count()})" />
							
							<th></th>
						</tr>
					</thead>
					<tbody>
					<g:each in="${accountInstanceList}" status="i" var="accountInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
							<td>
								<g:link action="admin_edit" id="${accountInstance.id}">
								${accountInstance.name}</g:link>
							</td>
						
							<td>${accountInstance.email}<br/>
								${accountInstance.username}
							</td>
						

							<td align="center">
								<g:link controller="account" action="account_activity" params="[id: accountInstance.id]" class="">
									${accountInstance.catalogViews}
								</g:link>
							</td>

							<td align="center">
								<g:link controller="account" action="account_activity" params="[id: accountInstance.id]" class="">
									${accountInstance.productViews}
								</g:link>
							</td>
							
							<td align="center">
								<g:link controller="account" action="account_activity" params="[id: accountInstance.id]" class="">
									${accountInstance.pageViews}
								</g:link>
							</td>
						
							<td align="center">
								<g:link controller="account" action="account_activity" params="[id: accountInstance.id]" class="">
									${accountInstance.searches}
								</g:link>
							</td>
					
							<td align="center">
								<g:link controller="account" action="admin_order_history" params="[id: accountInstance.id]" class="">${accountInstance.orders}
								</g:link>
							</td>

							<td>
								<g:link controller="account" action="admin_edit" params="[id: accountInstance.id]" class="edit-${accountInstance.id}" elementId="edit-${accountInstance.username}"><g:message code="edit"/></g:link>&nbsp;&nbsp;<br/>
								<g:link controller="account" action="account_activity" params="[id: accountInstance.id]" class=""><g:message code="activity"/></g:link>
							</td>
						
						</tr>
					</g:each>
					</tbody>
				</table>
				
				<div class="btn-group">
					<g:paginate total="${accountInstanceTotal}" 
						 	params="${[query : params.query ]}"/>
				</div>

				<div id="newsletter-signups-link">
					<g:link controller="newsletter" action="list"><g:message code="newsletter.signups"/></g:link>
					&nbsp;&nbsp;
					<g:link controller="account" action="export"><g:message code="export.all"/></g:link>
				</div>

			</g:if>
			<g:else>
				<br/>
				<p style="color:#333;padding:0px 40px;"><g:message code="no.accounts"/>...</p>
			</g:else>
		</div>
	</body>
</html>
