<%@page contentType="text/html;charset=UTF-8"%>

<%@page pageEncoding="UTF-8"%>
<%@ page session="false"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
	
	<shiro:guest>
     <a href="/my/ad/login">登录</a>
     </shiro:guest>
     <shiro:user>
     	<a href="/my/ad/logout">退出</a>
     	Welcome, <shiro:principal />
     	<shiro:hasRole name="admin">
     		我是管理员
     	</shiro:hasRole>
     </shiro:user>

</div>
</body>
</html>