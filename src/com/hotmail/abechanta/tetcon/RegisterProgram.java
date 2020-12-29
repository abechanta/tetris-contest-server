package com.hotmail.abechanta.tetcon;

import java.io.IOException;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.hotmail.abechanta.tetcon.Model.Config;
import com.hotmail.abechanta.tetcon.Model.Program;

/**
 * Servlet implementation class RegisterProgram
 */
public class RegisterProgram extends DbHttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String encoding = "utf-8";

	private void parseParams(HttpServletRequest request, Hashtable<String, String> params, Hashtable<String, byte[]> files) throws FileUploadException {
		if (ServletFileUpload.isMultipartContent(request)) {
			DiskFileItemFactory dfiFactory = new DiskFileItemFactory();
			ServletFileUpload programFile0 = new ServletFileUpload(dfiFactory);
			List<FileItem> list = programFile0.parseRequest(request);
			Iterator<FileItem> it = list.iterator();
			while (it.hasNext()) {
				FileItem item = (FileItem)it.next();
				if (item.isFormField()) {
					String fieldName = item.getFieldName();
					String fieldValue = new String(item.get());
					params.put(fieldName, fieldValue);
				} else {
					String contentName = item.getFieldName();
					byte contentBody[] = item.get();
					files.put(contentName, contentBody);
				}
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//
		// UI パラメータを取得する。
		//
		request.setCharacterEncoding(encoding);
		Hashtable<String, String> params = new Hashtable<String, String>();
		Hashtable<String, byte[]> files = new Hashtable<String, byte[]>();
		try {
			parseParams(request, params, files);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// コンテンツを準備する。
		//
		Connection conn = null;
		try {
			conn = DbAccess.getConn(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// パスワードが合っているか確認する。
		//
		try {
			if (!params.get("passwd0").equals(Config.query(conn, "passwd"))) {
				printNg(response, "invalid password.");
				return;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// すでに登録が無いかプログラムを検索する。
		//
		Vector<Program> programs = null;
		try {
			programs = Program.queryByVer(conn, params.get("ver0"));
			if (programs.size() > 1) {
				throw new ServletException("db integrity issue occurred.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// プログラムを登録する。
		// すでに登録が有る場合は、バイナリを再登録し、認証鍵を再発行する。
		//
		Program program0 = (programs.size() > 0) ?
			programs.get(0)		// エントリ有り → 更新。
		:
			new Program(
				0,
				params.get("ver0"),
				"",
				files.get(0),
				null
			)					// エントリ無し → 登録。
		;
		program0.generateAuthkey();
		program0.setContent(files.get("program0"));

		try {
			if (program0.validate(conn) == 0) {
				throw new ServletException("invalid arg.");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException(ex.getMessage());
		}

		//
		// DB にコミットする。
		//
		try {
			conn.commit();
		} catch (Exception ex) {
			if (conn != null) {
				try {
					// DB をロールバックする。
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
		printOk(response, "ver=" + program0.getVer() + ", authkey=" + program0.getAuthkey());
	}
}
