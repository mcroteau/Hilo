<%@ page import="io.hilo.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		
		<title><g:message code="create.catalog"/></title>
		
		<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
		<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
		<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
	
		<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
	</head>
	<body>
		
		
		
		<div class="form-outer-container">
		
		
			<div class="form-container">
			
				<h2><g:message code="create.catalog"/>
					<g:link controller="catalog" action="list" class="btn btn-default pull-right"><g:message code="back.to.list"/></g:link>
					<br class="clear"/>
				</h1>
			
				<br class="clear"/>
			

			
				<div class="messages">
			
					<g:if test="${flash.message}">
						<div class="alert alert-info" role="status">${flash.message}</div>
					</g:if>
			
					<g:if test="${flash.error}">
						<div class="alert alert-danger" role="status">${flash.error}</div>
					</g:if>
					
					<g:hasErrors bean="${catalogInstance}">
						<div class="alert alert-danger">
							<ul>
								<g:eachError bean="${catalogInstance}" var="error">
									<li><g:message error="${error}"/></li>
								</g:eachError>
							</ul>
						</div>
					</g:hasErrors>
				
				</div>
				
				
				
			
				<g:form action="save" >
					<div class="form-row">
						<span class="form-label twohundred secondary"><g:message code="name"/> 
							<span class="information secondary block"><g:message code="name.unique"/></span>
						</span>
						<span class="input-container">
							<input name="name" type="text" class="form-control threefifty" value="${catalogInstance?.name}" id="name"/>
						</span>
						<br class="clear"/>
					</div>
					
					

					<div class="form-row">
						
						<span class="form-label twohundred secondary"><g:message code="location"/></span>

						<span class="input-container">
							<select name="location" class="form-control" style="width:auto">
								<option value="">-- <g:message code="top.level"/> --</option>
								${raw(catalogOptions)}
							</select>
						</span>
						<br class="clear"/>
					</div>
					
						  

			 		<div class="form-row">
			 			<span class="form-label twohundred secondary"><g:message code="layout"/></span>
			 			<span class="input-container">
							<g:select name="layout.id"
									from="${layouts}"
									value="${catalogInstance?.layout?.id}"
									optionKey="id" 
									optionValue="name" 
									class="form-control"/>
			 			</span>
			 			<br class="clear"/>
			 		</div>
					
				
					<div class="form-row">
						<span class="form-label twohundred secondary"><g:message code="description"/> 
						</span>
						<span class="input-container">
							<span class="information secondary block"><g:message code="editor.below.allows.message"/></span>
						</span>
						<br class="clear"/>
					</div>
				
				
					<div class="form-row">
						<g:textArea class="form-control ckeditor" name="description" id="description" cols="40" rows="15" maxlength="65535" value="${catalogInstance?.description}"/>
						<br class="clear"/>
					</div>
				
				
				
					<div class="buttons-container">
						<g:submitButton name="create" class="btn btn-primary" value="${message(code:'save.catalog')}" />
					</div>
					
				</g:form>
				
			</div>
		</div>
		
		
		
	</body>
</html>
