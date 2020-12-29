package com.hotmail.abechanta.tetcon.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ScoreForStats {
	private String key;
	private String code;

	private static Hashtable<String, Vector<ScoreForStats>> customCategory = null;
	static {
		ScoreForStats.customCategory = new Hashtable<String, Vector<ScoreForStats>>();
		{
			Vector<ScoreForStats> entry = new Vector<ScoreForStats>();
			entry.add(new ScoreForStats("cntR", "count(sid)"));
			entry.add(new ScoreForStats("avgPr", "avg(pr)"));
			entry.add(new ScoreForStats("avgLr", "avg(lr)"));
			entry.add(new ScoreForStats("avgRr", "avg(rr)"));
			entry.add(new ScoreForStats("avgP", "avg(p)"));
			entry.add(new ScoreForStats("maxP", "max(p)"));
			entry.add(new ScoreForStats("avgL", "avg(l)"));
			entry.add(new ScoreForStats("maxL", "max(l)"));
			entry.add(new ScoreForStats("sumX", "sum(x)"));
			ScoreForStats.customCategory.put("total", entry);
		}
		{
			Vector<ScoreForStats> entry = new Vector<ScoreForStats>();
			entry.add(new ScoreForStats("sumL1", "sum(l1)"));
			entry.add(new ScoreForStats("sumL2", "sum(l2)"));
			entry.add(new ScoreForStats("sumL3", "sum(l3)"));
			entry.add(new ScoreForStats("sumL4", "sum(l4)"));
			ScoreForStats.customCategory.put("lsum", entry);
		}
	}

	ScoreForStats(String key, String code) {
		this.key  = key;
		this.code = code;
	}

	private static String getTarget(String category)
	{
		String rv = "";
		for (Enumeration<ScoreForStats> elem = ScoreForStats.customCategory.get(category).elements(); elem.hasMoreElements(); ) {
			rv += ((rv.equals("")) ? "" : ", ") + elem.nextElement().code;
		}
		return rv;
	}

	private static String getStatement_Stats(int mid, int ruleid, String date0, String date1, String category) {
		if (mid == 0) {
			return null;
		}
		if (ruleid == 0) {
			return null;
		}
		return
			"select " + getTarget(category) +
			"	from tetcon.score" +
			"	where" +
			"		rid in (" +
			"			select rid" +
			"				from tetcon.result" +
			"				where" +
			"					ruleid = " + ruleid +
			"					and '" + date0 + "' <= date" +
			"					and date <= '" + date1 + "'" +
			"		)" +
			"		and mid = " + mid
		;
	}

	private static String getStatement_History(int mid, int ruleid, String date0, String date1, String category) {
		if (mid == 0) {
			return null;
		}
		if (ruleid == 0) {
			return null;
		}
		return
			"select tetcon.score." + category +
			"	from tetcon.score" +
			"	where" +
			"		rid in (" +
			"			select rid" +
			"				from tetcon.result" +
			"				where" +
			"					ruleid = " + ruleid +
			"					and '" + date0 + "' <= date" +
			"					and date <= '" + date1 + "'" +
			"		)" +
			"		and mid = " + mid
		;
	}

	public static Hashtable<String, Integer> queryStats(Connection conn, int mid, int ruleid, String date0, String date1, String category) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}
		if (category == null) {
			return null;
		}

		Hashtable<String, Integer> rv = new Hashtable<String, Integer>();
		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// 与えられた諸条件からメンバ成績を集計する。
			//
			progress = "ScoreForStats.queryStats: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "ScoreForStats.queryStats: query member-stats-category.";
			statement = getStatement_Stats(mid, ruleid, date0, date1, category);
			res = stmt.executeQuery(statement);

			if (res.next()) {
				//
				// 集計結果を戻り値に格納する。
				//
				int ii = 1;
				for (Enumeration<ScoreForStats> elem = ScoreForStats.customCategory.get(category).elements(); elem.hasMoreElements(); ) {
					rv.put(elem.nextElement().key, res.getInt(ii++));
				}
			}

			progress = "ScoreForStats.queryStats: done.";
			res.close();
			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

		return rv;
	}

	public static Vector<Integer> queryHistory(Connection conn, int mid, int ruleid, String date0, String date1, String category, int offset) throws SQLException {
		// TODO: add offset
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}
		if (category == null) {
			return null;
		}

		Vector<Integer> rv = new Vector<Integer>();
		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// 与えられた諸条件から試合結果を検索する。
			//
			progress = "ScoreForStats.queryHistory: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "ScoreForStats.queryHistory: query member-history-category.";
			statement = getStatement_History(mid, ruleid, date0, date1, category);
			res = stmt.executeQuery(statement);

			while (res.next()) {
				//
				// 見つかったエントリを戻り値に格納する。
				//
				rv.addElement(res.getInt(1));
			}

			progress = "ScoreForStats.queryHistory: done.";
			res.close();
			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

		return rv;
	}

	public static Vector<Integer> queryHistory(Connection conn, int mid, int ruleid, String date0, String date1, String category) throws SQLException {
		return queryHistory(conn, mid, ruleid, date0, date1, category, 0);
	}

}
