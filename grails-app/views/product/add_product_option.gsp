<%@ page import="io.hilo.Product" %>
<%@ page import="io.hilo.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title><g:message code="add.product.option"/></title>
	</head>
	<body>



		<div class="content">
		
			<h2><g:message code="add.product.option"/>
				<g:link action="edit" name="edit" class="btn btn-default pull-right" id="${productInstance.id}"><g:message code="back.to.product"/></g:link>
			</h2>
		
			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>
			
			
			<div style="width:400px;float:left">
			
				<g:form action="save_option" method="post">
				
					
					<input type="hidden" name="id" value="${productInstance.id}"/>
					
			
					<div class="form-row">
						<span class="form-label secondary"><g:message code="product"/></span>
						<span class="input-container">
							<span class="label label-default">${productInstance?.name}</span>
						</span>
						<br class="clear"/>
					</div>
					
				
			
					<div class="form-row">
						<span class="form-label secondary"><g:message code="name"/></span>
						<span class="input-container">
							<input type="text" name="name" class="form-control" value="${productOption?.name}" style="width:200px;display:inline-block"/>
						</span>
						<br class="clear"/>
					</div>
					
					
					<g:submitButton name="add" class="btn btn-primary" value="${message(code: "save.product.option")}" />
					
				</g:form>
				
			</div>
			
			<div class="clear"></div>
			
			<br/>
			




			<!--
			<div class="alert alert-info" role="status">
			
				<strong>What are Product Options?</strong><br/><br/>
				
				Product Options are attributes like Color, Size, Type anything that is a descriptor that has more than one option or what is called a variant within Hilo. Variants have a possible additional price. Product Options have variants.<br/></br>
				
				Example:<br/>
				Size : Small, Medium, Large $0 additional price. XLarge $3.00 additional
				
				
			</div>	
			-->

		</div>

	
	</body>
</html>
