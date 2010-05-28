<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="calopsita" %>
<page:applyDecorator name="iteration">
<html>
<head>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/iteration.css"/>" />
	<script type="text/javascript" src="<c:url value="/javascript/jquery/selectableDraggable.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery.validate.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/javascript/iteration-show.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/javascript/project-cards.js"/>"></script>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/impromptu.css"/>"></link>
    <script type="text/javascript" src="<c:url value="/javascript/jquery/jquery-impromptu.2.5.min.js"/>"></script>
</head>

<body>
	<div id="cards">
		<calopsita:cards cards="${iterationCards}" listId="cards" description="true"/>
	</div>	  
</body>
</html>
</page:applyDecorator>