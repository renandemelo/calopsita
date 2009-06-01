<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html>
<head>
	<title><fmt:message key="project"/></title>
	
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/project.css"/>" />
    <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/impromptu.css"/>" />
    <script type="text/javascript" src="<c:url value="/javascript/jquery/jquery-impromptu.2.5.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/javascript/project-show.js"/>"></script>
</head>

<body>

<div id="tab3">
  <ul id="tabnav">
    <li class="tab1"><a href="<c:url value="/iterations/${project.id }/current/"/>">Current iteration</a></li>
    <li class="tab2"><a href="<c:url value="/iterations/${project.id }/list/"/>">Iterations</a></li>
    <li class="tab3"><a href="<c:url value="/project/${project.id }/cards/"/>">Cards</a></li>
    <li class="tab4"><a href="<c:url value="/project/${project.id }/admin/"/>">Admin</a></li>
  </ul>
  
  <div id="projects">
      <p><fmt:message key="project.name"/>: ${project.name}</p>
  </div>
  
  <div id="cards">
  	<c:if test="${not empty cards}">
  		<%@include file="../card/update.ok.jsp" %>
  	</c:if>
  </div>
  <a href="javascript:toggle('cardForm'); document.addCard.reset();"><fmt:message key="project.addCard"/></a><br/>
  
  <form id="cardForm" class="hidden" name="addCard" action="<c:url value="/card/save/"/>" method="post">
  	<input type="hidden" name="project.id" value="${project.id }" />
  	<p>
  		<label><fmt:message key="card.name"/></label>
  		<em>*</em><input type="text" name="card.name"/>
  	</p>
  	<p>
  		<label><fmt:message key="card.description"/></label>
  		<em>*</em><textarea name="card.description"></textarea>
  	</p>
      <p>
      	<input class="buttons" type="submit" value="<fmt:message key="add"/>"/>
    		<input class="buttons" type="reset" value="<fmt:message key="cancel"/>" onclick="toggle('cardForm');"/>
    	</p>
  </form>
</div>
</body>
</html>