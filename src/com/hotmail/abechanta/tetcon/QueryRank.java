package com.hotmail.abechanta.tetcon;

import java.io.IOException;
import java.sql.Connection;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotmail.abechanta.tetcon.Model.Contest;
import com.hotmail.abechanta.tetcon.Model.Rank;

/**
 * Servlet implementation class QueryRank
 */
@WebServlet("/QueryRank")
public class QueryRank extends DbHttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String encoding = "utf-8";
	private static final String fwdUrl = "member_ranking.jsp";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		// UI パラメータを取得する。
		//
		request.setCharacterEncoding(encoding);
		Integer cid0 = getInteger(request, "cid0", 0);
		getInteger(request, "contest0", 0);
		getInteger(request, "contest1", 0);

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

		Vector<Contest> outContest = null;
		try {
			outContest = Contest.queryAll(conn, 0);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		Vector<Rank> outRank = null;
		try {
			for (int ii = 0; ii < outContest.size(); ii++) {
				if (outContest.get(ii).getCid() == cid0) {
					outRank = Rank.queryAll(conn, outContest.get(ii));
					break;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// リクエストにコンテンツを添付する。
		//
		request.setAttribute("cid0", cid0.toString());
		request.setAttribute("outContest", outContest);
		request.setAttribute("outRank", outRank);

		//
		// リクエストを JSP に転送する。
		//
		RequestDispatcher dispatch = request.getRequestDispatcher(fwdUrl);
		dispatch.forward(request, response);
	}

}
