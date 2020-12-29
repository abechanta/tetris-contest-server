package com.hotmail.abechanta.tetcon.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Hashtable;
import java.util.Vector;

public class Member {
	private int mid;
	private String name;
	private String author;
	private Timestamp registered;
	public static final String columns = "mid, name, author, registered";

	public static Member decode(Hashtable<String, String> tbl) {
		return new Member(
			0,
			tbl.get("NAME"),
			tbl.get("AUTHOR"),
			new Timestamp(0)
		);
	}

	private static Member decode(ResultSet res) throws SQLException {
		return new Member(
			res.getInt(1),
			res.getString(2),
			res.getString(3),
			res.getTimestamp(4)
		);
	}

	private static String getStatement_Fetch(int fetch) {
		return "FETCH first " + fetch + " ROWS ONLY";
	}

	public Member(
		int mid,
		String name,
		String author,
		Timestamp registered
	) {
		this.mid = mid;
		this.name = name;
		this.author = author;
		this.registered = registered;
	}

	private static Vector<Member> query(Connection conn, String filterCond, String filterFetch) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Vector<Member> rv = new Vector<Member>();
		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを検索する。
			//
			progress = "Member.query: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Member.query: query member.";
			statement =
				"select " + Member.columns +
				"	from tetcon.member" +
				"	" + filterCond +
				"	order by registered desc" +
				"	" + filterFetch;
			res = stmt.executeQuery(statement);

			while (res.next()) {
				//
				// 見つかったエントリを戻り値に格納する。
				//
				rv.add(decode(res));
			}

			progress = "Member.query: done.";
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

	public static Vector<Member> queryAll(Connection conn, int fetch) throws SQLException {
		String filterCond = "";
		String filterFetch = (fetch == 0) ? "" : getStatement_Fetch(fetch);
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Member> queryByMid(Connection conn, int mid) throws SQLException {
		String filterCond = "where " + getStatement_Mid(mid);
		String filterFetch = getStatement_Fetch(1);
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Member> queryByNameAndAuthor(Connection conn, String name, String author) throws SQLException {
		String filterCond = "where " + getStatement_Name(name) + " and " + getStatement_Author(author);
		String filterFetch = getStatement_Fetch(100);
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
			progress = "Member.validate: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Member.validate: query member.";
			statement =
				"select " + Member.columns +
				"	from tetcon.member" +
				"	where" +
				"		name = '" + this.name + "'";
			res = stmt.executeQuery(statement);

			if (!res.next()) {
				//
				// エントリが無いので登録する。
				//
				progress = "Member.validate: insert member.";
				statement =
					"insert into tetcon.member" +
					"	values (" +
					"		default," +
					"		'" + this.name + "'," +
					"		'" + this.author + "'," +
					"		default" +
					"	)";
				stmt.executeUpdate(statement);

				//
				// 登録したエントリを検索する。
				//
				progress = "Member.validate: re-query member.";
				statement =
					"select " + Member.columns +
					"	from tetcon.member" +
					"	where" +
					"		name = '" + this.name + "'";
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
			this.mid = res.getInt(1);
//			this.name = res.getString(2);
//			this.author = res.getString(3);
			this.registered = res.getTimestamp(4);

			progress = "Member.validate: done.";
			res.close();
			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

		return getMid();
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
	private static String getStatement_Name(String name) {
		return "name like '%" + name + "%'";
	}

	public String getAuthor() {
		return this.author;
	}
	private static String getStatement_Author(String author) {
		return "author like '%" + author + "%'";
	}

	public Timestamp getRegistered() {
		return this.registered;
	}

}
