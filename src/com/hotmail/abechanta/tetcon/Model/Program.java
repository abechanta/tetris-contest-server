package com.hotmail.abechanta.tetcon.Model;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class Program {
	private int pid;
	private String ver;
	private String authkey;
	private byte content[];
	private Timestamp registered;
	public static final String columns = "pid, ver, authkey, content, registered";

	public static Program decode(Hashtable<String, String> tbl) {
		return new Program(
			0,
			tbl.get("VER"),
			"",
			null,
			null
		);
	}

	private static Program decode(ResultSet res) throws SQLException {
		return new Program(
			res.getInt(1),
			res.getString(2),
			res.getString(3),
			res.getBytes(4),
			res.getTimestamp(5)
		);
	}

	private static String getStatement_Fetch(int fetch) {
		return "FETCH first " + fetch + " ROWS ONLY";
	}

	public Program(
		int pid,
		String ver,
		String authkey,
		byte content[],
		Timestamp registered
	) {
		this.pid = pid;
		this.ver = ver;
		this.authkey = authkey;
		this.content = content;
		this.registered = registered;
	}

	private static Vector<Program> query(Connection conn, String filterCond, String filterFetch) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Vector<Program> rv = new Vector<Program>();
		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		try {
			//
			// 与えられた ver からエントリを検索する。
			//
			progress = "Program.query: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Program.query: query program.";
			statement =
				"select " + Program.columns +
				"	from tetcon.program" +
				"	" + filterCond +
				"	order by registered desc" +
				"	" + filterFetch;
			res = stmt.executeQuery(statement);

			while (res.next()) {
				//
				// 見つかったエントリを戻り値に格納する。
				//
				rv.add(Program.decode(res));
			}

			progress = "Program.query: done.";
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

	public static Vector<Program> queryAll(Connection conn, int fetch) throws SQLException {
		String filterCond = "";
		String filterFetch = (fetch == 0) ? "" : getStatement_Fetch(fetch);
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Program> queryByPid(Connection conn, int pid) throws SQLException {
		String filterCond = "where " + getStatement_Pid(pid);
		String filterFetch = getStatement_Fetch(1);
		return query(conn, filterCond, filterFetch);
	}

	public static Vector<Program> queryByVer(Connection conn, String ver) throws SQLException {
		String filterCond = "where " + getStatement_Ver(ver);
		String filterFetch = getStatement_Fetch(1);
		return query(conn, filterCond, filterFetch);
	}

	public int validate(Connection conn) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		PreparedStatement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		if (
			(!this.ver.matches("^[A-Z][a-z]{2} \\d{2} \\d{4}-\\d{6}$"))	// ex: Mar 16 2011-202059
		) {
			return 0;
		}

		try {
			//
			// すでにエントリが無いか検索してみる。
			//
			progress = "Program.update: query program.";
			statement =
				"select " + Program.columns +
				"	from tetcon.program" +
				"	where" +
				"		ver = '" + this.ver + "'" +
				"	order by registered desc";
			stmt = conn.prepareStatement(statement);
			res = stmt.executeQuery();

			if (res.next()) {
				//
				// 見つかったエントリをインスタンスに格納する。
				//
				this.pid = res.getInt(1);
//				this.ver = res.getString(2);
//				this.authkey = res.getString(3);
//				this.content = res.getBytes(4);
				this.registered = res.getTimestamp(5);

				//
				// エントリが有るので更新する。
				//
				progress = "Program.update: update program.";
				statement =
					"update tetcon.program" +
					"	set" +
					"		authkey = '" + this.authkey + "'," +
					"		content = ?," +
					"		registered = default" +
					"	where" +
					"		ver = '" + this.ver + "'";
				stmt = conn.prepareStatement(statement);
				stmt.setBinaryStream(1, new ByteArrayInputStream(this.content));
				stmt.execute();
			} else {
				//
				// エントリが無いので登録する。
				//
				progress = "Program.update: insert program.";
				statement =
					"insert into tetcon.program (" + Program.columns + ")" +
					"	values (" +
					"		default," +
					"		'" + this.ver + "'," +
					"		'" + this.authkey + "'," +
					"		?," +
					"		default" +
					"	)";
				stmt = conn.prepareStatement(statement);
				stmt.setBinaryStream(1, new ByteArrayInputStream(this.content));
				stmt.execute();
			}

			//
			// 登録／更新したエントリを検索する。
			//
			progress = "Program.update: re-query program.";
			statement =
				"select " + Program.columns +
				"	from tetcon.program" +
				"	where" +
				"		ver = '" + this.ver + "'" +
				"	order by registered desc";
			stmt = conn.prepareStatement(statement);
			res = stmt.executeQuery();

			if (!res.next()) {
				//
				// 登録されてなきゃおかしい。
				//
				throw new SQLException(progress + " not found.");
			}

			//
			// 見つかったエントリをインスタンスに格納する。
			//
			this.pid = res.getInt(1);
//			this.ver = res.getString(2);
//			this.authkey = res.getString(3);
//			this.content = res.getBytes(4);
			this.registered = res.getTimestamp(5);

			progress = "Program.update: done.";
			res.close();
			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}

		return getPid();
	}

	public boolean verifyIntegrity(String reportContent) throws NoSuchAlgorithmException {
		String recvKey = reportContent;
		if (!recvKey.startsWith("KEY=")) {
			return false;
		}
		recvKey = recvKey.substring(recvKey.indexOf("KEY=") + 4, recvKey.indexOf('&'));
		byte recvVal[] = decodeBytes(recvKey);

		String body = reportContent;
		body = body.substring(body.indexOf('&') + 1);

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(decodeBytes(this.authkey));
		md.update(this.content);
		md.update(body.getBytes());
		md.update(decodeBytes(this.authkey));
		byte calcVal[] = md.digest();
		return Arrays.equals(recvVal, calcVal);
	}

	private byte[] decodeBytes(String line) {
		byte rv[] = new byte[line.length() / 2];
		byte sum = 0;
		for (int ii = 0; ii < line.length(); ii++) {
			char ch = line.charAt(ii);
			sum = (byte)(
				(sum << 4) | (
					('0' <= ch) && (ch <= '9') ? ch - '0' : (
						('A' <= ch) && (ch <= 'F') ? 10 + ch - 'A' : (
							('a' <= ch) && (ch <= 'f') ? 10 + ch - 'a' : 0
						)
					)
				)
			);
			if (((ii + 1) % 2) == 0) {
				rv[ii / 2] = sum;
				sum = 0;
			}
		}
		return rv;
	}

	public int getPid() {
		return this.pid;
	}
	private static String getStatement_Pid(int pid) {
		return "pid = " + pid;
	}

	public String getVer() {
		return this.ver;
	}
	private static String getStatement_Ver(String ver) {
		return 	"ver = '" + ver + "'";
	}

	public String getAuthkey() {
		return this.authkey;
	}
	public void generateAuthkey() {
		Integer key1 = (int)(Integer.MAX_VALUE * Math.random()) + 1;
		Integer key2 = (int)(Integer.MAX_VALUE * Math.random()) + 1;
		this.authkey = String.format("%1$08X%2$08X", key1, key2);
	}

	public byte[] getContent() {
		return this.content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}

	public Timestamp getRegistered() {
		return this.registered;
	}

}
