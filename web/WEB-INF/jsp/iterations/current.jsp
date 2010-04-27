<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="calopsita" %>
<page:applyDecorator name="current-iteration">
<html>
<head>
<script type="text/javascript" src="<c:url value="/javascript/iteration-show.js"/>"></script>
	  <script type="text/javascript" src="<c:url value="/javascript/jquery/selectableDraggable.js"/>"></script>
	  <script type="text/javascript" src="<c:url value="/javascript/jquery/jquery.validate.min.js"/>"></script>
	  <link rel="stylesheet" type="text/css" media="all" href="<c:url value="/css/iteration.css"/>" />
	  
	  <script type="text/javascript">
	  function confirmation(project_id,iteration_id,card_id) {

		  	
					
			$("#dialog-confirm").dialog({
				resizable: false,
				height:140,
				modal: true,
				buttons: {
					Ok: function() {
						location.href = '/calopsita/projects/' + project_id + '/iterations/' + iteration_id + '/card/' + card_id + '/cardOwner/';
					},
					Cancel: function() {
						$(this).dialog('close');
					}
				}
			});
		}	  
	    </script>
	  
</head>
<body>
<div id="dialog-confirm" title="Empty the recycle bin?" style="display:none;">
	<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Você já é responsável por outros cartões ainda não finalizados. Deseja ser responsável por este cartão mesmo assim?</p>
</div>

<div id="tab1">
<c:if test="${not empty iteration}">

	<div id="planning">
		<div class="help">
			<p><fmt:message key="help.changeCardsStatus"/></p>
		</div>
		<div id="todo_cards" class="selectable cards column">
			<h2><fmt:message key="toDo"/></h2>
			<calopsita:cards cards="${iteration.todoCards}" classes="board" listId="todo_list" />
		</div>
		<div id="done_cards" class="selectable cards column">
			<h2><fmt:message key="done"/></h2>
			<calopsita:cards cards="${iteration.doneCards}" classes="board" listId="done_list" />
		</div>
	</div>
</c:if>
<c:if test="${empty iteration}">
<p>There is no current iteration</p>
</c:if>

</div>
</body>
</html>
</page:applyDecorator>