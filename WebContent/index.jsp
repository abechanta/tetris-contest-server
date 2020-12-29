<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="style.css">
	<title>タイトル名</title>

	<script type="text/javascript">
<!--
function onSubmit() {
	return true;
}
-->
	</script>
</head>
<body>
	<div class="container">
	<%@ include file="header.jsp" %>
	<%@ include file="nav.jsp" %>

	<div class="content">
		<h1>ようこそ tetcon へ！</h1>
		ようこそ。

		<hr />

		<br />
		※ SVG 形式に対応していないブラウザでは、表示用のプラグイン（ <a href="http://www.adobe.com/jp/svg/">Adobe SVG Viewer</a> など）を導入する必要があります。
		<p />
	</div>

	<%@ include file="footer.jsp" %>
	</div>
</body>
</html>
