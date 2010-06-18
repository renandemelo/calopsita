<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="ownerName" value="nobody"/>
<c:if test="${not empty gadget.owner}">
	<c:set var="ownerName" value="${gadget.owner.name}"/>	
</c:if>

<c:set var="urlBeAnOwner" value="/projects/${gadget.card.project.id}/iterations/${gadget.card.iteration.id}/card/${gadget.card.id}/cardOwner/" />

<sub class="assignable" title="Owner" align="right"> Owned by <c:out value="${ownerName}" />
	<a href='<c:url value="${urlBeAnOwner}"/>'>Be an Owner now!</a>
</sub>