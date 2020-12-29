package com.hotmail.abechanta.tetcon.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

public class Rule {
	private int ruleid;
	private String str;
	private int w;
	private int h;
	private int g;
	public static final String columns = "ruleid, str, w, h, g";

	public static Rule decode(Hashtable<String, String> tbl) {
		return new Rule(
			0,
			tbl.get("RULE"),
			Integer.valueOf(tbl.get("W")),
			Integer.valueOf(tbl.get("H")),
			Integer.valueOf(tbl.get("G"))
		);
	}

	private static Rule decode(ResultSet res) throws SQLException {
		return new Rule(
			res.getInt(1),
			res.getString(2),
			res.getInt(3),
			res.getInt(4),
			res.getInt(5)
		);
	}

	private static String getStatement_Fetch(int fetch) {
		return "FETCH first " + fetch + " ROWS ONLY";
	}

	public Rule(
		int ruleid,
		String str,
		int w,
		int h,
		int g
	) {
		this.ruleid = ruleid;
		this.str = str;
		this.w = w;
		this.h = h;
		this.g = g;
	}

	public boolean isValid() {
		return this.ruleid != 0;
	}

	private static Vector<Rule> query(Connection conn, String filterCond, String filterFetch) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Vector<Rule> rv = new Vector<Rule>();
		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを検索する。
			//
			progress = "Rule.query: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Rule.query: query result.";
			statement =
				"select " + Rule.columns +
				"	from tetcon.rule" +
				"	" + filterCond +
				"	order by ruleid desc" +
				"	" + filterFetch;
			res = stmt.executeQuery(statement);

			while (res.next()) {
				//
				// 見つかったエントリを戻り値に格納する。
				//
				rv.add(decode(res));
			}

			progress = "Rule.query: done.";
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

	public static Vector<Rule> queryAll(Connection conn, int fetch) throws SQLException {
		String filterCond = "";
		String filterFetch = (fetch == 0) ? "" : getStatement_Fetch(fetch);
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Rule> queryByRuleid(Connection conn, int ruleid) throws SQLException {
		String filterCond = "where " + getStatement_Ruleid(ruleid);
		String filterFetch = getStatement_Fetch(1);
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Rule> queryByRule(Connection conn, String str, int w, int h, int g) throws SQLException {
		String filterCond = "where " + getStatement_Str(str) + " and " + getStatement_W(w) + " and " + getStatement_H(h) + " and " + getStatement_G(g);
		String filterFetch = getStatement_Fetch(1);
		return query(conn, filterCond, filterFetch);
	}

	public int validate(Connection conn) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// すでにエントリが無いか検索してみる。
			//
			progress = "Rule.validate: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Rule.validate: query rule.";
			statement =
				"select " + Rule.columns +
				"	from tetcon.rule" +
				"	where" +
				"		str = '" + this.str + "'" +
				"		and w = " + this.w +
				"		and h = " + this.h +
				"		and g = " + this.g;
			res = stmt.executeQuery(statement);

			if (!res.next()) {
				//
				// エントリが無いので登録する。
				//
				progress = "Rule.validate: insert rule.";
				statement =
					"insert into tetcon.rule (" + Rule.columns + ")" +
					"	values (" +
					"		default," +
					"		'" + this.str + "'," +
					"		" + this.w + "," +
					"		" + this.h + "," +
					"		" + this.g +
					"	)";
				stmt.executeUpdate(statement);

				//
				// 登録したエントリを検索する。
				//
				progress = "Rule.validate: re-query rule.";
				statement =
					"select " + Rule.columns +
					"	from tetcon.rule" +
					"	where" +
					"		str = '" + this.str + "'" +
					"		and w = " + this.w +
					"		and h = " + this.h +
					"		and g = " + this.g;
				res = stmt.executeQuery(statement);
				if (!res.next()) {
					//
					// 登録されてなきゃおかしい。
					//
					throw new SQLException(progress + " not found.");
				}
			}

			//
			// 見つかったエントリをインスタンスに格納する。
			//
			this.ruleid = res.getInt(1);
//			this.str = res.getString(2);
//			this.w = res.getInt(3);
//			this.h = res.getInt(4);
//			this.g = res.getInt(5);

			progress = "Rule.validate: done.";
			res.close();
			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

		return getRuleid();
	}

	public int getRuleid() {
		return this.ruleid;
	}
	private static String getStatement_Ruleid(int ruleid) {
		return "ruleid = " + ruleid;
	}

	public String getStr() {
		return this.str;
	}
	private static String getStatement_Str(String str) {
		return "str = '" + str + "'";
	}

	public int getW() {
		return this.w;
	}
	private static String getStatement_W(int w) {
		return "w = " + w;
	}

	public int getH() {
		return this.h;
	}
	private static String getStatement_H(int h) {
		return "h = " + h;
	}

	public int getG() {
		return this.g;
	}
	private static String getStatement_G(int g) {
		return "g = " + g;
	}

}
