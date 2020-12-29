package com.hotmail.abechanta.tetcon.Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

public class Config {
	private static Hashtable<String, String> content = new Hashtable<String, String>();

	public static String query(Connection conn, String name) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		if (!name.matches("^[0-9A-Za-z]+$")) {
			return new String("");
		}

		if (Config.content.get(name) == null) {
			try {
				//
				// 与えられた name からエントリを検索する。
				//
				progress = "Config.query: connect to database via jdbc.";
				stmt = conn.createStatement();
	
				progress = "Config.query: query config.";
				statement =
					"select val" +
					"	from tetcon.config" +
					"	where" +
					"		name = '" + name + "'";
				res = stmt.executeQuery(statement);
	
				if (res.next()) {
					//
					// 見つかったエントリをテーブルに格納する。
					//
					Config.content.put(name, res.getString(1));
				}
	
				progress = "Config.query: done.";
				res.close();
				res = null;
				stmt.close();
				stmt = null;
	
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new SQLException(progress + " statement=" + statement);
			}
		}

		return Config.content.get(name);
	}

	public static void update(Connection conn, String name, String val) throws SQLException {
		if (conn == null) {
			throw new SQLException("invalid arg: conn=" + conn);
		}

		Statement stmt = null;
		ResultSet res = null;
		String progress = "";
		String statement = "";

		if (!name.matches("^[0-9A-Za-z]+$")) {
			return;
		}

		Config.content.put(name, val);

		try {
			//
			// 与えられた name からエントリを検索する。
			//
			progress = "Config.update: connect to database via jdbc.";
			stmt = conn.createStatement();

			progress = "Config.update: query config.";
			statement =
				"select val" +
				"	from tetcon.config" +
				"	where" +
				"		name = '" + name + "'";
			res = stmt.executeQuery(statement);

			if (res.next()) {
				//
				// エントリが有るので更新する。
				//
				progress = "Config.update: update config.";
				statement =
					"update tetcon.config" +
					"	set" +
					"		val = '" + val + "'" +
					"	where" +
					"		name = '" + name + "'";
				stmt.executeUpdate(statement);
			} else {
				//
				// エントリが無いので登録する。
				//
				progress = "Config.update: insert program.";
				statement =
					"insert into tetcon.config (name, val, updated)" +
					"	values (" +
					"		'" + name + "'," +
					"		'" + val + "'," +
					"		default" +
					"	)";
				stmt.executeUpdate(statement);
			}

			progress = "Config.update: done.";
			res.close();
			res = null;
			stmt.close();
			stmt = null;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new SQLException(progress + " statement=" + statement);
		}
	}

}
