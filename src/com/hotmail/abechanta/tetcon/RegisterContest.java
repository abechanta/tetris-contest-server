package com.hotmail.abechanta.tetcon;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotmail.abechanta.tetcon.Model.Config;
import com.hotmail.abechanta.tetcon.Model.Contest;
import com.hotmail.abechanta.tetcon.Model.Rule;

/**
 * Servlet implementation class RegisterContest
 */
public class RegisterContest extends DbHttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String encoding = "utf-8";

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		// UI パラメータを取得する。
		//
		request.setCharacterEncoding(encoding);
		String passwd0 = getString(request, "passwd0", "");
		String name0 = getString(request, "name0", "");
		String date0 = getString(request, "date0", "");
		String date1 = getString(request, "date1", "");
		String rulestr0 = getString(request, "rulestr0", "");
		Integer rulew = getInteger(request, "rulew", 10);
		Integer ruleh = getInteger(request, "ruleh", 18);
		Integer ruleg = getInteger(request, "ruleg", 10);
		String category0 = getString(request, "category0", "");
		Integer cntr0 = getInteger(request, "cntr0", 0);
		Integer prize1 = getInteger(request, "prize1", 0);
		Integer prize2 = getInteger(request, "prize2", 0);
		Integer prize3 = getInteger(request, "prize3", 0);

		String date0tmp = date0 + "000000";
		String date1tmp = date1 + "999999";

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
		// すでに登録が無いかコンテストを検索する。
		//
		Vector<Contest> contests = null;
		try {
			contests = Contest.queryByName(conn, name0);
			if (contests.size() > 1) {
				throw new ServletException("db integrity issue occurred.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// 一致する ruleid を検索する。
		//
		Rule rule0 = new Rule(0, rulestr0, rulew, ruleh, ruleg);
		try {
			rule0.validate(conn);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// コンテストを登録する。
		// すでに登録が有る場合は、バイナリを再登録し、認証鍵を再発行する。
		//
		Contest contest0 = (contests.size() > 0) ?
			contests.get(0)		// エントリ有り → 更新。
		:
			new Contest(
				0,
				name0,
				date0tmp,
				date1tmp,
				rule0.getRuleid(),
				category0,
				cntr0,
				prize1,
				prize2,
				prize3,
				Contest.STATE_READY,
				new Timestamp(0)
			)					// エントリ無し → 登録。
		;
		if (contests.size() > 0) {
			contest0.setDate0(date0tmp);
			contest0.setDate1(date1tmp);
			contest0.setRuleid(rule0.getRuleid());
			contest0.setCategory(category0);
			contest0.setCntr(cntr0);
			contest0.setPrize1(prize1);
			contest0.setPrize2(prize2);
			contest0.setPrize3(prize3);
		}

		try {
			if (contest0.validate(conn) == 0) {
				throw new ServletException("invalid arg.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// DB にコミットする。
		//
		try {
			conn.commit();
		} catch (Exception ex) {
			if (conn != null) {
				try {
					// DB をロールバックする。
					conn.rollback();
				} catch (Exception innerEx) {
					ex = innerEx;
				}
			}
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// 結果を返信する。
		//
		printOk(response, "done.");
	}
}
