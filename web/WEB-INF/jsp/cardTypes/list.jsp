<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>

<script type="text/javascript" src="<c:url value="/javascript/project-cardtypes.js"/>"></script>

<page:applyDecorator name="admin">
	<div id="cardTypesList">
	<ul id="cardTypes" class="pretty">
		<c:forEach items="${cardTypeList}" var="type">
		<div id="cardType_${type.name }">
		<li class="story card" name="${type.name }" id="cardType_${type.id}"  >		
				<span class="name" onclick="toggleDescription(this.parentNode);"> 
					${type.name }
				</span>
				<c:forEach items="${type.gadgets}" var="gadget"
				varStatus="s">
				<div class="description"><pre>${gadget }</pre></div>
				
				</c:forEach>
			<div class="action" id="${type.name }"><a
				class="ui-icon ui-icon-pencil" name="edit ${type.name }"
				title="<fmt:message key="edit"/>"
				href="<c:url value="/projects/${type.project.id}/cardTypes/${type.id}/"/>"></a>			
			</div>
		</li>
		</div>
		</c:forEach>
	</ul>
	</div>

	<div class="clear">
	<hr>
	<c:if test="${not empty message}">
	<span class="success"><c:out value="${message }" ></c:out></span><br /><br />
	</c:if> 
	
	<a href="<c:url value="/projects/${project.id }/cardTypes/" />"><fmt:message
		key="add.cardType" /></a> 
	<c:choose>
		<c:when test="${not empty cardType}">
			<c:url var="urlActionForm"
				value="/projects/${project.id }/cardTypes/${cardType.id}/" />
		</c:when>
		<c:otherwise>
			<c:url var="urlActionForm"
				value="/projects/${project.id }/cardTypes/" />
		</c:otherwise>
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
		type="button"  name="cancel" value="<fmt:message key="cancel"/>" onclick="location.href='<c:url value="/projects/${project.id }/cardTypes/" />';"  /></p>
	</form>
	<div id="success_message" style="color: blue;">
	
	</div>
	</div>

</page:applyDecorator>