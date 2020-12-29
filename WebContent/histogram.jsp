<?xml version="1.0" standalone="no"?>
<%@ page contentType="image/svg-xml; charset=utf-8"
	import="java.util.Vector"
%>
<jsp:useBean id="outArray" class="java.util.Vector" scope="request" />
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<%
	int min = 0;
	int max = 100;
	int step = 5;
	int histo[] = new int[(max + step - 1) / step];

	int width = 500;
	int height = 250;
	int margin = 10;

	for (int ii = 0; ii < outArray.size(); ii++) {
		int val = (Integer)outArray.get(ii);
		int idx = (val - min) / step;
		if (val < min) {
			idx = 0;
		} else if (val >= max) {
			idx = histo.length - 1;
		}
		histo[idx]++;
	}

	int maxcount = 1;	// 1 for dummy.
	for (int ii = 0; ii < histo.length; ii++) {
		if (maxcount < histo[ii]) {
			maxcount = histo[ii];
		}
	}
%>
<svg width="<%= width %>" height="<%= height %>"
  xmlns="http://www.w3.org/2000/svg"
  xmlns:xlink="http://www.w3.org/1999/xlink">
	<desc><%= request.getRequestURI() %></desc>
<%
	int xx0 = margin;
	int xx1 = width - margin;
	int yy0 = height - margin;
	int yy1 = margin;
	int ww = (xx1 - xx0) / histo.length;
	String fontFamily = "courier new";
	int fontSize = 9;
%>
	<line x1="<%= xx0 %>" y1="<%= yy0 %>" x2="<%= xx0 %>" y2="<%= yy1 %>" style="fill:black;"/>
	<line x1="<%= xx0 %>" y1="<%= yy0 %>" x2="<%= xx1 %>" y2="<%= yy0 %>" style="fill:black;"/>
	<text x="<%= xx1 %>" y="<%= yy0 + fontSize %>" font-family="<%= fontFamily %>" font-size="<%= fontSize %>" fill="black" text-anchor="middle">[%]</text>
<%
	//
	// draws histogram.
	//
	for (int ii = 0; ii < histo.length; ii++) {
		int hh = (yy0 - yy1) * histo[ii] / maxcount;
			if (hh > 0) {
%>
	<rect x="<%= xx0 + ii * ww %>" y="<%= yy0 - hh %>" width="<%= ww %>" height="<%= hh %>" style="fill:<%= (ii & 1) == 0 ? "#c0ffc0" : "#c0e0e0" %>;" />
<%
		}
%>
	<text x="<%= xx0 + ii * ww + ww / 2 %>" y="<%= yy0 - hh %>" font-family="<%= fontFamily %>" font-size="<%= fontSize %>" fill="black" text-anchor="middle"><%= histo[ii] %></text>
<%
		if ((ii % 2) == 0) {
%>
	<text x="<%= xx0 + ii * ww %>" y="<%= yy0 + fontSize %>" font-family="<%= fontFamily %>" font-size="<%= fontSize %>" fill="black" text-anchor="middle"><%= ii * step %></text>
<%
		}
	}
%>
</svg>
