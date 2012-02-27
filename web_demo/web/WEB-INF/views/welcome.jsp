<%@page contentType="text/html;charset=UTF-8"%>
<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
	<META http-equiv="Content-Type" content="text/html;charset=UTF-8">
	<title>欢迎您!</title>
	<link rel="stylesheet" href="<c:url value="/resources/blueprint/screen.css" />" type="text/css" media="screen, projection">
	<link rel="stylesheet" href="<c:url value="/resources/blueprint/print.css" />" type="text/css" media="print">
	<!--[if lt IE 8]>
		<link rel="stylesheet" href="<c:url value="/resources/blueprint/ie.css" />" type="text/css" media="screen, projection">
	<![endif]-->
</head>
<body>
<div class="container">  
	<h1>
		欢迎您!
	</h1>
	<p>
		Locale = ${pageContext.response.locale} 
		测试中文
	</p>
	<p>
		hello : ${hello} 
	</p>
	<p>
		name : ${name} 
	</p>
	<p>
		ad2.name : ${ad2.name}
	</p>
	<p>
	分页 ${pageStr}
	</p>
	<c:forEach items="${page.result}" var="ad" varStatus="status">
		<li>(${status.index }) name : ${ad.name} </li>
	</c:forEach>
	<hr>	
	<ul>
		<li> <a href="?locale=en_us">us</a> |  <a href="?locale=en_gb">gb</a> | <a href="?locale=es_es">es</a> | <a href="?locale=de_de">de</a> </li>
	</ul>
	<ul>
		<li><a href="account">@Controller Example</a></li>
	</ul>
	
	<form action="/demo/ad" enctype="multipart/form-data" method="post">
     <input type="text" name="username" /><br />
     <input type="file" name="myfile" /><br/>
     <input type="file" name="myfile" /><br/>
     <input type="submit" />
    </form>
</div>
</body>
</html>