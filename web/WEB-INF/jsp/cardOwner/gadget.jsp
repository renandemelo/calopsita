<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${not empty gadgetForHtml.card.iteration}">
	
	<c:if test="${gadgetForHtml.card.iteration.current}">
		<c:set var="ownerName" value="nobody"/>
		<c:if test="${gadgetForHtml.owner != null}">
			<c:set var="ownerName" value="${gadgetForHtml.owner.login}"/>	
		</c:if>
		
		<c:set var="urlBeAnOwner" value="/projects/${gadgetForHtml.card.project.id}/iterations/${gadgetForHtml.card.iteration.id}/card/${gadgetForHtml.card.id}/cardOwner/" />
		
		<sub class="assignable" title="Owner" align="right"> Owned by <c:out value="${ownerName}" />
			<c:if test="${currentUser.login != ownerName}">
				<a href='<c:url value="${urlBeAnOwner}"/>'>Be an Owner now!</a>	
			</c:if>
		</sub>
	</c:if>
</c:if>