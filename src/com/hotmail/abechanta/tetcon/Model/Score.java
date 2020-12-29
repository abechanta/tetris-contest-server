package com.hotmail.abechanta.tetcon.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;


public class Score {
	private int sid;
	private int mid;
	private int rid;
	private int p;
	private int l;
	private int l1;
	private int l2;
	private int l3;
	private int l4;
	private int x;
	private int si;
	private int sd;
	private int pr;
	private int lr;
	private int rr;
	public static final String columns = "sid, mid, rid, p, l, l1, l2, l3, l4, x, si, sd, pr, lr, rr";

	public static Score decode(Hashtable<String, String> tbl) {
		return new Score(
			0,
			0,
			0,
			Integer.valueOf(tbl.get("P")),
			Integer.valueOf(tbl.get("L")),
			Integer.valueOf(tbl.get("L1")),
			Integer.valueOf(tbl.get("L2")),
			Integer.valueOf(tbl.get("L3")),
			Integer.valueOf(tbl.get("L4")),
			Integer.valueOf(tbl.get("X")),
			Integer.valueOf(tbl.get("SI")),
			Integer.valueOf(tbl.get("SD")),
			Integer.valueOf(tbl.get("PR")),
			Integer.valueOf(tbl.get("LR")),
			Integer.valueOf(tbl.get("RR"))
		);
	}

	private static Score decode(ResultSet res) throws SQLException {
		return new Score(
			res.getInt(1),
			res.getInt(2),
			res.getInt(3),
			res.getInt(4),
			res.getInt(5),
			res.getInt(6),
			res.getInt(7),
			res.getInt(8),
			res.getInt(9),
			res.getInt(10),
			res.getInt(11),
			res.getInt(12),
			res.getInt(13),
			res.getInt(14),
			res.getInt(15)
		);
	}

	private static String getStatement_Fetch(int fetch) {
		return "FETCH first " + fetch + " ROWS ONLY";
	}

	public Score(
		int sid,
		int mid,
		int rid,
		int p,
		int l,
		int l1,
		int l2,
		int l3,
		int l4,
		int x,
		int si,
		int sd,
		int pr,
		int lr,
		int rr
	) {
		this.sid = sid;
		this.mid = mid;
		this.rid = rid;
		this.p = p;
		this.l = l;
		this.l1 = l1;
		this.l2 = l2;
		this.l3 = l3;
		this.l4 = l4;
		this.x = x;
		this.si = si;
		this.sd = sd;
		this.pr = pr;
		this.lr = lr;
		this.rr = rr;
	}

	private static Vector<Score> query(Connection conn, String filterCond, String filterFetch) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Vector<Score> rv = new Vector<Score>();
		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを検索する。
			//
			progress = "Score.query: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Score.query: query result.";
			statement =
				"select " + Score.columns +
				"	from tetcon.score" +
				"	" + filterCond +
				"	order by sid desc" +
				"	" + filterFetch;
			res = stmt.executeQuery(statement);

			while (res.next()) {
				//
				// 見つかったエントリを戻り値に格納する。
				//
				rv.add(decode(res));
			}

			progress = "Score.query: done.";
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

	public static Vector<Score> queryAll(Connection conn, int fetch) throws SQLException {
		String filterCond = "";
		String filterFetch = (fetch == 0) ? "" : getStatement_Fetch(fetch);
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Score> queryBySid(Connection conn, int sid) throws SQLException {
		String filterCond = "where " + getStatement_Sid(sid);
		String filterFetch = getStatement_Fetch(1);
		return query(conn, filterCond, filterFetch);
	}

	public int validate(Connection conn) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Statement stmt = null;
//		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを登録する。
			//
			progress = "Score.validate: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Score.validate: insert score.";
			statement =
				"insert into tetcon.score (" + Score.columns + ")" +
				"	values (" +
				"		default," +
				"		" + this.mid + "," +
				"		" + this.rid + "," +
				"		" + this.p + "," +
				"		" + this.l + "," +
				"		" + this.l1 + "," +
				"		" + this.l2 + "," +
				"		" + this.l3 + "," +
				"		" + this.l4 + "," +
				"		" + this.x + "," +
				"		" + this.si + "," +
				"		" + this.sd + "," +
				"		" + this.pr + "," +
				"		" + this.lr + "," +
				"		" + this.rr +
				"	)";
			stmt.executeUpdate(statement);

			//
			// 登録したエントリの再検索はしない（エントリをインスタンスに格納する必要が無いので）。
			//
//			this.sid = res.getInt(1);
//			this.mid = res.getInt(2);
//			this.this.rid = res.getInt(3);
//			this.p = res.getInt(4);
//			this.l = res.getInt(5);
//			this.l1 = res.getInt(6);
//			this.l2 = res.getInt(7);
//			this.l3 = res.getInt(8);
//			this.l4 = res.getInt(9);
//			this.x = res.getInt(10);
//			this.si = res.getInt(11);
//			this.sd = res.getInt(12);
//			this.pr = res.getInt(13);
//			this.lr = res.getInt(14);
//			this.rr = res.getInt(15);

			progress = "Score.validate: done.";
//			res.close();
//			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

		return getSid();	// ただし sid は無効。
	}

	public int getSid() {
		return this.sid;
	}
	private static String getStatement_Sid(int sid) {
		return "sid = " + sid;
	}

	public int getMid() {
		return this.mid;
	}
	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getRid() {
		return this.rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}

	public int getP() {
		return this.p;
	}

	public int getL() {
		return this.l;
	}

	public int getL1() {
		return this.l1;
	}

	public int getL2() {
		return this.l2;
	}

	public int getL3() {
		return this.l3;
	}

	public int getL4() {
		return this.l4;
	}

	public int getX() {
		return this.x;
	}

	public int getSi() {
		return this.si;
	}

	public int getSd() {
		return this.sd;
	}

	public int getPr() {
		return this.pr;
	}

	public int getLr() {
		return this.lr;
	}

	public int getRr() {
		return this.rr;
	}

}
