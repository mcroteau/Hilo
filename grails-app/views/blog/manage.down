<%@ page import="io.hilo.Product" %>
<%@ page import="io.hilo.ApplicationService" %>
<% def applicationService = grailsApplication.classLoader.loadClass('io.hilo.ApplicationService').newInstance()%>

<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="admin">
		<title>Blog Categories</title>

        <link rel="stylesheet" href="${resource(dir:'js/lib/ckeditor/4.4.0', file:'contents.css')}" />  
        <script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/ckeditor.js')}"></script>
    
        <script type="text/javascript" src="${resource(dir:'js/lib/ckeditor/4.4.0/styles.js')}"></script>
    
        <link rel="stylesheet" href="${resource(dir:'css', file:'admin.css')}" />


        <style type="text/css">
            #categories{
                width:225px; 
                float:left;
            }

            #add-category-container{
                width:500px;
                float:right;
                padding:15px;
                background:#efefef;
                border:solid 1px #ddd;
            }
        </style>
	</head>
	<body>

		<div class="content">
		
			<h2>Manage Blog Categories
				<g:link action="list" name="list" class="btn btn-default pull-right">Back to Posts</g:link>
			</h2>

		    <p class="information">You may select one from each specification. Each selection will be filterable via catalog selection.</p>

			<g:if test="${flash.message}">
				<div class="alert alert-info" role="status">${flash.message}</div>
			</g:if>


            <table class="table" id="categories">

                <g:each in="${categories}" var="category">
                    <tr>
                        <td>${category.name}</td>
                        <td>
                            <g:form action="remove_category" method="post" id="${category.id}">
                                <g:actionSubmit action="remove_category" class="btn btn-default" value="x" formnovalidate="" onclick="return confirm('Are you sure?');" />
                            </g:form>
                        </td>
                    </tr>
                </g:each>

            </table>


            <div id="add-category-container">
                <g:form action="add_category" method="post">
                    
                    <h3>Add Category</h3>

                    <div class="form-row">
                        <span class="form-label twohundred secondary">Name 
                            <span class="information secondary block">Name must be unique</span>
                        </span>
                        <span class="input-container">
                            <input name="name" type="text" class="form-control twohundred" value="${category?.name}" id="name"/>
                        </span>
                        <br class="clear"/>
                    </div>


                    <div class="form-row">
                        <g:textArea class="form-control ckeditor" name="description" rows="15" maxlength="65535" value="${category?.description}"/>
                        <br class="clear"/>
                    </div>

                    <input type="submit" name="save" value="Save Blog Category" class="btn btn-primary pull-right"/>

                    <br class="clear"/>

                </g:form>
            </div>
			
            <br class="clear"/>
				
		</div>

	</body>
</html>
