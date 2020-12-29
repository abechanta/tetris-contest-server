package com.hotmail.abechanta.tetcon.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Vector;

public class Contest {
	private int cid;
	private String name;
	private String date0;
	private String date1;
	private int ruleid;
	private String category;
	private int cntr;
	private int prize1;
	private int prize2;
	private int prize3;
	private int state;
	private Timestamp updated;
	public static final String columns = "cid, name, date0, date1, ruleid, category, cntr, prize1, prize2, prize3, state, updated";
	public static final int STATE_READY   = 0;
	public static final int STATE_OPEN    = 1;
	public static final int STATE_SUSPEND = 2;
	public static final int STATE_CLOSED  = 3;

	private static Contest decode(ResultSet res) throws SQLException {
		return new Contest(
			res.getInt(1),
			res.getString(2),
			res.getString(3),
			res.getString(4),
			res.getInt(5),
			res.getString(6),
			res.getInt(7),
			res.getInt(8),
			res.getInt(9),
			res.getInt(10),
			res.getInt(11),
			res.getTimestamp(12)
		);
	}

	private static String getStatement_Fetch(int fetch) {
		return "FETCH first " + fetch + " ROWS ONLY";
	}

	public Contest(
		int cid,
		String name,
		String date0,
		String date1,
		int ruleid,
		String category,
		int cntr,
		int prize1,
		int prize2,
		int prize3,
		int state,
		Timestamp updated
	) {
		this.cid = cid;
		this.name = name;
		this.date0 = date0;
		this.date1 = date1;
		this.ruleid = ruleid;
		this.category = category;
		this.cntr = cntr;
		this.prize1 = prize1;
		this.prize2 = prize2;
		this.prize3 = prize3;
		this.state = state;
		this.updated = updated;
	}

	private static Vector<Contest> query(Connection conn, String filterCond, String filterFetch) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Vector<Contest> rv = new Vector<Contest>();
		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを検索する。
			//
			progress = "Contest.query: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Contest.query: query contest.";
			statement =
				"select " + Contest.columns +
				"	from tetcon.contest" +
				"	" + filterCond +
				"	order by cid desc" +
				"	" + filterFetch;
			res = stmt.executeQuery(statement);

			while (res.next()) {
				//
				// 見つかったエントリを戻り値に格納する。
				//
				rv.add(decode(res));
			}

			progress = "Contest.query: done.";
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

