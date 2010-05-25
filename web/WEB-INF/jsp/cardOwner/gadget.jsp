<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ownerName" value="nobody"/>
<c:if test="${gadgetForHtml.owner != null}">
	<c:set var="ownerName" value="${gadgetForHtml.owner.name}"/>	
</c:if>

<c:set var="urlBeAnOwner" value="/projects/${gadgetForHtml.card.project.id}/iterations/${gadgetForHtml.card.iteration.id}/card/${gadgetForHtml.card.id}/cardOwner/" />

<sub class="assignable" title="Owner" align="right"> Owned by <c:out value="${ownerName}" />
	<a href='<c:url value="${urlBeAnOwner}"/>'>Be an Owner now!</a>
</sub>