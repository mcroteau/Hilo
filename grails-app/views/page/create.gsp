<%@ page import="io.hilo.Page" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title><g:message code="create.page"/></title>
			
		<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
		<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
		<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
	
		<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
		
	</head>
	<body>

		<div class="form-outer-container">
		
		
			<div class="form-container">
			
				<h2><g:message code="create.page"/>
					<g:link controller="page" action="list" class="btn btn-default pull-right"><g:message code="back.to.list"/></g:link>
					<br class="clear"/>
				</h2>
			
				<br class="clear"/>
			
			
				<div class="messages">
			
					<g:if test="${flash.message}">
						<div class="alert alert-info" role="status">${flash.message}</div>
					</g:if>
			
					<g:if test="${flash.error}">
						<div class="alert alert-danger" role="status">${flash.error}</div>
					</g:if>
					
					<g:hasErrors bean="${pageInstance}">
						<div class="alert alert-danger">
							<ul>
								<g:eachError bean="${pageInstance}" var="error">
									<li><g:message error="${error}"/></li>
								</g:eachError>
							</ul>
						</div>
					</g:hasErrors>
				
				</div>
				
				
				
			
				<g:form action="save" >
					<div class="form-row">
						<span class="form-label full secondary"><g:message code="title"/> 
							<span class="information secondary block"><g:message code="title.unique.message"/></span>
						</span>
						<span class="input-container">
							<input name="title" type="text" class="form-control threefifty" value="${pageInstance?.title}"/>
						</span>
						<br class="clear"/>
					</div>
					
				
					<div class="form-row">
						<span class="form-label full secondary"><g:message code="content"/> 
						</span>
						<span class="input-container">
							<span class="information secondary block"><g:message code="editor.below.allows.message"/></span>
						</span>
						<br class="clear"/>
					</div>
				
				
					<div class="form-row">
						<g:textArea class="form-control ckeditor" name="content" id="content" cols="40" rows="15" maxlength="65535" value="${pageInstance?.content}"/>
						<br class="clear"/>
					</div>

					  

		 			<div class="form-row">
		 				
		 				<span class="form-label full secondary"><g:message code="layout"/></span>

		 				<span class="input-container">
							<g:select name="layout.id"
									from="${layouts}"
									value="${pageInstance?.layout?.id}"
									optionKey="id" 
									optionValue="name" 
									class="form-control"
									style="width:275px;"/>
		 				</span>
		 				<br class="clear"/>
		 			</div>
				
				
				
					<div class="buttons-container">
						<g:submitButton name="create" class="btn btn-primary" value="${message(code:'save.page')}" />
					</div>
					
				</g:form>
				
			</div>
		</div>

	</body>
</html>
