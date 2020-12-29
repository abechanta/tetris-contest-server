<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"
	import="java.util.Vector"
	import="com.hotmail.abechanta.tetcon.Model.Contest"
	import="com.hotmail.abechanta.tetcon.Model.Rank"
%>
<jsp:useBean id="cid0" class="java.lang.String" scope="request" />
<jsp:useBean id="contest0" class="java.lang.String" scope="request" />
<jsp:useBean id="contest1" class="java.lang.String" scope="request" />
<jsp:useBean id="outContest" class="java.util.Vector" scope="request" />
<jsp:useBean id="outRank" class="java.util.Vector" scope="request" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<link rel="stylesheet" type="text/css" href="style.css" />
	<title>TETCON RANKING</title>
	<script type="text/javascript">

	</script>
</head>
<body>
	<div class="container">
	<%@ include file="header.jsp" %>
	<%@ include file="nav.jsp" %>

	<div class="content">
	<p /><h2>ランキング選択</h2><p />

	<form method="get" action="QueryRank" name="form0">
	<input type="hidden" name="cid0" value="<%= cid0 %>">
	<table>
<%
	Vector<Contest> contests = (Vector<Contest>)outContest;
	Contest currentContest = null;
	int openContest = 0;
	int closedContest = 0;
	for (int ii = 0; ii < contests.size(); ii++) {
		if (Integer.valueOf(cid0) == contests.get(ii).getCid()) {
			currentContest = contests.get(ii);
		}
		switch (contests.get(ii).getState()) {
		case Contest.STATE_OPEN:
			openContest++;
			break;
		case Contest.STATE_CLOSED:
			closedContest++;
			break;
		}
	}
%>
	<tr>
		<td>
			開催中のコンテスト＝
		</td>
		<td>
<%
	if (openContest <= 0) {
%>
 			ありません。
<%
	} else {
%>
	 		<select name="contest0">
<%
		for (int ii = 0; ii < contests.size(); ii++) {
			if (contests.get(ii).getState() == Contest.STATE_OPEN) {
%>
				<option value="<%= contests.get(ii).getCid() %>" <%= Integer.valueOf(contest0) == contests.get(ii).getCid() ? "selected" : "" %>><%= contests.get(ii).getName() %></option>
<%
			}
		}
%>
			</select>
<%
	}
%>
		</td>
		<td>
			<input type="submit" value="暫定ランキングを表示" <%= openContest <= 0 ?   "disabled" : "" %> onclick="document.getElementById('cid0').value = document.getElementById('contest0').value;" />
		</td>
	</tr>
	<tr>
		<td>
			過去のコンテスト＝
		</td>
		<td>
<%
	if (closedContest <= 0) {
%>
 			ありません。
<%
	} else {
%>
 		<select name="contest1">
<%
		for (int ii = 0; ii < contests.size(); ii++) {
			if (contests.get(ii).getState() == Contest.STATE_CLOSED) {
%>
				<option value="<%= contests.get(ii).getCid() %>" <%= Integer.valueOf(contest1) == contests.get(ii).getCid() ? "selected" : "" %>><%= contests.get(ii).getName() %></option>
<%
			}
		}
%>
			</select>
		</td>
		<td>
			<input type="submit" value="確定ランキングを表示" <%= closedContest <= 0 ? "disabled" : "" %> onclick="document.getElementById('cid0').value = document.getElementById('contest1').value;" />
		</td>
	</tr>
<%
	}
%>
	</table>
	</form>

<%
	if (currentContest != null) {
		int row = 0;
%>
	<p /><h2>コンテストの詳細</h2><p />

	<table class="at">
		<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
			<td>Name</td>
			<td><%= currentContest.getName() %></td>
		</tr>
		<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
			<td>Period</td>
			<td><%= currentContest.getDate0() %>～<%= currentContest.getDate1() %></td>
		</tr>
		<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
			<td>Rule</td>
			<td><%= currentContest.getRuleid() %></td>
		</tr>
		<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
			<td>Points By</td>
			<td><%= currentContest.getCategory() %></td>
		</tr>
		<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
			<td>Required Rounds</td>
			<td><%= currentContest.getCntr() %></td>
		</tr>
		<tr class="<%= (row++ % 2) == 0 ? "even" : "odd" %>">
			<td>Updated On</td>
			<td><%= currentContest.getUpdated() %></td>
		</tr>
	</table>
<%
	}
%>

<%
	Vector<Rank> ranks = (Vector<Rank>)outRank;
	if (ranks.size() > 0) {
%>
	<p /><h2>ランキング（<%= currentContest.getUpdated() %> 集計）</h2><p />

	<table class="at">
		<tr class="even">
			<th>RANK</th>
			<th>MEMBER</th>
			<th>ROUND</th>
			<th>PTS</th>
		</tr>
<%
		for (int ii = 0; ii < ranks.size(); ii++) {
%>
		<tr class="<%= (ii % 2) == 0 ? "even" : "odd" %>">
			<td><%= ranks.get(ii).getRank() %></td>
			<td><a href="QueryStats?mid0=<%= ranks.get(ii).getMid() %>&ruleid0=<%= currentContest.getRuleid() %>&date0=<%= currentContest.getDate0().substring(0, 6) %>&date1=<%= currentContest.getDate1().substring(0, 6) %>"><%= ranks.get(ii).getMid() %></a></td>
			<td><%= ranks.get(ii).getCntr() %></td>
			<td><%= ranks.get(ii).getPts() %></td>
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
