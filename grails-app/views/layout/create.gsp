<%@ page import="io.hilo.Catalog" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title><g:message code="create.layout"/></title>
		
		<script type="text/javascript" src="${resource(dir:'js/allow-tab.js')}"></script>
		
	</head>
	<body>

		<g:if test="${flash.message}">
			<div class="alert alert-info" style="margin-top:10px;">${flash.message}</div>
		</g:if>
		
		<g:if test="${flash.error}">
			<div class="alert alert-danger" style="margin-top:10px;">${flash.error}</div>
		</g:if>
		
		<g:form controller="layout" action="save">
			
			<div class="form-group" style="margin-top:30px">
				<g:submitButton class="btn btn-primary pull-right" name="save" value="${message(code:'save.store.layout')}" />
				
				<g:link class="btn btn-default pull-right" controller="layout" action="index" style="display:inline-block;margin-right:10px;"><g:message code="back.to.list"/></g:link>

			<br class="clear"/>
			</div>
			
			<h2><g:message code="create.layout"/></h2>
			
			<div class="form-row">
				<span class="form-label twohundred secondary"><g:message code="name"/> 
					<span class="information secondary block"><g:message code="name.unique"/></span>
				</span>
				<span class="input-container">
					<input name="name" type="text" class="form-control threefifty" value="${layoutInstance?.name}" id="name"/>
				</span>
				<br class="clear"/>
			</div>
			<div class="form-row">
				<span class="form-label twohundred secondary"><g:message code="default"/> 
					<span class="information secondary block"><g:message code="selecting.default.message"/></span>
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
			
			<%
			def layoutContent = layoutInstance?.content
			if(!layoutContent)layoutContent = "[[CONTENT]]"
			%>
			
				
			<div style="border:solid 1px #ddd; background:#333;background:#384248">
				<span class="layout-code">&lt;html&gt;</span>
				<span class="layout-code">&nbsp;&nbsp;&nbsp;&lt;body&gt;</span>
				<textarea id="layout-textarea"
						name="content" 
						class="form-control">${layoutContent}</textarea>
				<span class="layout-code">&nbsp;&nbsp;&nbsp;&lt;/body&gt;</span>
				<span class="layout-code">&lt;/html&gt;</span>
			</div>
			
			<h3><g:message code="layout.css"/></h3>
			<p class="instructions"><g:message code="much.of.css.code.message"/></p>
			
			
			<div id="layout-code-wrapper" class="css">
				<span class="layout-code">&lt;style&gt;</span>
				<textarea id="css-textarea" 
						name="css" 
						class="form-control">${layoutInstance?.css}</textarea>
				<span class="layout-code">&lt;/style&gt;</span>
			</div>
			
			
			<br class="clear"/>
			
			
			<h3><g:message code="layout.javascript"/></h3>
			
			<p class="instructions"><g:message code="this.section.message"/></p>
			
			<p><g:message code="please.use.single.quotes.message"/></p>
			
			<p class="instructions"><g:message code="this.will.be.added.at.bottom.message"/></p>
			

			<div id="layout-code-wrapper" class="javascript">
				<span class="layout-code">&lt;script&gt;</span>
				<textarea id="javascript-textarea" 
						name="javascript" 
						class="form-control">${layoutInstance?.javascript}</textarea>
				<span class="layout-code">&lt;/script&gt;</span>
			</div>
					
			
			<div class="form-group" style="margin-top:30px">
				<g:submitButton class="btn btn-primary pull-right" name="save" value="${message(code:'save.store.layout')}" />
			</div>
			
		</g:form>
		
		

<script type="text/javascript">
$(document).ready(function(){
	$("#layout-textarea").allowTabChar();
	$("#css-textarea").allowTabChar();
	$("#javascript-textarea").allowTabChar();
});
</script>		
		
	</body>
</html>
