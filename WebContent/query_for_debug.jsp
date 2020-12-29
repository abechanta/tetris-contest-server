<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"
	import="java.util.Vector"
%>
<jsp:useBean id="table0" class="java.lang.String" scope="request" />
<jsp:useBean id="cond0" class="java.lang.String" scope="request" />
<jsp:useBean id="fetch0" class="java.lang.String" scope="request" />
<jsp:useBean id="id0" class="java.lang.String" scope="request" />
<jsp:useBean id="outArray" class="java.util.Vector" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<link rel="stylesheet" type="text/css" href="style.css" />
	<title>Insert title here</title>
	<script type="text/javascript">

function onSubmit() {
	var cond0 = document.getElementsByName('cond0');
	for (var ii = 0; ii < cond0.length; ii++) {
		if (!cond0[ii].checked) {
			continue;
		}
		if (cond0[ii].value == 'fetch') {
			var fetch0 = document.getElementById('fetch0');
			if (!fetch0.value.match(/^[0-9]+$/) || (fetch0.value > 100)) {
				window.alert('FETCH');
				return false;
			}
		} else if (cond0[ii].value == 'id') {
			var id0 = document.getElementById('id0');
			if (!id0.value.match(/^[0-9]+$/)) {
				window.alert('ID');
				return false;
			}
		}
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
	<p /><h2>管理者メニュー → QueryTable</h2><p />

	<form method="post" action="QueryTable" name="form0" onSubmit="return onSubmit()">
	PASSWD=
	<input type="password" size=20 name="passwd0" />
	<br />
	TABLE=
	<select name="table0">
		<option value="contest" <%= table0.equals("contest") ? "selected" : "" %>>CONTEST</option>
		<option value="member"  <%= table0.equals("member" ) ? "selected" : "" %>>MEMBER </option>
		<option value="program" <%= table0.equals("program") ? "selected" : "" %>>PROGRAM</option>
		<option value="result"  <%= table0.equals("result" ) ? "selected" : "" %>>RESULT </option>
		<option value="rule"    <%= table0.equals("rule"   ) ? "selected" : "" %>>RULE   </option>
		<option value="score"   <%= table0.equals("score"  ) ? "selected" : "" %>>SCORE  </option>
		<option value="title"   <%= table0.equals("title"  ) ? "selected" : "" %>>TITLE  </option>
	</select>
	<br />
	<input type="radio" name="cond0" value="fetch" <%= cond0.equals("fetch") ? "checked" : "" %> />
	FETCH=
	<input type="text" size=20 value="<%= fetch0 %>" name="fetch0" />
	/
	<input type="radio" name="cond0" value="id" <%= cond0.equals("id") ? "checked" : "" %> />
	ID=
	<input type="text" size=20 value="<%= id0 %>" name="id0" />
	<p />
	<input type="submit" value="検索" />
	</form>

<%
	Vector<String> in = (Vector<String>)outArray;
	if (in.size() > 0) {
		int idx = 0;
		int width = Integer.valueOf(in.get(idx++));
		int height = (in.size() - 1) / width;
%>
	<hr />

	<table class="at">
<%
		for (int jj = 0; jj < height; jj++) {
%>
		<tr class="<%= (jj % 2) == 0 ? "even" : "odd" %>">
<%
			for (int ii = 0; ii < width; ii++) {
				if (jj == 0) {
%>
			<th><%= in.get(idx++) %></th>
<%
				} else {
%>
			<td><%= in.get(idx++) %></td>
<%
				}
			}
%>
		</tr>
<%
		}
%>
	</table>
<%
	}
%>

	<p />
	<a href="index.jsp">もどる</a>
	</div>

	<%@ include file="footer.jsp" %>
	</div>
</body>
</html>
