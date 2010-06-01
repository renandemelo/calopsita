<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="calopsita" %>
<page:applyDecorator name="cards">
<html>
<head>
	<title><fmt:message key="project.priorization"/></title>
	<script type="text/javascript" src="<c:url value="/javascript/jquery/selectableDraggable.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery.form.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/javascript/jquery/jquery.validate.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/javascript/card-prioritization.js"/>"></script>
	<link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/iteration.css"/>" />
	<script type="text/javascript">
		initialize('<fmt:message key="infinityPriority"/>', '<c:url value="/projects/${project.id }/prioritize/"/>');
		selected = 1;
	</script>
	<script type="text/javascript" src="<c:url value="/javascript/project-cards.js"/>"></script>
</head>

<body>
<div id="tab3">
	<div class="help">
		<p><fmt:message key="help.prioritization" /></p>
	</div>
	
	<div id="prioritizationForm">
        <div id="left" class="column">
          <div id="board">
            <c:forEach items="${cards}" varStatus="s" var="currentPriority" >
              <h2 id="title_${s.index }" class="title"><fmt:message key="priority"/> ${s.index }</h2>
              <calopsita:cards cards="${currentPriority}" classes="board" listId="level_${s.index}" title="Priority ${s.index }" priority="${s.index }">
                 <input type="hidden" name="cards[#].id" value="${card.id}" />
                 <input class="priority" type="hidden" name="cards[#].priority" value="${s.index }" />
              </calopsita:cards>
            </c:forEach>
          </div>
          <h2 class="title"><fmt:message key="newPriorityLevel"/></h2>
          <ul id="lowerPriority" class="board" title="New Priority Level"></ul>
      
        </div>
        <div id="right" class="column">
        </div>
	</div>
    
	<form id="undoForm" action="<c:url value="/projects/${project.id }/prioritize/"/>" method="POST">
		<c:forEach items="${cards}" var="currentPriority" varStatus="s">
			<c:forEach items="${currentPriority}" var="card">
				<div class="undo hidden">
					<input type="hidden" name="cards[#].id" value="${card.id}" />
					<input class="priority" type="hidden" name="cards[#].priority" value="${s.index}" />
				</div>
			</c:forEach>
		</c:forEach>
		<input type="submit" value="<fmt:message key='undo'/>"/>		
	</form>
	<a href="<c:url value="/projects/${project.id }/cards/"/>"><fmt:message key="back"/></a>
</div>
</body>
</html>
</page:applyDecorator>