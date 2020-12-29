package com.hotmail.abechanta.tetcon.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;


public class Rank {
	private int mid;
	private int cntr;
	private int pts;
	private int rank;
	public static final String columns = "mid, cntr, pts";

	private static Rank decode(ResultSet res) throws SQLException {
		return new Rank(
			res.getInt(1),
			res.getInt(2),
			res.getInt(3)
		);
	}

	public Rank(
		int mid,
		int cntr,
		int pts
	) {
		this.mid = mid;
		this.cntr = cntr;
		this.pts = pts;
		this.rank = 0;
	}

	private static Vector<Rank> query(Connection conn, Contest contest, String filterCond, String filterFetch) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Vector<Rank> rv = new Vector<Rank>();
		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを検索する。
			//
			progress = "Rank.query: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Rank.query: query result.";
			statement =
				"select " + Rank.columns +
				"	from tetcon.rank_" + contest.getCid() +
				"	" + filterCond +
				"	order by pts desc" +
				" 	" + filterFetch;
			res = stmt.executeQuery(statement);

			int cnt = 0;
			int rankno = 1;
			int pts = 0;
			while (res.next()) {
				//
				// 見つかったエントリを戻り値に格納する。
				//
				Rank rank = decode(res);

				cnt++;
				if (pts != rank.getPts()) {
					// 直前のエントリと異なる点数なら rankno を更新する
					rankno = cnt;
				} else {
					// 直前のエントリと同じ点数なら rankno を更新しない
				}
				rank.setRank(rankno);
				pts = rank.getPts();

				rv.add(rank);
			}

			progress = "Rank.query: done.";
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

	public static Vector<Rank> queryAll(Connection conn, Contest contest) throws SQLException {
		String filterCond = "";
		String filterFetch = "";
		return query(conn, contest, filterCond, filterFetch);
	}

	public static void update(Connection conn, Contest contest) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Statement stmt = null;
//		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// エントリを削除する。
			//
			progress = "Rank.update: connect to database via jdbc.";
			stmt = conn.createStatement();

			try {
				progress = "Rank.update: create rank.";
				statement =
					"create table tetcon.rank_" + contest.getCid() +
					"	as select *" +
					"		from tetcon.rank_" +
					"		with no data";
				stmt.executeUpdate(statement);		

			} catch (Exception ex) {
				//
				// 織り込み済みの例外なので、何もしない。
				//
			}

			try {
				progress = "Rank.update: delete rank.";
				statement =
					"delete" +
					"	from tetcon.rank_" + contest.getCid();
				stmt.executeUpdate(statement);		

			} catch (Exception ex) {
				//
				// 織り込み済みの例外なので、何もしない。
				//
			}

			//
			// エントリを追加する。
			//
			progress = "Rank.update: insert rank.";
			statement =
				"insert into tetcon.rank_" + contest.getCid() + " (mid, cntr, pts)" +
				"	select mid, count(sid) as cntr, " + contest.getCategoryStatement() + " as pts" +
				"		from (" +
				"			select *" +
				"				from tetcon.score" +
				"				where" +
				"					rid in (" +
				"						select rid" +
				"							from tetcon.result" +
				"							where" +
				"								ruleid = " + contest.getRuleid() +
				"								and '" + contest.getDate0() + "' <= date" +
				"								and date <= '" + contest.getDate1() + "'" +
				"					)" +
				"		) as tmp0" +
				"		group by mid" +
				"		having count(sid) >= " + contest.getCntr();
			stmt.executeUpdate(statement);

			progress = "Rank.update: done.";
//			res.close();
//			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}
	}

	public int getMid() {
		return this.mid;
	}

	public int getCntr() {
		return this.cntr;
	}

	public int getPts() {
		return this.pts;
	}

	public int getRank() {
		return this.rank;
	}
	private void setRank(int rank) {
		this.rank = rank;
	}

}
