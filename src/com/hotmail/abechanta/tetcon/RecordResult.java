package com.hotmail.abechanta.tetcon;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotmail.abechanta.tetcon.Model.Member;
import com.hotmail.abechanta.tetcon.Model.Program;
import com.hotmail.abechanta.tetcon.Model.Result;
import com.hotmail.abechanta.tetcon.Model.Rule;
import com.hotmail.abechanta.tetcon.Model.Score;

/**
 * Servlet implementation class RecordResult
 */
@WebServlet("/RecordResult")
public class RecordResult extends DbHttpServlet {
	private static final long serialVersionUID = 1L;

	private Vector<Hashtable<String, String>> parseContentBody(String in) {
		Vector<Hashtable<String, String>> rv = new Vector<Hashtable<String, String>>();
		StringTokenizer token = new StringTokenizer(in, ",");

		while (token.hasMoreElements()) {
			String args = token.nextToken();
			Hashtable<String, String> elem = parseContentBody_Elem(args);
			if (elem.size() >= 1) {
				rv.add(elem);
			}
		}
		return rv;
	}

	private Hashtable<String, String> parseContentBody_Elem(String in) {
		Hashtable<String, String> rv = new Hashtable<String, String>();
		StringTokenizer token = new StringTokenizer(in, "&");

		while (token.hasMoreElements()) {
			String args = token.nextToken();
			Vector<String> elem = parseContentBody_Kv(args);
			if (elem.size() >= 1) {
				rv.put(elem.get(0), elem.size() >= 2 ? elem.get(1) : "");
			}
		}
		return rv;
	}

	private Vector<String> parseContentBody_Kv(String in) {
		Vector<String> rv = new Vector<String>();
		StringTokenizer token = new StringTokenizer(in, "=");

		while (token.hasMoreElements()) {
			String args = token.nextToken();
			rv.add(args);
		}
		return rv;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		// パラメータを取得する。
		//
		request.setCharacterEncoding("UTF-16LE");
		BufferedReader in = request.getReader();

		//
		// パラメータを解析する。
		//
		String reportContent = in.readLine();
		Vector<Hashtable<String, String>> body = parseContentBody(reportContent);
		if (body.size() < 2) {
			// ContentBody が不足している。
			throw new ServletException("parse error.");
		}

		//
		// パラメータ#0 は tetcon.program / tetcon.rule / tetcon.result のもの。
		//
		Rule rule0 = Rule.decode(body.get(0));
		Program program0 = Program.decode(body.get(0));
		Result result0 = Result.decode(body.get(0));

		//
		// パラメータ#1 以降は tetcon.member / tetcon.score のもの。
		//
		Vector<Member> members = new Vector<Member>();
		Vector<Score> scores = new Vector<Score>();
		for (int ii = 1; ii < body.size(); ii++) {
			members.add(Member.decode(body.get(ii)));
			scores.add(Score.decode(body.get(ii)));
		}

		//
		// コネクションを確立する。
		//
		Connection conn = null;
		try {
			conn = DbAccess.getConn(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// ルールを登録する。
		//
		int ruleid = 0;
		try {
			ruleid = rule0.validate(conn);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// プログラムを検索する。
		//
		Vector<Program> programs = null;
		try {
			programs = Program.queryByVer(conn, program0.getVer());
			if (programs.size() != 1) {
				throw new ServletException("db integrity issue occurred.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// レポートを検証する。
		//
		program0 = programs.get(0);
		try {
			if (!program0.verifyIntegrity(reportContent)) {
				throw new ServletException("program integrity issue occurred.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// 結果を登録する。
		//
		int pid = program0.getPid();
		result0.setPid(pid);
		result0.setRuleid(ruleid);
		int rid = 0;
		try {
			rid = result0.validate(conn);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		try {
			for (int ii = 0; ii < members.size(); ii++) {
				//
				// メンバを登録する。
				//
				int mid = members.get(ii).validate(conn);

				//
				// スコアを登録する。
				//
				scores.get(ii).setMid(mid);
				scores.get(ii).setRid(rid);
				scores.get(ii).validate(conn);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		try {
			conn.commit();
		} catch (Exception ex) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (Exception innerEx) {
					ex = innerEx;
				}
			}
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}
	}
}
