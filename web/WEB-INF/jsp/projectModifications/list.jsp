<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>

<page:applyDecorator name="modifications">
<html>
<head>
	<title>${project.name}</title>
</head>

<body>
		<div id="lastModifications">
			<ul class="pretty">				
				<c:forEach items="${modifications}" var="modification">
					<li>${modification.description}</li>
				</c:forEach>
			</ul>
		</div>
</body>
</html>
</page:applyDecorator>