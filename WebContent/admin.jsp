<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<link rel="stylesheet" type="text/css" href="style.css" />
	<title>Insert title here</title>
</head>
<body>
	<div class="container">
	<%@ include file="header.jsp" %>
	<%@ include file="nav.jsp" %>

	<div class="content">
	<p /><h2>管理者メニュー</h2><p />

	<a href="query_for_debug.jsp">Query For Debug</a>
	<p />
	<a href="program_registration.jsp">Program Registration</a>
	<p />
	<a href="contest_registration.jsp">Contest Registration</a>
	<p />
	<a href="ranking_update.jsp">Ranking Update</a>

	<p />
	<a href="index.jsp">もどる</a>
	</div>

	<%@ include file="footer.jsp" %>
	</div>
</body>
</html>
