<%@ page import="io.hilo.Catalog" %>
<%@ page import="io.hilo.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()
%>

<!DOCTYPE html>
<html>
<head>
	<meta name="layout" content="admin">
	
	<title><g:message code="edit.catalog"/></title>

	<link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />	
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
	<script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
	
	<link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />
</head>
<body>
	
	
	<div class="form-outer-container">
		
		<div class="form-container">
			
			<h2><g:message code="edit.catalog"/>
				<g:link controller="catalog" action="list" class="btn btn-default pull-right"><g:message code="back.to.list"/></g:link>
				<br class="clear"/>
			</h1>
			
			<br class="clear"/>

			
			<div class="messages">
			
				<g:if test="${flash.message}">
					<div class="alert alert-info" role="status">${raw(flash.message)}</div>
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
			
			
			
			<g:form method="post" >
			
				<g:hiddenField name="id" value="${catalogInstance?.id}" />
				<g:hiddenField name="version" value="${catalogInstance?.version}" />
			
				
			
				<div class="form-row">
					
					<span class="form-label twohundred secondary"><g:message code="url"/></span>

					<span class="input-container">
						<span class="secondary">
							/${applicationService.getContextName()}/catalog/products/${catalogInstance.id} &nbsp;
						</span>

						<a href="/${applicationService.getContextName()}/catalog/products/${URLEncoder.encode("${catalogInstance.id}", "UTF-8")}" target="_blank"><g:message code="test"/></a>
						
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="form-row">
					<span class="form-label twohundred secondary"><g:message code="name"/> 
						<span class="information secondary block"><g:message code="name.unique"/></span>
					</span>
					<span class="input-container">
						<input name="name" type="text" class="form-control threefifty" value="${catalogInstance?.name}"/>
					</span>
					<br class="clear"/>
				</div>
				
				
				
				
				<div class="form-row">
					
					<span class="form-label twohundred secondary"><g:message code="location"/></span>

					<span class="input-container">
						<select name="location" class="form-control" id="location" style="width:auto">
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
				
				
				<div class="form-row">
					<span class="form-label full secondary">
						<g:message code="specifications"/>

						<g:link controller="specification" action="list" id="${catalogInstance.id}" class="information" style="display:block"><g:message code="manage.specifications"/></g:link>

						<span class="information secondary" style="display:inline-block; margin-left:15px;"><g:message code="specifications.are.used.for.message"/></span>

					</span>
					<span class="input-container sized">
						<g:if test="${specifications?.size() > 0}">
							<g:each in="${specifications}" var="specification">
								<span class="label label-default">${specification.name}</span>
							</g:each>
						</g:if>
						<g:else>
							<g:link controller="specification" action="create" id="${catalogInstance.id}" class="btn btn-default"><g:message code="add.specifications"/></g:link>
						</g:else>
					</span>
					<br class="clear"/>
				</div>
				
				
				<div class="buttons-container">
					<g:actionSubmit class="btn btn-danger" action="delete" value="${message(code:'delete')}" formnovalidate="" onclick="return confirm('Are you sure you want to delete this Catalog?');" />
				
					<g:actionSubmit class="btn btn-primary" action="update" value="${message(code:'update')}" />
				</div>
				
				
			</g:form>
		</div>
	</div>
		
<script type="text/javascript">

<g:if test="${catalogInstance.parentCatalog}">
	$(document).ready(function(){
	
		var $select = $('#location');
		var id = ${catalogInstance?.parentCatalog.id};
		
		$select.val(id)
	});
</g:if>	

</script>
			
</body>
</html>
