package com.ceva.filters;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * The <code>SessionFilter</code> is configured for the url mapping .do and
 * checks whether the requested resource that can be allowed or not if not it
 * checks for the configured allowed urls in the web.xml else it redirects to
 * the index page or the configured url.
 * </p>
 * 
 * <p>
 * &copy; 2008 by YALAMANCHILI
 * </p>
 * 
 * @author <a href='mailto:sivarama@naradaproducts.com'>Shiva Ram Murthy M</a>
 * @version 1.0
 */
public class SessionFilter implements Filter {

	// default redirect url
	private static final String DEFAULT_REDIRECT_URL = "/html/index.html";

	// default forbidden url
	private static final String DEFAULT_FORBIDDEN_URL = "/Responder/ErrorDisplayer.jsp?error_code=DENYACCESS";

	// string to hold allowed urls from config
	private static final String ALLOWED_URLS = "allowedURLs";

	// string to holds redirect url configured in config for this filter
	private static final String REDIRECT_URL = "redirectURL";

	// string to hold forbidden url configured in config for this filter
	private static final String FORBIDDEN_URL = "forbiddenURL";

	private String forbiddenURL;

	private String redirectURL;

	// array to hold the allowed urls
	private static String[] allowed = null;

	/**
	 * When filters gets loaded <code>init()</code> gets invoked and the
	 * config values are read from the configurations configured in web.xml and
	 * assigned to the class varaibles
	 */
	public void init(FilterConfig config) throws ServletException {

		redirectURL = StringUtils.defaultString(config
				.getInitParameter(REDIRECT_URL));

		redirectURL = "".equals(redirectURL) ? DEFAULT_REDIRECT_URL
				: redirectURL;

		forbiddenURL = StringUtils.defaultString(config
				.getInitParameter(FORBIDDEN_URL));
		forbiddenURL = "".equals(forbiddenURL) ? DEFAULT_FORBIDDEN_URL
				: forbiddenURL;

		String allowedURLs = StringUtils.defaultString(config
				.getInitParameter(ALLOWED_URLS));
		/**
		 * splitting the comma separated allowed urls and holding them in array
		 */
		if (allowedURLs.indexOf('.') != -1) {
			allowed = allowedURLs.split(",");

		} else {
			allowed = new String[1];
			allowed[0] = allowedURLs;
		}

	}

	/**
	 * This method retuen true when given request pass through else return
	 * false.
	 * 
	 * @param request
	 *            It accepts HttpServletRequest Object.
	 * @return It returns boolean value.
	 */
	public static boolean allowedPass(HttpServletRequest request) {
		String contextPath = request.getContextPath();
		for (int i = 0; i < allowed.length; i++) {
			if (Pattern.matches(contextPath + allowed[i], request
					.getRequestURI())) {
				return true;
			} else if (request.getServletPath().endsWith(allowed[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		boolean allowedPass = allowedPass(request);
		/**
		 * User logged In
		 */
		if (session != null) {

			chain.doFilter(request, response);
		} else {
			if (allowedPass) {
				chain.doFilter(request, response);
			} else {
				request.getRequestDispatcher(redirectURL).forward(request,
						response);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {

	}
}