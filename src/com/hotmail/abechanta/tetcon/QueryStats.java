package com.hotmail.abechanta.tetcon;

import java.io.IOException;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotmail.abechanta.tetcon.Model.Member;
import com.hotmail.abechanta.tetcon.Model.Rule;
import com.hotmail.abechanta.tetcon.Model.ScoreForStats;
import com.hotmail.abechanta.tetcon.Model.Title;

/**
 * Servlet implementation class QueryStats
 */
@WebServlet("/QueryStats")
public class QueryStats extends DbHttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String encoding = "utf-8";
	private static final String fwdUrl = "member_statistics.jsp";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		// UI パラメータを取得する。
		//
		request.setCharacterEncoding(encoding);
		Integer mid0 = getInteger(request, "mid0", 0);
		String name0 = getString(request, "name0", "");
		String author0 = getString(request, "author0", "");
		Integer ruleid0 = getInteger(request, "ruleid0", 0);
		String rulestr0 = getString(request, "rulestr0", "");
		Integer rulew = getInteger(request, "rulew", 10);
		Integer ruleh = getInteger(request, "ruleh", 18);
		Integer ruleg = getInteger(request, "ruleg", 10);
		getString(request, "period0", "");
		String date0 = getString(request, "date0", "");
		String date1 = getString(request, "date1", "");

		String date0tmp = date0.equals("") ? "000000" : date0;
		date0tmp += "000000";
		String date1tmp = date1.equals("") ? "999999" : date1;
		date1tmp += "999999";

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

		Vector<Rule> outRule = null;
		try {
			if (ruleid0 == 0) {
				// rulestr0 / rulew / ruleh / ruleg からエントリを検索する。
				outRule = Rule.queryByRule(conn, rulestr0, rulew, ruleh, ruleg);
			} else {
				// ruleid が分かっていれば ruleid からエントリを検索する。
				outRule = Rule.queryByRuleid(conn, ruleid0);
			}
			if (outRule.size() > 0) {
				ruleid0 = outRule.get(0).getRuleid();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		Vector<Member> outMember = null;
		try {
			if (mid0 == 0) {
				// name0 / author0 からエントリを検索する。複数エントリが見つかるかもしれない。
				outMember = Member.queryByNameAndAuthor(conn, name0, author0);
			} else {
				// mid が分かっていれば mid からエントリを検索する。
				outMember = Member.queryByMid(conn, mid0);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		Hashtable<String, String> outStat = new Hashtable<String, String>();
		if (
			(outRule.size() == 1) &&
			(outMember.size() == 1)
		) {
			mid0 = outMember.get(0).getMid();

			// メンバ成績を検索する。
			try {
				Hashtable<String, Integer> stat = null;
				stat = ScoreForStats.queryStats(conn, mid0, ruleid0, date0tmp, date1tmp, "total");
				for (Enumeration<String> it = stat.keys(); it.hasMoreElements(); ) {
					String tmp = it.nextElement();
					outStat.put(tmp, String.valueOf(stat.get(tmp)));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServletException(ex.getMessage());
			}

			// メンバのタイトルを検索する。
			try {
				String out = "none";
				Vector<Title> titles = Title.queryByMid(conn, mid0);
				if ((titles != null) && (titles.size() > 0)) {
					out = titles.get(0).getName();
					for (int ii = 1; ii < titles.size(); ii++) {
						out += " / " + titles.get(ii).getName();
					}
				}
				outStat.put("title0", out);
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServletException(ex.getMessage());
			}
		}

		//
		// リクエストにコンテンツを添付する。
		//
		request.setAttribute("mid0", String.valueOf(mid0));
		request.setAttribute("ruleid0", String.valueOf(ruleid0));
		request.setAttribute("outMember", outMember);
		request.setAttribute("outRule", outRule);
		request.setAttribute("outStat", outStat);

		//
		// リクエストを JSP に転送する。
		//
		RequestDispatcher dispatch = request.getRequestDispatcher(fwdUrl);
		dispatch.forward(request, response);
	}

}
