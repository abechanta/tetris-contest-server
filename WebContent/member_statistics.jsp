<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"
	import="java.util.Hashtable"
	import="java.util.Vector"
	import="com.hotmail.abechanta.tetcon.Model.Member"
	import="com.hotmail.abechanta.tetcon.Model.Rule"
%>
<jsp:useBean id="mid0" class="java.lang.String" scope="request" />
<jsp:useBean id="name0" class="java.lang.String" scope="request" />
<jsp:useBean id="author0" class="java.lang.String" scope="request" />
<jsp:useBean id="ruleid0" class="java.lang.String" scope="request" />
<jsp:useBean id="rulestr0" class="java.lang.String" scope="request" />
<jsp:useBean id="rulew" class="java.lang.String" scope="request" />
<jsp:useBean id="ruleh" class="java.lang.String" scope="request" />
<jsp:useBean id="ruleg" class="java.lang.String" scope="request" />
<jsp:useBean id="period0" class="java.lang.String" scope="request" />
<jsp:useBean id="date0" class="java.lang.String" scope="request" />
<jsp:useBean id="date1" class="java.lang.String" scope="request" />
<jsp:useBean id="outMember" class="java.util.Vector" scope="request" />
<jsp:useBean id="outRule" class="java.util.Vector" scope="request" />
<jsp:useBean id="outStat" class="java.util.Hashtable" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<link rel="stylesheet" type="text/css" href="style.css" />
	<title>TETCON STATS</title>
	<script type="text/javascript">

function onSubmit() {
	var name0 = document.getElementById('name0');
	if (!name0.value.match(/^[-0-9A-Za-z ._/]*$/)) {
		window.alert('NAME');
		return false;
	}
	var author0 = document.getElementById('author0');
	if (!author0.value.match(/^[-0-9A-Za-z ._/]*$/)) {
		window.alert('AUTHOR');
		return false;
	}
	var rulew = document.getElementById('rulew');
	if (!rulew.value.match(/^[1-9][0-9]*$/)) {
		window.alert('RULE W');
		return false;
	}
	var ruleh = document.getElementById('ruleh');
	if (!ruleh.value.match(/^[1-9][0-9]*$/)) {
		window.alert('RULE H');
		return false;
	}
	var ruleg = document.getElementById('ruleg');
	if (!ruleg.value.match(/^[1-9][0-9]*$/)) {
		window.alert('RULE G');
		return false;
	}
	return true;
}

function getDateString_yymmdd(date) {
	var yy = date.getYear() % 100;
	if (yy < 1) {
		yy = "00";
	} else if (yy < 10) {
		yy = "0" + yy;
	}
	var mm = date.getMonth() + 1;
	if (mm < 10) {
		mm = "0" + mm;
	}
	var dd = date.getDate();
	if (dd < 10) {
		dd = "0" + dd;
	}
	return yy + mm + dd;
}

function getDateString(date) {
	return getDateString_yymmdd(date);
}

function getThisMonthString() {
	var date = new Date();
	date.setDate(1);
	return getDateString(date);
}

function getThisWeekString() {
	var date = new Date();
	var dec = date.getDay();
	var dd = date.getDate();
	var mm = date.getMonth() + 1;
	var yy = date.getYear() % 100;

	if (dd > dec) {
		dd -= dec;
	} else {
		// move to last month.
		dec -= dd;
		if (mm > 1) {
			mm--;
		} else {
			// move to last year.
			mm = 12;
			yy--;
		}
		var end = new Array(
			31, 28, 31, 30, 31, 30,
			31, 31, 30, 31, 30, 31
		);
		dd = end[mm] + ((yy % 4 == 0) ? 1 : 0) - dec;
	}
	return getDateString(new Date(2000 + yy, mm - 1, dd));
}

function getNowString() {
	var date = new Date();
	return getDateString(date);
}

function onSelect() {
	var date0 = document.getElementById('date0');
	var date1 = document.getElementById('date1');
	var period0 = document.getElementById('period0');
	if (period0.value == "LIFE") {
		date0.value = '';
		date1.value = '';
	} else if (period0.value == "MONTHLY") {
		date0.value = getThisMonthString();
		date1.value = getNowString();
	} else if (period0.value == "WEEKLY") {
		date0.value = getThisWeekString();
		date1.value = getNowString();
	}
}