	public static Vector<Contest> queryAll(Connection conn, int fetch) throws SQLException {
		String filterCond = "";
		String filterFetch = (fetch == 0) ? "" : getStatement_Fetch(fetch);
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Contest> queryByCid(Connection conn, int cid) throws SQLException {
		String filterCond = "where " + getStatement_Cid(cid);
		String filterFetch = "";
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Contest> queryByName(Connection conn, String name) throws SQLException {
		String filterCond = "where " + getStatement_Name(name);
		String filterFetch = "";
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Contest> queryByState(Connection conn, int state, int fetch) throws SQLException {
		String filterCond = "where " + getStatement_State(state);
		String filterFetch = (fetch == 0) ? "" : getStatement_Fetch(fetch);
		return query(conn, filterCond, filterFetch);
	}

	public static void update(Connection conn, int cid) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Statement stmt = null;
//		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを検索する。
			//
			progress = "Contest.update: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Contest.update: update contest.";
			statement =
				"update tetcon.contest" +
				"	set" +
				"		updated = default" +
				"	where" +
				"		cid = " + cid;
			stmt.executeUpdate(statement);

			progress = "Contest.update: done.";
//			res.close();
//			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

	}

	public static void updatePre(Connection conn, String currentDate) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Statement stmt = null;
//		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを検索する。
			//
			progress = "Contest.updatePre: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Contest.updatePre: update contest.";
			statement =
				"update tetcon.contest" +
				"	set" +
				"		state = " + STATE_OPEN +
				"	where" +
				"		state = " + STATE_READY +
				"		and date0 < '" + currentDate + "'";
			stmt.executeUpdate(statement);

			progress = "Contest.updatePre: done.";
//			res.close();
//			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

	}

	public static void updatePost(Connection conn, String currentDate) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Statement stmt = null;
//		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを検索する。
			//
			progress = "Contest.updatePost: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Contest.updatePost: update contest.";
			statement =
				"update tetcon.contest" +
				"	set" +
				"		state = " + STATE_CLOSED +
				"	where" +
				"		state = " + STATE_OPEN +
				"		and date1 < '" + currentDate + "'";
			stmt.executeUpdate(statement);

			progress = "Contest.updatePost: done.";
//			res.close();
//			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

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
			progress = "Contest.validate: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Contest.validate: query contest.";
			statement =
				"select " + Contest.columns +
				"	from tetcon.contest" +
				"	where" +
				"		name = '" + this.name + "'";
			res = stmt.executeQuery(statement);

			if (res.next()) {
				//
				// 見つかったエントリをインスタンスに格納する。
				//
				this.cid = res.getInt(1);
//				this.name = res.getString(2);
//				this.date0 = res.getString(3);
//				this.date1 = res.getString(4);
//				this.ruleid = res.getInt(5);
//				this.category = res.getString(6);
//				this.cntr = res.getInt(7);
//				this.prize1 = res.getInt(8);
//				this.prize2 = res.getInt(9);
//				this.prize3 = res.getInt(10);
				this.state = Contest.STATE_READY;	// res.getInt(11);
				this.updated = res.getTimestamp(12);

				//
				// エントリが有るので更新する。
				//
				progress = "Contest.update: update contest.";
				statement =
					"update tetcon.contest" +
					"	set" +
					"		date0 = '" + this.date0 + "'," +
					"		date1 = '" + this.date1 + "'," +
					"		ruleid = " + this.ruleid + "," +
					"		category = '" + this.category + "'," +
					"		cntr = " + this.cntr + "," +
					"		prize1 = " + this.prize1 + "," +
					"		prize2 = " + this.prize2 + "," +
					"		prize3 = " + this.prize3 + "," +
					"		state = default" +
					"	where" +
					"		name = '" + this.name + "'";
				stmt.executeUpdate(statement);
			} else {
				//
				// エントリが無いので登録する。
				//
				progress = "Contest.validate: insert contest.";
				statement =
					"insert into tetcon.contest" +
					"	values (" +
					"		default," +
					"		'" + this.name + "'," +
					"		'" + this.date0 + "'," +
					"		'" + this.date1 + "'," +
					"		" + this.ruleid + "," +
					"		'" + this.category + "'," +
					"		" + this.cntr + "," +
					"		" + this.prize1 + "," +
					"		" + this.prize2 + "," +
					"		" + this.prize3 + "," +
					"		default," +
					"		default" +
					"	)";
				stmt.executeUpdate(statement);
			}

			//
			// 登録／更新したエントリを検索する。
			//
			progress = "Contest.validate: re-query contest.";
			statement =
				"select " + Contest.columns +
				"	from tetcon.contest" +
				"	where" +
				"		name = '" + this.name + "'";
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
			this.cid = res.getInt(1);
//			this.name = res.getString(2);
//			this.date0 = res.getString(3);
//			this.date1 = res.getString(4);
//			this.ruleid = res.getInt(5);
//			this.category = res.getString(6);
//			this.cntr = res.getInt(7);
//			this.prize1 = res.getInt(8);
//			this.prize2 = res.getInt(9);
//			this.prize3 = res.getInt(10);
//			this.state = res.getInt(11);
			this.updated = res.getTimestamp(12);

			progress = "Contest.validate: done.";
			res.close();
			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

		return getCid();
	}

	public int getCid() {
		return this.cid;
	}
	private static String getStatement_Cid(int cid) {
		return "cid = " + cid;
	}

	public String getName() {
		return this.name;
	}
	private static String getStatement_Name(String name) {
		return "name = '" + name + "'";
	}

	public String getDate0() {
		return this.date0;
	}
	public void setDate0(String date0) {
		this.date0 = date0;
	}

	public String getDate1() {
		return this.date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public int getRuleid() {
		return this.ruleid;
	}
	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
	}

	public String getCategory() {
		return this.category;
	}
	public String getCategoryStatement() {
		if (this.category.equals("avgPts")) {
			return "avg(pr) * avg(lr)";
		} else if (this.category.equals("avgL")) {
			return "avg(l)";
		}
		return "UNKNOWN";
	}
	public void setCategory(String category) {
		this.category = category;
	}

	public int getCntr() {
		return this.cntr;
	}
	public void setCntr(int cntr) {
		this.cntr = cntr;
	}

	public int getPrize(int prize) {
		int rv = 0;
		switch (prize) {
		case 1:
			rv = this.prize1;
			break;
		case 2:
			rv = this.prize2;
			break;
		case 3:
			rv = this.prize3;
			break;
		default:
			rv = 0;
			break;
		}
		return rv;
	}

	public int getPrize1() {
		return this.prize1;
	}
	public void setPrize1(int prize1) {
		this.prize1 = prize1;
	}

	public int getPrize2() {
		return this.prize2;
	}
	public void setPrize2(int prize2) {
		this.prize2 = prize2;
	}

	public int getPrize3() {
		return this.prize3;
	}
	public void setPrize3(int prize3) {
		this.prize3 = prize3;
	}

	public int getState() {
		return this.state;
	}
	private static String getStatement_State(int state) {
		return "state = " + state;
	}

	public Timestamp getUpdated() {
		return this.updated;
	}
}
