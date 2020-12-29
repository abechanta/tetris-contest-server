package com.hotmail.abechanta.tetcon;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DbHttpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException {
		try {
			DbAccess.getConn(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServletException("db connection not found.");
		}
	}

	@Override
	public void destroy() {
		try {
			DbAccess.destroy();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	protected String getString(HttpServletRequest request, String name, String defaultValue)
	{
		String rv = request.getParameter(name);
		if ((rv == null) || !rv.matches("^[-0-9A-Za-z ._/]*$")) {
			rv = defaultValue;
		}

		// リクエストに UI パラメータを添付する。
		request.setAttribute(name, rv);
		return rv;
	}

	protected Integer getInteger(HttpServletRequest request, String name, int defaultValue)
	{
		Integer rv = null;
		try {
			String value = request.getParameter(name);
			rv = Integer.valueOf(value);
		} catch (Exception ex) {
			rv = new Integer(defaultValue);
		}

		// リクエストに UI パラメータを添付する。
		request.setAttribute(name, String.valueOf(rv));
		return rv;
	}

	protected void printOk(HttpServletResponse response, String message) throws IOException {
		PrintWriter out = response.getWriter();
		out.println("OK: " + message);
	}

	protected void printNg(HttpServletResponse response, String message) throws IOException {
		PrintWriter out = response.getWriter();
		out.println("NG: " + message);
	}
}
