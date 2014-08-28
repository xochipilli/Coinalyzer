<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.text.DateFormat, java.text.SimpleDateFormat,java.util.Calendar" %>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error Page</title>
</head>

<body>

	<h2><font color="red">Coin</font><font color="green">alyzer</font> application error</h2>

	<h3>Debug Information:</h3>
	<%
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS");
		Calendar cal = Calendar.getInstance();
		String datetime = dateFormat.format(cal.getTime());
	%>
	Timestamp = <%=datetime %>
	<br />
	Requested URL = ${url}
	<br /> 
	Exception = ${exception.message}
	<br />
	<br />

	<strong>Exception Stack Trace</strong>
	<br />
	<c:forEach items="${exception.stackTrace}" var="ste">
    ${ste}
	</c:forEach>

</body>

</html>