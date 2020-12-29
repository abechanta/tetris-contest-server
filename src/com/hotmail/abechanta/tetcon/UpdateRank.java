package com.hotmail.abechanta.tetcon;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hotmail.abechanta.tetcon.Model.Contest;
import com.hotmail.abechanta.tetcon.Model.Rank;
import com.hotmail.abechanta.tetcon.Model.Title;

/**
 * Servlet implementation class UpdateRank
 */
@WebServlet("/UpdateRank")
public class UpdateRank extends DbHttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		// パラメータを取得する。
		//
		request.setCharacterEncoding("UTF-16LE");
//		BufferedReader in = request.getReader();

		//
		// パラメータを解析する。
		//
//		String reportContent = in.readLine();
//		int cid = 0;
//		if (!reportContent.equals("all")) {
//			try {
//				cid = Integer.valueOf(reportContent);
//			} catch (Exception ex) {
//				// reportContent が空 or 不正。
//				throw new ServletException();
//			}
//		}

		//
		// コネクションを確立する。
		//
		Connection conn = null;
		try {
			conn = DbAccess.getConn(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// コンテストの状態を更新する。
		//
		Calendar current = Calendar.getInstance();
		String currentDate = String.format("%1$ty%1$tm%1$td%1$tH%1$tM%1$tS", current);
		try {
			Contest.updatePre(conn, currentDate);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// コンテストを検索する。
		//
		Vector<Contest> contests = null;
		try {
			contests = Contest.queryByState(conn, Contest.STATE_OPEN, 0);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// ランキングを更新する。
		//
		try {
			for (int ii = 0; ii < contests.size(); ii++) {
				Contest contest0 = contests.get(ii);
				Rank.update(conn, contest0);

				if (contest0.getDate1().compareTo(currentDate) > 0) {
					Vector<Rank> ranks = Rank.queryAll(conn, contest0);

					for (int jj = 1; jj <= 3; jj++) {
						if (
							(contest0.getPrize(jj) > 0) &&
							(ranks.size() >= jj)
						) {
							Title title = new Title(
								0,
								ranks.get(jj - 1).getMid(),
								getTitlename(contest0.getName(), ranks.get(jj - 1).getRank()),
								contest0.getPrize(ranks.get(jj - 1).getRank()),
								new Timestamp(0)
							);
							title.validate(conn);
						}
					}
				}

				Contest.update(conn, contest0.getCid());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// コンテストの状態を更新する。
		//
		try {
			Contest.updatePost(conn, currentDate);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		try {
			conn.commit();
		} catch (Exception ex) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (Exception innerEx) {
					ex = innerEx;
				}
			}
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// 結果を返信する。
		//
		printOk(response, "done.");
	}

	private String getTitlename(String name, int rankno) {
		String ordinal = "th";
		if ((rankno / 10) % 10 != 1) {
			switch (rankno) {
			case 1:
				ordinal = "st";
				break;
			case 2:
				ordinal = "nd";
				break;
			case 3:
				ordinal = "rd";
				break;
			default:
				break;
			}
		}
		return name + ": " + rankno + ordinal;
	}
}
