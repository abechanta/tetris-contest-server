<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<link rel="stylesheet" type="text/css" href="style.css" />
	<title>Insert title here</title>
	<script type="text/javascript">

	</script>
</head>
<body>
	<div class="container">
	<%@ include file="header.jsp" %>
	<%@ include file="nav.jsp" %>

	<div class="content">
	<p /><h2>管理者メニュー → UpdateRank</h2><p />

	<form method="post" action="UpdateRank" name="form0" onSubmit="return onSubmit()">
	<input type="submit" value="更新" />
	</form>

	<p />
	<a href="index.jsp">もどる</a>
	</div>

	<%@ include file="footer.jsp" %>
	</div>
</body>
</html>
