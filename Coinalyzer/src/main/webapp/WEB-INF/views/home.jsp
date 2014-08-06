<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page session="false" %>
<html>
<head>
	<title>Coinalyzer</title>
</head>
<body>

<p align="left">

<font color="red">Coin</font><font color="green">alyzer</font>
<a href="config">Configuration</a>
</p>  

<br/>

<a href="/caapi?refresh">Refresh</a>

<br/>

Period: <a href="/caapi/?period=5m">5m</a> <a href="/caapi/?period=15m">15m</a> <a href="/caapi/?period=30m">30m</a> <a href="/caapi/?period=2h">2h</a> <a href="/caapi/?period=4h">4h</a>

<br />

Timeframe: <a href="/caapi/?timeframe=6h">6h</a> <a href="/caapi/?timeframe=24h">24h</a> <a href="/caapi/?timeframe=2d">2d</a> <a href="/caapi/?timeframe=4d">4d</a> <a href="/caapi/?timeframe=1w">1w</a> <a href="/caapi/?timeframe=2w">2w</a> <a href="/caapi/?timeframe=1m">1m</a> <a href="/caapi/?timeframe=all">all</a>

<br/>




 <img src="chart.png">
 
 <% 
 	System.out.println("coinPairs");
 %>
 
</body>
</html>
