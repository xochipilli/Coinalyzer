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

<a href="/coinalyzer?refresh">Refresh</a>

<br/>
  	
Period: <a href="/coinalyzer/?period=5m">5m</a> <a href="/coinalyzer/?period=15m">15m</a> <a href="/coinalyzer/?period=30m">30m</a> <a href="/coinalyzer/?period=2h">2h</a> <a href="/coinalyzer/?period=4h">4h</a>

<br />

Timeframe: <a href="/coinalyzer/?timeframe=6h">6h</a> <a href="/coinalyzer/?timeframe=24h">24h</a> <a href="/coinalyzer/?timeframe=2d">2d</a> <a href="/coinalyzer/?timeframe=4d">4d</a> <a href="/coinalyzer/?timeframe=1w">1w</a> <a href="/coinalyzer/?timeframe=2w">2w</a> <a href="/coinalyzer/?timeframe=1m">1m</a> <a href="/coinalyzer/?timeframe=all">all</a>

<br />

 <img src="chart.png">

<br /> 

<a href="/coinalyzer/?train">Train NN</a>

<br />

</body>
</html>
