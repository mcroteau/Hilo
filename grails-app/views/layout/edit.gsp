<%@ page import="io.hilo.Catalog" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Hilo :Store Layout</title>
		
		<script type="text/javascript" src="${resource(dir:'js/allow-tab.js')}"></script>
		
	</head>
	<body>

		<g:if test="${flash.message}">
			<div class="alert alert-info" style="margin-top:10px;">${flash.message}</div>
		</g:if>
		
		<g:if test="${flash.error}">
			<div class="alert alert-danger" style="margin-top:10px;">${flash.error}</div>
		</g:if>

				
		<div style="margin-bottom:20px">
			
			<g:form controller="layout" action="apply_catalogs" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Catalogs" class="btn btn-default pull-right" style="display:inline-block;"/>
			</g:form>
			
			<g:form controller="layout" action="apply_products" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Products" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
			
			<g:form controller="layout" action="apply_pages" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Pages" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
			
		</div>
		<br class="clear"/>
		
		<form name="update-layout" action="/${applicationService.getContextName()}/layout/update" method="post" id="layoutForm" class="form-horizontal">
		
			<g:hiddenField name="id" value="${layoutInstance?.id}" />
			<g:hiddenField name="version" value="${layoutInstance?.version}" />
			
			
			<div class="form-group" style="margin-top:30px">
				<g:actionSubmit class="btn btn-primary pull-right" action="update" value="Update Store Layout" />
				
				<g:link class="btn btn-default pull-right" controller="layout" action="index" style="display:inline-block;margin-right:10px;">Back to Layouts</g:link>
				<br class="clear"/>
			</div>
			
			
			<div class="form-row">
				<span class="form-label twohundred secondary">Name 
					<span class="information secondary block">Name must be unique</span>
				</span>
				<span class="input-container">
					<input name="name" type="text" class="form-control threefifty" value="${layoutInstance?.name}" id="name"/>
				</span>
				<br class="clear"/>
			</div>
			<div class="form-row">
				<span class="form-label twohundred secondary">Default 
					<span class="information secondary block">Selecting default will automatically unassign the other</span>
				</span>
				<span class="input-container">
					<g:checkBox name="defaultLayout" value="${layoutInstance?.defaultLayout}" checked="${layoutInstance?.defaultLayout}"/>
				</span>
				<br class="clear"/>
			</div>
			
			<hr/>

			<h3><g:message code="layout.html"/></h3>
			
			<p class="instructions"><g:message code="layout.code.message"/>
			<g:link controller="layout" action="edit_wrapper"><g:message code="edit.main.html.message"/></g:link>. <g:message code="all.that.is.needed.message"/></p>
			
			<p class="instructions"><g:message code="place.all.code.message"/> 
			<g:link controller="layout" action="tags"><g:message code="view.all.available.tags.message"/></g:link></p>  
			
			<p class="instructions"><g:message code="content.tag.is.where.message"/></p>
			
			<p class="instructions"><g:link controller="layout" action="how"><g:message code="how.layout.engine.works"/></g:link></p>

				
			<div style="border:solid 1px #ddd; background:#333;background:#384248">
				<span class="layout-code">&lt;html&gt;</span>
				<span class="layout-code">&nbsp;&nbsp;&nbsp;&lt;body&gt;</span>
				<textarea id="layout-textarea"
						name="content" 
						class="form-control">${layoutInstance?.content}</textarea>
				<span class="layout-code">&nbsp;&nbsp;&nbsp;&lt;/body&gt;</span>
				<span class="layout-code">&lt;/html&gt;</span>
			</div>

			
			<h3>Layout CSS</h3>
			<p class="instructions">Much of the storefront has css code, however your layout will need its own  supporting css code below</p>
			
			<div id="layout-code-wrapper" class="css">
				<span class="layout-code">&lt;style&gt;</span>
				<textarea id="css-textarea" 
						name="css" 
						class="form-control">${layoutInstance?.css}</textarea>
				<span class="layout-code">&lt;/style&gt;</span>
			</div>
			
			<br class="clear"/>
			
			
			<h3>Layout Javascript</h3>
			
			<p class="instructions">This section is for such things as home page carousels, sliding panes or any other Javascript goodness to add to your storefront</p>
			
			<p>Please use single quotes for javascript variables</p>
			
			<p class="instructions">This will be added at the bottom of your page layout. Warning, there are additional scripts on a per page basis that may conflict. Please check variable names on page load to ensure no duplicates or interference with base store front.</p>
			
			
			<div id="layout-code-wrapper" class="javascript">
				<span class="layout-code">&lt;script&gt;</span>
				<textarea id="javascript-textarea" 
						name="javascript" 
						class="form-control">${layoutInstance?.javascript}</textarea>
				<span class="layout-code">&lt;/script&gt;</span>
			</div>
					
			
			<div class="form-group" style="margin-top:30px">
				<g:actionSubmit class="btn btn-primary pull-right" action="update" value="Update Store Layout" />
				
				<g:actionSubmit class="btn btn-danger pull-right" action="delete" value="Delete" formnovalidate="" onclick="return confirm('Are you sure you want to delete this Layout?');" style="display:inline-block; margin-right:10px;"/>
			</div>
			
		</form>
				
		<br class="clear"/>
				
		<div style="margin-top:20px">
			
			<g:form controller="layout" action="apply_catalogs" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Catalogs" class="btn btn-default pull-right" style="display:inline-block;"/>
			</g:form>
			
			<g:form controller="layout" action="apply_products" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Products" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
			
			<g:form controller="layout" action="apply_pages" method="post" id="${layoutInstance.id}">
				<input type="submit" value="Apply to all Pages" class="btn btn-default pull-right" style="display:inline-block; margin-right:10px"/>
			</g:form>
			
		</div>
	
		<br class="clear"/>
		

<script type="text/javascript">
$(document).ready(function(){
	var $layoutForm = $("#layoutForm");
	
	$("#layout-textarea").allowTabChar();
	$("#css-textarea").allowTabChar();
	$("#javascript-textarea").allowTabChar();
	
	$(window).keypress(function(event) {
		/**
		console.log(event)
		console.log(event.which, event.ctrlKey)
		
        if((event.ctrlKey || event.metaKey) && event.which == 83) {
            // Save Function
            event.preventDefault();
            return false;
        }
		**/
	});
	
	$(window).keydown(function(event){
        if((event.ctrlKey || event.metaKey) && event.which == 83) {
			console.log("save...")
            event.preventDefault();
            $layoutForm.submit()
            return false;
        }
	})
	
});
</script>		
		
	</body>
</html>