window.onload = (
	function () {
		var name0 = document.getElementById('name0');
		name0.focus();
	}
);

	</script>
</head>
<body>
	<div class="container">
	<%@ include file="header.jsp" %>
	<%@ include file="nav.jsp" %>

	<div class="content">
	<p /><h2>メンバ／ルール選択</h2><p />

	<form method="get" action="QueryStats" name="form0" onsubmit="return onSubmit()">
	<input type="hidden" name="mid0" value="<%= mid0 %>" />
	<input type="hidden" name="ruleid0" value="<%= ruleid0 %>" />
	<table>
	<tr>
		<td>MEMBER＝</td>
		<td>
			名前（NAME）
			<input type="text" size=20 value="<%= name0 %>" name="name0" onchange="document.getElementById('mid0').value = ''" />
			×
			作者（AUTHOR）
			<input type="text" size=20 value="<%= author0 %>" name="author0" onchange="document.getElementById('mid0').value = ''" />
		</td>
	</tr>
<%
	Vector<Member> members = (Vector<Member>)outMember;
	Vector<Rule> rules = (Vector<Rule>)outRule;
	if (rulestr0.length() == 0) {
		rulestr0 = rules.size() <= 0 ? "LONGRUN" : String.valueOf(rules.get(0).getStr());
	}
	if (rulew.length() == 0) {
		rulew = rules.size() <= 0 ? "10" : String.valueOf(rules.get(0).getW());
	}
	if (ruleh.length() == 0) {
		ruleh = rules.size() <= 0 ? "18" : String.valueOf(rules.get(0).getH());
	}
	if (ruleg.length() == 0) {
		ruleg = rules.size() <= 0 ? "10" : String.valueOf(rules.get(0).getG());
	}

	if (members.size() > 1) {
%>
	<tr>
		<td>&nbsp;</td>
		<td>
			<blockquote><small>
				複数のメンバ候補があります。
				<br />
<%
			for (int ii = 0; ii < members.size(); ii++) {
%>
				→ メンバ
				&quot;<%= members.get(ii).getName() %>&quot;
				を
				<input type="submit" value="表示" onclick="document.getElementById('mid0').value = <%= members.get(ii).getMid() %>" />
				<br />
<%
			}
%>
			</small></blockquote>
		</td>
	</tr>
<%
	}
%>
	<tr>
		<td>RULE＝</td>
		<td>
	 		<select name="rulestr0" onchange="document.getElementById('ruleid0').value = ''" >
				<option value="LONGRUN" <%= rulestr0.equals("LONGRUN") ? "selected" : "" %>>LONGRUN</option>
			</select>
			×
			幅（W）
			<input type="text" size=3 value="<%= rulew %>" name="rulew" onchange="document.getElementById('ruleid0').value = ''" />
			×
			高さ（H）
			<input type="text" size=3 value="<%= ruleh %>" name="ruleh" onchange="document.getElementById('ruleid0').value = ''" />
			×
			重力（G）
			<input type="text" size=3 value="<%= ruleg %>" name="ruleg" onchange="document.getElementById('ruleid0').value = ''" />
		</td>
	</tr>
	<tr>
		<td>PERIOD＝</td>
		<td>
	 		<select name="period0" onclick="onSelect()">
				<option value="LIFE"    <%= period0.equals("LIFE")    ? "selected" : "" %>>通算</option>
				<option value="MONTHLY" <%= period0.equals("MONTHLY") ? "selected" : "" %>>今月</option>
				<option value="WEEKLY"  <%= period0.equals("WEEKLY")  ? "selected" : "" %>>今週</option>
			</select>
			（
				FROM
				<input type="text" size=12 value="<%= date0 %>" name="date0" onchange="document.getElementById('period0').value = 'FREE'" />
				～
				TO
				<input type="text" size=12 value="<%= date1 %>" name="date1" onchange="document.getElementById('period0').value = 'FREE'" />
			）
			<br />
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>
			<input type="submit" value="表示" />
		</td>
	</tr>
	</table>
	</form>

