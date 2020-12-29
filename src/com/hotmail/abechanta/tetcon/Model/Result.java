package com.hotmail.abechanta.tetcon.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

public class Result {
	private int rid;
	private int pid;
	private Timestamp reported;
	private String date;
	private String host;
	private int ruleid;
	private int r;
	public static final String columns = "rid, pid, reported, date, host, ruleid, r";

	public static Result decode(Hashtable<String, String> tbl) {
		return new Result(
			0,
			0,
			new Timestamp(0),
			tbl.get("DATE"),
			tbl.get("HOST"),
			0,
			Integer.valueOf(tbl.get("R"))
		);
	}

	private static Result decode(ResultSet res) throws SQLException {
		return new Result(
			res.getInt(1),
			res.getInt(2),
			res.getTimestamp(3),
			res.getString(4),
			res.getString(5),
			res.getInt(6),
			res.getInt(7)
		);
	}

	private static String getStatement_Fetch(int fetch) {
		return "FETCH first " + fetch + " ROWS ONLY";
	}

	public Result(
		int rid,
		int pid,
		Timestamp reported,
		String date,
		String host,
		int ruleid,
		int r
	) {
		this.rid = rid;
		this.pid = pid;
		this.reported = reported;
		this.date = date;
		this.host = host;
		this.ruleid = ruleid;
		this.r = r;
	}

	private static Vector<Result> query(Connection conn, String filterCond, String filterFetch) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Vector<Result> rv = new Vector<Result>();
		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを検索する。
			//
			progress = "Result.query: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Result.query: query result.";
			statement =
				"select " + Result.columns +
				"	from tetcon.result" +
				"	" + filterCond +
				"	order by reported desc" +
				"	" + filterFetch;
			res = stmt.executeQuery(statement);

			while (res.next()) {
				//
				// 見つかったエントリを戻り値に格納する。
				//
				rv.add(decode(res));
			}

			progress = "Result.query: done.";
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

	public static Vector<Result> queryAll(Connection conn, int fetch) throws SQLException {
		String filterCond = "";
		String filterFetch = (fetch == 0) ? "" : getStatement_Fetch(fetch);
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Result> queryByRid(Connection conn, int rid) throws SQLException {
		String filterCond = "where " + getStatement_Rid(rid);
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
			progress = "Result.validate: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Result.validate: query result.";
			statement =
				"select " + Result.columns +
				"	from tetcon.result" +
				"	where" +
				"		pid = " + this.pid +
				"		and date = '" + this.date + "'" +
				"		and host = '" + this.host + "'" +
				"		and ruleid = " + this.ruleid +
				"		and r = " + this.r;
			res = stmt.executeQuery(statement);

			if (res.next()) {
				//
				// 二重登録とみなして、登録しない。
				//
				throw new SQLException(progress + " already found.");
			}

			//
			// エントリが無いので登録する。
			//
			progress = "Result.validate: insert result.";
			statement =
				"insert into tetcon.result (" + Result.columns + ")" +
				"	values (" +
				"		default," +
				"		" + this.pid + "," +
				"		default," +
				"		'" + this.date + "'," +
				"		'" + this.host + "'," +
				"		" + this.ruleid + "," +
				"		" + this.r +
				"	)";
			stmt.executeUpdate(statement);

			progress = "Result.validate: re-query result.";
			statement =
				"select " + Result.columns +
				"	from tetcon.result" +
				"	where" +
				"		pid = " + this.pid +
				"		and date = '" + this.date + "'" +
				"		and host = '" + this.host + "'" +
				"		and ruleid = " + this.ruleid +
				"		and r = " + this.r;
			res = stmt.executeQuery(statement);
			if (!res.next()) {
				//
				// 登録されてなきゃおかしい。
				//
				throw new SQLException(progress + " not found.");
			}

			//
			// 見つかったエントリをインスタンスに格納する。
			//
			this.rid = res.getInt(1);
//			this.pid = res.getInt(2);
			this.reported = res.getTimestamp(3);
//			this.date = res.getString(4);
//			this.host = res.getString(5);
//			this.ruleid = res.getInt(6);
//			this.r = res.getInt(7);

			progress = "Result.validate: done.";
			res.close();
			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

		return getRid();
	}

	public int getRid() {
		return this.rid;
	}
	private static String getStatement_Rid(int rid) {
		return "rid = " + rid;
	}

	public int getPid() {
		return this.pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}

	public Timestamp getReported() {
		return this.reported;
	}

	public String getDate() {
		return this.date;
	}

	public String getHost() {
		return this.host;
	}

	public int getRuleid() {
		return this.ruleid;
	}
	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
	}

	public int getR() {
		return this.r;
	}

}
