<%@page import="nu.dyn.caapi.bot.AppConfig"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Error</title>
</head>
<body>

	<font color="red">
		<h1>Error</h1>
	</font>
	<h3>
		Could not load config file
		<%=AppConfig.configurationFile%>
	</h3>

</body>
</html>
