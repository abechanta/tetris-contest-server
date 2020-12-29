package com.hotmail.abechanta.tetcon;

import java.io.IOException;
import java.sql.Connection;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotmail.abechanta.tetcon.Model.Config;
import com.hotmail.abechanta.tetcon.Model.Contest;
import com.hotmail.abechanta.tetcon.Model.Member;
import com.hotmail.abechanta.tetcon.Model.Program;
import com.hotmail.abechanta.tetcon.Model.Result;
import com.hotmail.abechanta.tetcon.Model.Rule;
import com.hotmail.abechanta.tetcon.Model.Score;
import com.hotmail.abechanta.tetcon.Model.Title;

/**
 * Servlet implementation class QueryTable
 */
@WebServlet("/QueryTable")
public class QueryTable extends DbHttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String encoding = "utf-8";
	private static final String fwdUrl = "query_for_debug.jsp";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		// UI パラメータを取得する。
		//
		request.setCharacterEncoding(encoding);
		String passwd0 = getString(request, "passwd0", "");
		String table0 = getString(request, "table0", "");
		String cond0 = getString(request, "cond0", "");
		Integer fetch0 = getInteger(request, "fetch0", 100);
		Integer id0 = getInteger(request, "id0", 0);

		Vector<String> outArray = new Vector<String>();

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

		//
		// パスワードが合っているか確認する。
		//
		try {
			if (!passwd0.equals(Config.query(conn, "passwd"))) {
				printNg(response, "invalid password.");
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// コンテンツを準備する。
		//
		if (table0.equals("contest")) {
			Vector<Contest> contests = null;
			try {
				contests = cond0.equals("id") ?
					Contest.queryByCid(conn, id0)
				:
					Contest.queryAll(conn, Integer.valueOf(fetch0))
				;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServletException(ex.getMessage());
			}
		
			// エントリをコンテンツに格納する。
			StringTokenizer token = new StringTokenizer(Contest.columns, ",");
			outArray.add(String.valueOf(token.countTokens()));
			while (token.hasMoreTokens()) {
				outArray.add(token.nextToken());
			}
			for (int ii = 0; ii < contests.size(); ii++) {
				outArray.add(String.valueOf(contests.get(ii).getCid()));
				outArray.add(contests.get(ii).getName());
				outArray.add(contests.get(ii).getDate0());
				outArray.add(contests.get(ii).getDate1());
				outArray.add(String.valueOf(contests.get(ii).getRuleid()));
				outArray.add(contests.get(ii).getCategory());
				outArray.add(String.valueOf(contests.get(ii).getCntr()));
				outArray.add(String.valueOf(contests.get(ii).getPrize1()));
				outArray.add(String.valueOf(contests.get(ii).getPrize2()));
				outArray.add(String.valueOf(contests.get(ii).getPrize3()));
				outArray.add(String.valueOf(contests.get(ii).getState()));
				outArray.add(String.valueOf(contests.get(ii).getUpdated()));
			}
		}

		if (table0.equals("member")) {
			Vector<Member> members = null;
			try {
				members = cond0.equals("id") ?
					Member.queryByMid(conn, id0)
				:
					Member.queryAll(conn, Integer.valueOf(fetch0))
				;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServletException(ex.getMessage());
			}
		
			// エントリをコンテンツに格納する。
			StringTokenizer token = new StringTokenizer(Member.columns, ",");
			outArray.add(String.valueOf(token.countTokens()));
			while (token.hasMoreTokens()) {
				outArray.add(token.nextToken());
			}
			for (int ii = 0; ii < members.size(); ii++) {
				outArray.add(String.valueOf(members.get(ii).getMid()));
				outArray.add(members.get(ii).getName());
				outArray.add(members.get(ii).getAuthor());
				outArray.add(String.valueOf(members.get(ii).getRegistered()));
			}
		}

		if (table0.equals("program")) {
			Vector<Program> programs = null;
			try {
				programs = cond0.equals("id") ?
					Program.queryByPid(conn, id0)
				:
					Program.queryAll(conn, Integer.valueOf(fetch0))
				;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServletException(ex.getMessage());
			}
		
			// エントリをコンテンツに格納する。
			StringTokenizer token = new StringTokenizer(Program.columns, ",");
			outArray.add(String.valueOf(token.countTokens()));
			while (token.hasMoreTokens()) {
				outArray.add(token.nextToken());
			}
			for (int ii = 0; ii < programs.size(); ii++) {
				outArray.add(String.valueOf(programs.get(ii).getPid()));
				outArray.add(programs.get(ii).getVer());
				outArray.add(programs.get(ii).getAuthkey());
				outArray.add("<...>");
				outArray.add(String.valueOf(programs.get(ii).getRegistered()));
			}
		}

		if (table0.equals("result")) {
			Vector<Result> results = null;
			try {
				results = cond0.equals("id") ?
					Result.queryByRid(conn, id0)
				:
					Result.queryAll(conn, Integer.valueOf(fetch0))
				;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServletException(ex.getMessage());
			}
		
			// エントリをコンテンツに格納する。
			StringTokenizer token = new StringTokenizer(Result.columns, ",");
			outArray.add(String.valueOf(token.countTokens()));
			while (token.hasMoreTokens()) {
				outArray.add(token.nextToken());
			}
			for (int ii = 0; ii < results.size(); ii++) {
				outArray.add(String.valueOf(results.get(ii).getRid()));
				outArray.add(String.valueOf(results.get(ii).getPid()));
				outArray.add(String.valueOf(results.get(ii).getReported()));
				outArray.add(results.get(ii).getDate());
				outArray.add(results.get(ii).getHost());
				outArray.add(String.valueOf(results.get(ii).getRuleid()));
				outArray.add(String.valueOf(results.get(ii).getR()));
			}
		}

		if (table0.equals("rule")) {
			Vector<Rule> rules = null;
			try {
				rules = cond0.equals("id") ?
					Rule.queryByRuleid(conn, id0)
				:
					Rule.queryAll(conn, Integer.valueOf(fetch0))
				;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServletException(ex.getMessage());
			}
		
			// エントリをコンテンツに格納する。
			StringTokenizer token = new StringTokenizer(Rule.columns, ",");
			outArray.add(String.valueOf(token.countTokens()));
			while (token.hasMoreTokens()) {
				outArray.add(token.nextToken());
			}
			for (int ii = 0; ii < rules.size(); ii++) {
				outArray.add(String.valueOf(rules.get(ii).getRuleid()));
				outArray.add(rules.get(ii).getStr());
				outArray.add(String.valueOf(rules.get(ii).getW()));
				outArray.add(String.valueOf(rules.get(ii).getH()));
				outArray.add(String.valueOf(rules.get(ii).getG()));
			}
		}

		if (table0.equals("score")) {
			Vector<Score> scores = null;
			try {
				scores = cond0.equals("id") ?
					Score.queryBySid(conn, id0)
				:
					Score.queryAll(conn, Integer.valueOf(fetch0))
				;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServletException(ex.getMessage());
			}
		
			// エントリをコンテンツに格納する。
			StringTokenizer token = new StringTokenizer(Score.columns, ",");
			outArray.add(String.valueOf(token.countTokens()));
			while (token.hasMoreTokens()) {
				outArray.add(token.nextToken());
			}
			for (int ii = 0; ii < scores.size(); ii++) {
				outArray.add(String.valueOf(scores.get(ii).getSid()));
				outArray.add(String.valueOf(scores.get(ii).getMid()));
				outArray.add(String.valueOf(scores.get(ii).getRid()));
				outArray.add(String.valueOf(scores.get(ii).getP()));
				outArray.add(String.valueOf(scores.get(ii).getL()));
				outArray.add(String.valueOf(scores.get(ii).getL1()));
				outArray.add(String.valueOf(scores.get(ii).getL2()));
				outArray.add(String.valueOf(scores.get(ii).getL3()));
				outArray.add(String.valueOf(scores.get(ii).getL4()));
				outArray.add(String.valueOf(scores.get(ii).getX()));
				outArray.add(String.valueOf(scores.get(ii).getSi()));
				outArray.add(String.valueOf(scores.get(ii).getSd()));
				outArray.add(String.valueOf(scores.get(ii).getPr()));
				outArray.add(String.valueOf(scores.get(ii).getLr()));
				outArray.add(String.valueOf(scores.get(ii).getRr()));
			}
		}

		if (table0.equals("title")) {
			Vector<Title> titles = null;
			try {
				titles = cond0.equals("id") ?
					Title.queryByTid(conn, id0)
				:
					Title.queryAll(conn, Integer.valueOf(fetch0))
				;
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServletException(ex.getMessage());
			}
		
			// エントリをコンテンツに格納する。
			StringTokenizer token = new StringTokenizer(Title.columns, ",");
			outArray.add(String.valueOf(token.countTokens()));
			while (token.hasMoreTokens()) {
				outArray.add(token.nextToken());
			}
			for (int ii = 0; ii < titles.size(); ii++) {
				outArray.add(String.valueOf(titles.get(ii).getTid()));
				outArray.add(String.valueOf(titles.get(ii).getMid()));
				outArray.add(String.valueOf(titles.get(ii).getName()));
				outArray.add(String.valueOf(titles.get(ii).getPrize()));
				outArray.add(String.valueOf(titles.get(ii).getRegistered()));
			}
		}

		//
		// リクエストにコンテンツを添付する。
		//
		request.setAttribute("outArray", outArray);

		//
		// リクエストを JSP に転送する。
		//
		RequestDispatcher dispatch = request.getRequestDispatcher(fwdUrl);
		dispatch.forward(request, response);
	}

}
