<%@ tag language="java" pageEncoding="UTF-8" dynamic-attributes="attrs"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ attribute name="cards" description="A card" required="true" type="java.util.List" %>
<%@ attribute name="listId" description="css id for listing" required="false" type="java.lang.String" %>
<%@ attribute name="classes" description="css classes for listing" required="false" type="java.lang.String" %>
<%@ attribute name="description" description="should show description?" required="false" type="java.lang.Boolean" %>


<ul class="stories ${classes }" 
	<c:if test="${not empty listId }">
		id="${listId }"
	</c:if>
	<c:forEach items="${attrs}" var="attr">
		${attr.key}="${attr.value}"	
	</c:forEach>
 >
	<c:forEach items="${cards}" var="card" varStatus="s">
		<%@ variable name-given="card" %>
		<li class="story card" name="${card.name }" id="cards_${card.id}">
			<c:if test="${not empty card.subcards }">
				<span class="sign" onclick="toggleSubcards(this, ${card.id});">[+]</span>
			</c:if>
			<span class="name"
			<c:if test="${description }">
				onclick="toggleDescription(this.parentNode);"
			</c:if> 
			>
			${card.name } <sub class="creator">by ${card.creator.login }</sub></span>
			
			<div class="description"><pre>${fn:escapeXml(card.description) }</pre></div>
			<div class="gadgets">
				<c:forEach items="${card.gadgets}" var="gadget">
					${gadget.html}
				</c:forEach>
			</div>
			<div class="action">
				<a class="ui-icon ui-icon-pencil" name="edit ${card.name }" 
					title="<fmt:message key="edit"/>"
					href="<c:url value="/projects/${card.project.id}/cards/${card.id}/"/>"></a>
				<a class="ui-icon ui-icon-closethick" name="delete ${card.name }" href="javascript:void(0)" 
					title="<fmt:message key="delete"/>"
					onclick="confirmCardDeletion(this, '<c:url value="/projects/${card.project.id}/cards/${card.id}/"/>', ${not empty card.subcards })"></a>
			</div>			
			<jsp:doBody/>
		</li>
		<c:if test="${not empty card.subcards}">
			<c:set var="__subcards" value="${card.subcards}" scope="request" />
			<c:set var="__description" value="${description}" scope="request" />
			<c:set var="__id" value="${card.id}" scope="request" />
			<jsp:include page="/WEB-INF/subcards.jsp"/>
		</c:if>
	</c:forEach>
</ul>