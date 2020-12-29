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
	var name0 = document.getElementById('name0');
	if (!name0.value.match(/^[-0-9A-Za-z ._/]*$/)) {
		window.alert('NAME');
		return false;
	}
	var date0 = document.getElementById('date0');
	if (!date0.value.match(/^[0-9]{6}$/)) {
		window.alert('DATE0');
		return false;
	}
	var date1 = document.getElementById('date1');
	if (!date1.value.match(/^[0-9]{6}$/)) {
		window.alert('DATE1');
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
	var cntr0 = document.getElementById('cntr0');
	if (!cntr0.value.match(/^[1-9][0-9]*$/)) {
		window.alert('CNTR');
		return false;
	}
	var prize1 = document.getElementById('prize1');
	if (!prize1.value.match(/^[1-9]$/)) {
		window.alert('PRIZE1');
		return false;
	}
	var prize2 = document.getElementById('prize2');
	if (!prize2.value.match(/^[1-9]$/)) {
		window.alert('PRIZE2');
		return false;
	}
	var prize3 = document.getElementById('prize3');
	if (!prize3.value.match(/^[1-9]$/)) {
		window.alert('PRIZE3');
		return false;
	}
	return true;
}

function onChangeDate() {
	// FIXME
	var date0 = document.getElementById('date0');
	var date1 = document.getElementById('date1');
	if (date0.value > date1.value) {
		window.alert('DATE0 / DATE1');
	}
}

function onChangeWeight() {
	// FIXME
	var weight1 = document.getElementById('weight1');
	var weight2 = document.getElementById('weight2');
	var weight3 = document.getElementById('weight3');
	if (weight1.value < weight2.value) {
		window.alert('WEIGHT1 / WEIGHT2');
	} else if (weight2.value < weight3.value) {
		window.alert('WEIGHT2 / WEIGHT3');
	}
}

// FIXME
window.onload = (
	function () {
		var passwd0 = document.getElementById('passwd0');
		passwd0.focus();
	}
);

	</script>
</head>
<body>
	<div class="container">
	<%@ include file="header.jsp" %>
	<%@ include file="nav.jsp" %>

	<div class="content">
	<p /><h2>管理者メニュー → RegisterContest</h2><p />

	<form method="post" action="RegisterContest" name="form0" onSubmit="return onSubmit()">
	PASSWD=
	<input type="password" size=20 name="passwd0" />
	<br />
	NAME=
	<input type="text" size=50 name="name0" value="CY11M3 Longrun Ranking/Normal" />
	<br />
	PERIOD=
	FROM
	<input type="text" size=12 name="date0" onchange="onChangeDate()" />
	～
	TO
	<input type="text" size=12 name="date1" onchange="onChangeDate()" />
	<br />
	RULE＝
	<select name="rulestr0" onchange="document.getElementById('ruleid0').value = ''" >
	<option value="LONGRUN" >LONGRUN</option>
	</select>
	×
	幅（W）
	<input type="text" size=3 value=10 name="rulew" />
	×
	高さ（H）
	<input type="text" size=3 value="18" name="ruleh" />
	×
	重力（G）
	<input type="text" size=3 value="10" name="ruleg" />
	<br />
	CATEGORY＝
	<select name="category0" >
	<option value="avgPts" >avgPts</option>
	<option value="avgL" >avgL</option>
	</select>
	<br />
	CNTR=
	<input type="text" size=3 name="cntr0" value=100 />
	<br />
	PRIZE1=
	<input type="text" size=3 name="prize1" value=3 onchange="onChangeWeight()" />
	<br />
	PRIZE2=
	<input type="text" size=3 name="prize2" value=2 onchange="onChangeWeight()" />
	<br />
	PRIZE3=
	<input type="text" size=3 name="prize3" value=1 onchange="onChangeWeight()" />
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
