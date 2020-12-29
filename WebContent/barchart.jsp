<?xml version="1.0" standalone="no"?>
<%@ page contentType="image/svg-xml; charset=utf-8"
	import="java.util.Vector"
%>
<jsp:useBean id="outArray" class="java.util.Vector" scope="request" />
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
<%
	int width = 500;
	int height = 50;
	int margin = 10;

	int total = 0;
	for (int ii = 0; ii < outArray.size(); ii++) {
		total += (Integer)outArray.get(ii) * (1 + ii);
	}
%>
<svg width="<%= width %>" height="<%= height %>"
  xmlns="http://www.w3.org/2000/svg"
  xmlns:xlink="http://www.w3.org/1999/xlink">
	<desc><%= request.getRequestURI() %></desc>
<%
	int xx0 = margin;
	int xx1 = width - margin;
	int yy0 = margin;
	int yy1 = height - margin;
	String fontFamily = "courier new";
	int fontSize = 9;
	String label = "L";

	if (total > 0) {
		//
		// draws percent stacked bar chart.
		//
		String color[] = { "#80ff80", "#c0ff80", "#ffff80", "#ffc080", };
		int xx = 0;

		for (int ii = 0; ii < outArray.size(); ii++) {
			int ww = (xx1 - xx0) * (Integer)outArray.get(ii) * (1 + ii) / total;
				if (ww > 0) {
%>
	<rect x="<%= xx0 + xx %>" y="<%= yy0 %>" width="<%= ww %>" height="<%= yy1 - yy0 %>" style="fill:<%= color[ii % color.length] %>;" />
	<text x="<%= xx0 + xx + ww / 2 %>" y="<%= yy0 %>" font-family="<%= fontFamily %>" font-size="<%= fontSize %>" fill="black" text-anchor="middle"><%= label %><%= 1 + ii %></text>
	<text x="<%= xx0 + xx + ww / 2 %>" y="<%= yy0 + (fontSize * ((ii & 1) + 1)) %>" font-family="<%= fontFamily %>" font-size="<%= fontSize %>" fill="black" text-anchor="middle"><%= 100 * (Integer)outArray.get(ii) * (1 + ii) / total %>%</text>
<%
			}
			xx = xx + ww;
		}
	} else {
		//
		// no data found.
		//
%>
		<rect x="<%= xx0 %>" y="<%= yy0 %>" width="<%= xx1 - xx0 %>" height="<%= yy1 - yy0 %>" style="fill:white; stroke:black;" />
<%
	}
%>
</svg>