<%
	if (members.size() == 1) {
%>
	<p /><h2>メンバ成績</h2><p />

	MEMBER("<%= members.get(0).getName() %>")
	<br />
	RULE(<%= rulestr0 %>×<%= rulew %>×<%= ruleh %>×<%= ruleg %>)
<%
		if (!period0.equals("LIFE")) {
%>
	<br />
	PERIOD(<%= date0 %>～<%= date1 %>)
<%
		}
%>
	<p />

<%
		Hashtable<String, String> stat = (Hashtable<String, String>)outStat;
		if (Integer.valueOf(stat.get("cntR")) == 0) {
%>
	該当する試合結果がありません。
<%
		} else {
			int row = 0;
			String param = "mid0=" + mid0 + "&ruleid0=" + ruleid0 + "&date0=" + date0 + "&date1=" + date1;
%>
	<table class="at">
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Name</td>
		<td><%= members.get(0).getName() %></td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Author</td>
		<td><%= members.get(0).getAuthor() %></td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Registered On</td>
		<td><%= members.get(0).getRegistered() %></td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Round</td>
		<td>
			<%= stat.get("cntR") %>
			<%= period0.equals("LIFE") ? "" : "(Filtered by " + period0 +")" %>
		</td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Piece Average</td>
		<td>
			<%= stat.get("avgP") %> (
			Rank: <%= stat.get("avgP_R") %> /
			Record: <%= stat.get("maxP") %> )
		</td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Piece Rate</td>
		<td>
			<%= stat.get("avgPr") %>% (
			Rank: <%= stat.get("avgPr_R") %> )
			<br />
			<embed src="QueryHistory_Histogram?category0=pr&<%= param %>" type="image/svg+xml" width="500" height="250" />
		</td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Line Average</td>
		<td>
			<%= stat.get("avgL") %> (
			Rank: <%= stat.get("avgL_R") %> /
			Record: <%= stat.get("maxL") %> )
		</td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Line Rate</td>
		<td>
			x<%= Integer.valueOf(stat.get("avgLr").toString()) / 100.0 %> (
			Rank: <%= stat.get("avgLr_R") %> )
			<br />
			<embed src="QueryStats_BarChart?category0=lsum&<%= param %>" type="image/svg+xml" width="500" height="50" />
		</td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Recovery Rate</td>
		<td>
			<%= stat.get("avgRr") %>% (
			Rank: <%= stat.get("avgRr_R") %> )
			<br />
			<embed src="QueryHistory_Histogram?category0=rr&<%= param %>" type="image/svg-xml" width="500" height="250" />
		</td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Penalty</td>
		<td>
			<%= stat.get("sumX") %> (
			Rank: <%= stat.get("sumX_R") %> )
		</td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Title</td>
		<td>
			<%= stat.get("title0") %>
		</td>
	</tr>
	</table>

	<p /><h2>試合結果</h2><p />

	MEMBER("<%= members.get(0).getName() %>")
	<br />
	RULE(<%= rulestr0 %>×<%= rulew %>×<%= ruleh %>×<%= ruleg %>)
<%
		if (!period0.equals("LIFE")) {
%>
	<br />
	PERIOD(<%= date0 %>～<%= date1 %>)
<%
		}
%>
	<p />

	<table class="at">
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Piece Rate</td>
		<td>
			<embed src="QueryHistory_LogChart?max=100&unit=%25&category0=pr&offset0=0&<%= param %>" type="image/svg+xml" width="640" height="120" />
		</td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Line</td>
		<td>
			<embed src="QueryHistory_LogChart?max=400&unit=&category0=l&offset0=0&<%= param %>" type="image/svg+xml" width="640" height="120" />
		</td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Recovery Rate</td>
		<td>
			<embed src="QueryHistory_LogChart?max=100&unit=%25&category0=rr&offset0=0&<%= param %>" type="image/svg+xml" width="640" height="120" />
		</td>
	</tr>
	<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
		<td>Penalty</td>
		<td>
			<embed src="QueryHistory_LogChart?max=1&unit=&category0=x&offset0=0&<%= param %>" type="image/svg+xml" width="640" height="120" />
		</td>
	</tr>
	</table>

<%
		}
	}
%>

	<p />
	<a href="index.jsp">もどる</a>
	</div>

	<%@ include file="footer.jsp" %>
	</div>
</body>
</html>