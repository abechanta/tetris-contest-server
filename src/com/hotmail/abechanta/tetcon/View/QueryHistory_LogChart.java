package com.hotmail.abechanta.tetcon.View;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotmail.abechanta.tetcon.DbAccess;
import com.hotmail.abechanta.tetcon.DbHttpServlet;
import com.hotmail.abechanta.tetcon.Model.ScoreForStats;

/**
 * Servlet implementation class QueryHistory_LogChart
 */
@WebServlet("/QueryHistory_LogChart")
public class QueryHistory_LogChart extends DbHttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String encoding = "utf-8";
	private static final String fwdUrl = "logchart.jsp";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		// UI パラメータを取得する。
		//
		request.setCharacterEncoding(encoding);
		getString(request, "max", "");
		getString(request, "unit", "");
		Integer offset0 = getInteger(request, "offset0", 0);
		String category0 = getString(request, "category0", "");
		Integer mid0 = getInteger(request, "mid0", 0);
		Integer ruleid0 = getInteger(request, "ruleid0", 0);
		String date0 = getString(request, "date0", "");
		String date1 = getString(request, "date1", "");

		String date0tmp = date0.equals("") ? "000000" : date0;
		date0tmp += "000000";
		String date1tmp = date1.equals("") ? "999999" : date1;
		date1tmp += "999999";

		Vector<Integer> outArray = null;

		//
		// コンテンツを準備する。
		//
		Connection conn = null;
		try {
			conn = DbAccess.getConn(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		try {
			outArray = ScoreForStats.queryHistory(conn, mid0, ruleid0, date0tmp, date1tmp, category0, offset0);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// リクエストにコンテンツを添付する。
		//
		request.setAttribute("labelL", "yymmdd");	// TODO
		request.setAttribute("labelR", "yymmdd");
		request.setAttribute("outArray", outArray);

		//
		// リクエストを JSP に転送する。
		//
		RequestDispatcher dispatch = request.getRequestDispatcher(fwdUrl);
		dispatch.forward(request, response);
	}

}
