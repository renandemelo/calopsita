<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>

<page:applyDecorator name="admin">
	<div id="cardTypesList">
	<ul id="cardTypes" class="pretty">
		<c:forEach items="${cardTypeList}" var="type">
			<li>${type.name }
			<div class="action" id="${type.name }"><a
				class="ui-icon ui-icon-pencil" name="edit ${type.name }"
				title="<fmt:message key="edit"/>"
				href="<c:url value="/projects/${type.project.id}/cardTypes/${type.id}/"/>"></a>
			<a class="ui-icon ui-icon-closethick" name="delete ${type.name }"
				href="javascript:void(0)" title="<fmt:message key="delete"/>"></a>
			</div>
			</li>
		</c:forEach>
	</ul>
	</div>

	<div class="clear">
	<hr>
	<a href="<c:url value="/projects/${project.id }/cardTypes/" />"><fmt:message
		key="add.cardType" /></a> <c:choose>
		<c:when test="${cardType} != null">
			<c:url var="urlActionForm"
				value="/projects/${project.id }/cardTypes/${cardType.id}" />
			<c:otherwise>
				<c:url var="urlActionForm"
					value="/projects/${project.id }/cardTypes/" />
			</c:otherwise>
		</c:when>
	</c:choose>

	<form id="formCard" action="<c:out value="${urlActionForm}" />"
		method="post"><input type="hidden" name="cardtype.id"
		value="<c:out value="${cardType.id}" />" />
	<p><label><fmt:message key="card.name" /></label> <em>*</em><input
		type="text" name="cardType.name"
		value="<c:out value="${cardType.name}" />" /></p>

	<fieldset title="<fmt:message key="gadgets" />"><legend><fmt:message
		key="gadgets" /></legend> <c:forEach items="${gadgets}" var="gadget"
		varStatus="s">
		<input type="checkbox" name="cardType.gadgets[${s.index }]"
			value="${gadget }" id="${gadget }"
			${fn:contains(cardTypeGadgets, gadget)? 'checked="checked" ':'' }/>
		<fmt:message key="${gadget}" />
	</c:forEach></fieldset>
	<p><input class="buttons" type="submit"
		value="<fmt:message key="save"/>" /> <input class="buttons"
		type="reset" value="<fmt:message key="cancel"/>" /></p>
	</form>
	<div id="success_message" style="color: blue;">
	<c:out value="${message }"></c:out>
	</div>
	</div>

</page:applyDecorator>