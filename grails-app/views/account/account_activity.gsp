
<%@ page import="io.hilo.Account" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
		<title><g:message code="account.activity"/></title>
	</head>
	<body>
<style type="text/css">
#activity-stats-container{
	padding:0px;
	margin-top:0px;
}
.activity-stats{
	min-height: 176px;
}
.activity-stats h4{
	padding-top:0px;
	margin-top:0px;
}
.activity-stat-row{
	padding:2px 0px;
}
.details-link{
	font-size:12px;
}
</style>
		<div id="list-account" class="content scaffold-list" role="main">
		

			<g:link controller="account" action="admin_list" class="btn btn-default pull-right"><g:message code="back.to.list"/></g:link>
			
			<div style="width:10px; display:inline-block; border:solid 1px #fff;" class="pull-right"></div>

			<g:link controller="account" action="admin_order_history" id="${accountInstance.id}" class="btn btn-default pull-right"><g:message code="order.history"/></g:link>


			<h2>
				<g:if test="${accountInstance.name}">
					<strong>${accountInstance?.name}</strong>: 
				</g:if>
				${accountInstance.username}
				:
				<strong>${accountInstance.transactions.size()}</strong>&nbsp;<g:message code="orders"/>
			</h2>


			<p class="information secondary" style="margin-bottom:0px;">${accountInstance.username}'s <g:message code="overall.activity"/></p>

			<br class="clear" style="display:inline-block;line-height:1.0em;padding:0px;margin:0px;"/>
			

			
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>


			<div id="activity-stats-container">
				
				<div class="activity-stats">
					<h4 class="secondary"><g:message code="products.viewed"/></h4>

					<g:if test="${productViewStats}">
						<g:each in="${productViewStats}" var="statistic" status="i">
							<g:if test="${i <= 4}">
								<div class="activity-stat-row">
									<span class="activity-stat-title">${statistic.value.product}</span>
									<span class="activity-stat-value">${statistic.value.count}</span>
									<br class="clear"/>
								</div>
							</g:if>
						</g:each>
						<g:link controller="account" action="product_activity" id="${accountInstance.id}" class="pull-right details-link"><g:message code="details"/></g:link>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							<g:message code="no.activity.data.available"/>
						</div>
					</g:else>
				</div>
			
				
				<div class="activity-stats">
					<h4 class="secondary"><g:message code="catalogs.viewed"/></h4>
					
					<g:if test="${catalogViewStats}">
						<g:each in="${catalogViewStats}" var="statistic" status="i">
							<g:if test="${i <= 4}">
								<div class="activity-stat-row">
									<span class="activity-stat-title">${statistic.value.catalog}</span>
									<span class="activity-stat-value">${statistic.value.count}</span>
									<br class="clear"/>
								</div>
							</g:if>
						</g:each>
						<g:link controller="account" action="catalog_activity" id="${accountInstance.id}" class="pull-right details-link"><g:message code="details"/></g:link>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							<g:message code="no.activity.data.available"/>
						</div>
					</g:else>
				</div>


				
				<div class="activity-stats">
					<h4 class="secondary"><g:message code="pages.viewed"/></h4>

					<g:if test="${pageViewStats}">
						<g:each in="${pageViewStats}" var="statistic" status="i">
							<g:if test="${i <= 4}">
								<div class="activity-stat-row">
									<span class="activity-stat-title">${statistic.value.page}</span>
									<span class="activity-stat-value">${statistic.value.count}</span>
									<br class="clear"/>
								</div>
							</g:if>
						</g:each>
						<g:link controller="account" action="page_activity" id="${accountInstance.id}" class="pull-right details-link"><g:message code="details"/></g:link>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							<g:message code="no.activity.data.available"/>
						</div>
					</g:else>
				</div>



				<div class="activity-stats">
					<h4 class="secondary"><g:message code="searches"/></h4>

					
					<g:if test="${searchQueryStats}">
						<g:each in="${searchQueryStats}" var="statistic" status="i">
							<g:if test="${i <= 4}">
								<div class="activity-stat-row">
									<span class="activity-stat-title">${statistic.key}</span>
									<span class="activity-stat-value">${statistic.value.count}</span>
									<br class="clear"/>
								</div>
							</g:if>
						</g:each>
						<g:link controller="account" action="search_activity" id="${accountInstance.id}" class="pull-right details-link"><g:message code="details"/></g:link>
					</g:if>
					<g:else>
						<div style="margin:30px auto 40px auto" class="hint">
							<g:message code="no.activity.data.available"/>
						</div>
					</g:else>
				</div>	

				<br class="clear"/>
			</div>
			
			
			
		</div>
	</body>
</html>
