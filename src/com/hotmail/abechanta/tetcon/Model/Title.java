package com.hotmail.abechanta.tetcon.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

public class Title {
	private int tid;
	private int mid;
	private String name;
	private int prize;
	private Timestamp registered;
	public static final String columns = "tid, mid, name, prize, registered";

	private static Title decode(ResultSet res) throws SQLException {
		return new Title(
			res.getInt(1),
			res.getInt(2),
			res.getString(3),
			res.getInt(4),
			res.getTimestamp(5)
		);
	}

	private static String getStatement_Fetch(int fetch) {
		return "FETCH first " + fetch + " ROWS ONLY";
	}

	public Title(
		int tid,
		int mid,
		String name,
		int prize,
		Timestamp registered
	) {
		this.tid = tid;
		this.mid = mid;
		this.name = name;
		this.prize = prize;
		this.registered = registered;
	}

	private static Vector<Title> query(Connection conn, String filterCond, String filterFetch) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Vector<Title> rv = new Vector<Title>();
		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを検索する。
			//
			progress = "Title.query: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Title.query: query title.";
			statement =
				"select " + Title.columns +
				"	from tetcon.title" +
				"	" + filterCond +
				"	order by prize desc" +
				"	" + filterFetch;
			res = stmt.executeQuery(statement);

			while (res.next()) {
				//
				// 見つかったエントリを戻り値に格納する。
				//
				rv.add(decode(res));
			}

			progress = "Title.query: done.";
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

	public static Vector<Title> queryAll(Connection conn, int fetch) throws SQLException {
		String filterCond = "";
		String filterFetch = (fetch == 0) ? "" : getStatement_Fetch(fetch);
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Title> queryByTid(Connection conn, int tid) throws SQLException {
		String filterCond = "where " + getStatement_Tid(tid);
		String filterFetch = "";
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Title> queryByMid(Connection conn, int mid) throws SQLException {
		String filterCond = "where " + getStatement_Mid(mid);
		String filterFetch = "";
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
			progress = "Title.validate: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Title.validate: query title.";
			statement =
				"select " + Title.columns +
				"	from tetcon.title" +
				"	where" +
				"		name = '" + this.name + "'";
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
			progress = "Title.validate: insert title.";
			statement =
				"insert into tetcon.title (" + Title.columns + ")" +
				"	values (" +
				"		default," +
				"		" + this.mid + "," +
				"		'" + this.name + "'," +
				"		" + this.prize + "," +
				"		default" +
				"	)";
			stmt.executeUpdate(statement);

			progress = "Title.validate: re-query title.";
			statement =
				"select " + Title.columns +
				"	from tetcon.title" +
				"	where" +
				"		mid = " + this.mid +
				"		and name = '" + this.name + "'" +
				"		and prize = " + this.prize;
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
			this.tid = res.getInt(1);
//			this.mid = res.getInt(2);
//			this.name = res.getString(3);
//			this.prize = res.getInt(4);
			this.registered = res.getTimestamp(5);

			progress = "Title.validate: done.";
			res.close();
			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

		return getTid();
	}

	public int getTid() {
		return this.tid;
	}
	private static String getStatement_Tid(int tid) {
		return "tid = " + tid;
	}

	public int getMid() {
		return this.mid;
	}
	private static String getStatement_Mid(int mid) {
		return "mid = " + mid;
	}

	public String getName() {
		return this.name;
	}

	public int getPrize() {
		return this.prize;
	}

	public Timestamp getRegistered() {
		return this.registered;
	}
}
