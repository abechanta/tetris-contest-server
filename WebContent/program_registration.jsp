<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<jsp:useBean id="bean1" class="java.util.Hashtable" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<link rel="stylesheet" type="text/css" href="style.css" />
	<title>Insert title here</title>
	<script type="text/javascript">

function onSubmit() {
	var ver0 = document.getElementById('ver0');
	if (!ver0.value.match(/^[-0-9A-Za-z ._/]+$/)) {
		window.alert('VER');
		return false;
	}
	var program0 = document.getElementById('program0');
	if (!program0.value.match(/^.+$/)) {
		window.alert('PROGRAM');
		return false;
	}
	return true;
}

	</script>
</head>
<body>
	<div class="container">
	<%@ include file="header.jsp" %>
	<%@ include file="nav.jsp" %>

	<div class="content">
	<p /><h2>管理者メニュー → RegisterProgram</h2><p />

	<form method="post" action="RegisterProgram" name="form0" enctype="multipart/form-data" onSubmit="return onSubmit()">
	PASSWD=
	<input type="password" size=20 name="passwd0" />
	<br />
	VER=
	<input type="text" size=20 name="ver0" />
	<br />
	PROGRAM=
	<input type="file" size=60 name="program0" />
	<p />
	<input type="submit" value="登録" />
	</form>

	<p />
	<a href="index.jsp">もどる</a>
	</div>

	<%@ include file="footer.jsp" %>
	</div>
</body>
</html>
