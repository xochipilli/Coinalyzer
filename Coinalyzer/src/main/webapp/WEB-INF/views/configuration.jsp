<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Coinalyzer - configuration</title>
</head>
<body>

	<p align="left">

		<font color="red">Coin</font><font color="green">alyzer</font> <a
			href="/caapi">Main</a>
	</p>

	<p>${message}</p>
	<br />
	<form action="config" method="post">
		Primary Coin: <input type="text" name="coinPrimary" value="${AppConfig.coinPrimary}"><br />
		Counter Coin: <input type="text" name="coinCounter" value="${AppConfig.coinCounter}"><br />
		
		Proxy host: <input type="text" name="proxyhost" value="${AppConfig.prohyhost}"> Port: <input type="text" name="proxyport" value="${AppConfig.proxyport}"><br />
		
		<br /><br />
		
		<input type="submit" value="Submit">
		
	</form>
</body>
</html>
