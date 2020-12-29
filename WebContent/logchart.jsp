<?xml version="1.0" standalone="no"?>
<%@ page contentType="image/svg-xml; charset=utf-8"
	import="java.util.Vector"
%>
<jsp:useBean id="max" class="java.lang.String" scope="request" />
<jsp:useBean id="unit" class="java.lang.String" scope="request" />
<jsp:useBean id="labelL" class="java.lang.String" scope="request" />
<jsp:useBean id="labelR" class="java.lang.String" scope="request" />
<jsp:useBean id="outArray" class="java.util.Vector" scope="request" />
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<%
	int borderwidth = 3;
	int margin = 10;
	int width = 200 * borderwidth + 5 * margin;
	int height = 120;

	int base = Integer.valueOf(max);
	int maxval = Integer.valueOf(max);
	for (int ii = 0; ii < outArray.size(); ii++) {
		int val = (Integer)outArray.get(ii);
		if (maxval < val) {
			maxval = val;
		}
	}
%>
<svg width="100%" height="100%" viewBox="0 0 <%= width %> <%= height %>"
  xmlns="http://www.w3.org/2000/svg"
  xmlns:xlink="http://www.w3.org/1999/xlink">
	<desc><%= request.getRequestURI() %></desc>
<%
	int xx0 = 3 * margin;
	int xx1 = width - 2 * margin;
	int yy0 = height - margin;
	int yy1 = margin;
	String fontFamily = "courier new";
	int fontSize = 9;
	int strength = 25;
	int sum = 0;
	int lasthh = 0;

	//
	// draws logchart
	//
	for (int ii = 0; ii < outArray.size(); ii++) {
		int ww = borderwidth;
		int hh = (yy0 - yy1) * (Integer)outArray.get(outArray.size() - 1 - ii) / maxval;
		if (hh > 0) {
%>
	<rect x="<%= xx0 + ii * ww %>" y="<%= yy0 - hh %>" width="<%= ww %>" height="<%= hh %>" style="fill:#c0c0ff;" />
<%
		}

		sum += (Integer)outArray.get(outArray.size() - 1 - ii);
		if (ii >= strength) {
			sum -= (Integer)outArray.get(outArray.size() - 1 - (ii - strength));
		}
		int nowhh = (yy0 - yy1) * sum / strength / maxval;
		if (ii > strength) {
%>
	<line x1="<%= xx0 + (ii - 1) * ww %>" y1="<%= yy0 - lasthh %>" x2="<%= xx0 + ii * ww %>" y2="<%= yy0 - nowhh %>" style="fill:green;" />
<%
		}
		lasthh = nowhh;
	}
%>
	<line x1="<%= xx0 %>" y1="<%= yy0 %>" x2="<%= xx0 %>" y2="<%= yy1 %>" style="fill:black;"/>
	<line x1="<%= xx0 %>" y1="<%= yy0 %>" x2="<%= xx1 %>" y2="<%= yy0 %>" style="fill:black;"/>
	<line x1="<%= xx0 %>" y1="<%= yy0 - (yy0 - yy1) * base / maxval %>" x2="<%= xx1 %>" y2="<%= yy0 - (yy0 - yy1) * base / maxval %>" style="fill:red;"/>
	<text x="<%= xx0 %>" y="<%= yy0 - (yy0 - yy1) * base / maxval %>" font-family="<%= fontFamily %>" font-size="<%= fontSize %>" fill="black" text-anchor="end"><%= max %><%= unit %></text>
<!--
	<text x="<%= xx0 %>" y="<%= yy0 %>" font-family="<%= fontFamily %>" font-size="<%= fontSize %>" fill="black" text-anchor="end">0<%= unit %></text>
-->
	<text x="<%= xx0 %>" y="<%= yy0 + fontSize %>" font-family="<%= fontFamily %>" font-size="<%= fontSize %>" fill="black" text-anchor="middle"><%= labelL %></text>
	<text x="<%= xx1 %>" y="<%= yy0 + fontSize %>" font-family="<%= fontFamily %>" font-size="<%= fontSize %>" fill="black" text-anchor="middle"><%= labelR %></text>
</svg>
