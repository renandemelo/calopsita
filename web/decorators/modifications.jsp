<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="calopsita" %>
<html>
	
	<head>
		<decorator:head />
	</head>
	<body>
		<div id="content" title="<fmt:message key="modifications"/>">
			<decorator:body />
		</div>
	</body>
</html>