
package com.ceva.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class HTMLInputFilter {

	protected static final Logger logger = Logger
			.getLogger(HTMLInputFilter.class);

	/**
	 * flag determining whether to try to make tags when presented with
	 * "unbalanced" angle brackets (e.g. "<b text </b>" becomes "<b> text
	 * </b>"). If set to false, unbalanced angle brackets will be html escaped.
	 */
	protected static final boolean ALWAYS_MAKE_TAGS = false;

	/**
	 * flag determing whether comments are allowed in input String.
	 */
	protected static final boolean STRIP_COMMENTS = true;

	/** regex flag union representing /si modifiers in php * */
	protected static final int REGEX_FLAGS_SI = Pattern.CASE_INSENSITIVE
			| Pattern.DOTALL;

	/**
	 * set of allowed html elements, along with allowed attributes for each
	 * element *
	 */
	protected Map<String, List<String>> vAllowed;

	/** counts of open tags for each (allowable) html element * */
	protected Map<String, Integer> vTagCounts;

	/** html elements which must always be self-closing (e.g. "<img />") * */
	protected String[] vSelfClosingTags;

	/**
	 * html elements which must always have separate opening and closing tags
	 * (e.g. "<b></b>") *
	 */
	protected String[] vNeedClosingTags;

	/** attributes which should be checked for valid protocols * */
	protected String[] vProtocolAtts;

	/** allowed protocols * */
	protected String[] vAllowedProtocols;

	/**
	 * tags which should be removed if they contain no content (e.g. "<b></b>"
	 * or "<b />") *
	 */
	protected String[] vRemoveBlanks;

	/** entities allowed within html markup * */
	protected String[] vAllowedEntities;

	protected boolean vDebug;

	public HTMLInputFilter() {
		this(false);
	}

	/**
	 * This method is for filter html inputs.
	 * 
	 * @param debug
	 *            it takes boolean as argument.
	 */
	public HTMLInputFilter(boolean debug) {
		vDebug = debug;

		vAllowed = new HashMap<String, List<String>>();
		vTagCounts = new HashMap<String, Integer>();

		ArrayList<String> a_atts = new ArrayList<String>();
		a_atts.add("href");
		a_atts.add("target");
		vAllowed.put("a", a_atts);

		ArrayList<String> img_atts = new ArrayList<String>();
		img_atts.add("src");
		img_atts.add("width");
		img_atts.add("height");
		img_atts.add("alt");
		vAllowed.put("img", img_atts);

		ArrayList<String> no_atts = new ArrayList<String>();
		vAllowed.put("b", no_atts);
		vAllowed.put("strong", no_atts);
		vAllowed.put("i", no_atts);
		vAllowed.put("em", no_atts);

		vSelfClosingTags = new String[] { "img" };
		vNeedClosingTags = new String[] { "a", "b", "strong", "i", "em" };
		vAllowedProtocols = new String[] { "http", "mailto" }; // no ftp.
		vProtocolAtts = new String[] { "src", "href" };
		vRemoveBlanks = new String[] { "a", "b", "strong", "i", "em" };
		vAllowedEntities = new String[] { "amp", "gt", "lt", "quot" };
	}

	/**
	 * 
	 */
	protected void reset() {
		vTagCounts = new HashMap<String, Integer>();
	}

	/**
	 * This method take String an passes to logs on the bases of condition.
	 * 
	 * @param msg
	 *            I accepts String value.
	 */
	protected void debug(String msg) {
		if (vDebug) {
			logger.fatal(msg);
		}
	}

	/**
	 * This method takes int as input and Converts into String value
	 * 
	 * @param decimal
	 *            It accepts int value.
	 * @return it returns string value.
	 */
	public static String chr(int decimal) {
		return String.valueOf(decimal);
	}

	/**
	 * This method will convert the specific character to it equivatlent html
	 * understandable character.
	 * 
	 * @param s
	 *            It accepts string value.
	 * @return It returns String value.
	 */
	public static String htmlSpecialChars(String s) {
		s = s.replaceAll("&", "&amp;");
		s = s.replaceAll("\"", "&quot;");
		s = s.replaceAll("<", "&lt;");
		s = s.replaceAll(">", "&gt;");
		return s;
	}

	/**
	 * given a user submitted input String, filter out any invalid or restricted
	 * html.
	 * 
	 * @param input
	 *            text (i.e. submitted by a user) than may contain html
	 * @return "clean" version of input, with only valid, whitelisted html
	 *         elements allowed
	 */
	public synchronized String filter(String input, String name) {
		reset();
		String s = input;

		debug("Before["+name+"="+input+"]");

		s = escapeComments(s);
		//debug("     escapeComments: " + s);

		s = balanceHTML(s);
		//debug("        balanceHTML: " + s);

		s = checkTags(s);
	//	debug("          checkTags: " + s);

		s = processRemoveBlanks(s);
	//	debug("processRemoveBlanks: " + s);

		s = validateEntities(s);
	//	debug("    validateEntites: " + s);

		s.replaceAll("alert", "");
		
		debug("After["+name+"="+input+"]");
		return s;
	}

	/**
	 * The method escapes the comments passed in the String .
	 * 
	 * @param s
	 *            It accepts a String value.
	 * @return It returns a String Value.
	 */
	protected String escapeComments(String s) {
	//	debug("In input ::: " + s);
		Pattern p = Pattern.compile("<!--(.*?)-->", Pattern.DOTALL);
		Matcher m = p.matcher(s);
		StringBuffer buf = new StringBuffer();
		if (m.find()) {
			String match = m.group(1); // (.*?)
			m.appendReplacement(buf, "<!--" + htmlSpecialChars(match) + "-->");
		}
		m.appendTail(buf);

	//	debug("In escapeComments ::: " + buf);

		return buf.toString();
	}

	/**
	 * The method is for rest of the html part
	 * 
	 * @param s
	 *            It accepts String value.
	 * @return It accepts String value.
	 */
	protected String balanceHTML(String s) {
	//	debug("in input ::: " + s);
		if (ALWAYS_MAKE_TAGS) {
			s = regexReplace("^>", "", s);
			s = regexReplace("<([^>]*?)(?=<|$)", "<$1>", s);
			s = regexReplace("(^|>)([^<]*?)(?=>)", "$1<$2", s);
		} else {

			s = regexReplace("<([^>]*?)(?=<|$)", "&lt;$1", s);
			s = regexReplace("(^|>)([^<]*?)(?=>)", "$1$2&gt;<", s);

			/**
			 * The last regexp causes '<>' entities to appear (we need to do a
			 * lookahead assertion so that the last bracket can be used in the
			 * next pass of the regexp)
			 */
			s = s.replaceAll("<>", "");
			
			//debug("in balanceHTML::: " + s);
		}
		
		
		return s;
	}

	/**
	 * This method Checks the valid html tags in the String passed.
	 * 
	 * @param s
	 *            It accepts String value.
	 * @return It reurns String value.
	 */
	protected String checkTags(String s) {
		//debug("in input ::: " + s);
		Pattern p = Pattern.compile("<(.*?)>", Pattern.DOTALL);
		Matcher m = p.matcher(s);

		StringBuffer buf = new StringBuffer();
		while (m.find()) {
			String replaceStr = m.group(1);
			replaceStr = processTag(replaceStr);
			m.appendReplacement(buf, replaceStr);
		}
		m.appendTail(buf);

		s = buf.toString();

		/**
		 * These get tallied in processTag (remember to reset before subsequent
		 * calls to filter method)
		 */
		for (String key : vTagCounts.keySet()) {
			for (int ii = 0; ii < vTagCounts.get(key); ii++) {
				s += "</" + key + ">";
			}
		}
	//	debug("in checkTags ::: " + s);

		return s;
	}

	/**
	 * The method uses regular expression to remove blanks.
	 * 
	 * @param s
	 *            It accepts String value.
	 * @return It returns String vlaue.
	 */
	protected String processRemoveBlanks(String s) {
	//	debug("processRemoveBlanks  in input ::: " + s);
		for (String tag : vRemoveBlanks) {
			s = regexReplace("<" + tag + "(\\s[^>]*)?></" + tag + ">", "", s);
			s = regexReplace("<" + tag + "(\\s[^>]*)?/>", "", s);
		}
	//	debug("in processRemoveBlanks ::: " + s);
		return s;
	}

	/**
	 * This Method takes three parameter return the String after replace the
	 * needed String with respective String
	 * 
	 * @param regex_pattern
	 *            It accepts String which contains regular expression
	 * @param replacement
	 *            It accepts String ( replacement String ).
	 * @param s
	 *            It accepts String value.
	 */
	protected String regexReplace(String regex_pattern, String replacement,
			String s) {
		//debug("in input ::: " + s);
		Pattern p = Pattern.compile(regex_pattern);
		Matcher m = p.matcher(s);
		return m.replaceAll(replacement);
	}

	/**
	 * This method process the tag.
	 * 
	 * @param s
	 *            It accepts String value.
	 * @return It returns a String
	 */
	protected String processTag(String s) {
	//	debug("in input ::: " + s);
		// ending tags
		Pattern p = Pattern.compile("^/([a-z0-9]+)", REGEX_FLAGS_SI);
		Matcher m = p.matcher(s);
		if (m.find()) {
			String name = m.group(1).toLowerCase();
			if (vAllowed.containsKey(name)) {
				if (!inArray(name, vSelfClosingTags)) {
					if (vTagCounts.containsKey(name)) {
						vTagCounts.put(name, vTagCounts.get(name) - 1);
						return "</" + name + ">";
					}
				}
			}
		}

		// starting tags
		p = Pattern.compile("^([a-z0-9]+)(.*?)(/?)$", REGEX_FLAGS_SI);
		m = p.matcher(s);
		if (m.find()) {
			String name = m.group(1).toLowerCase();
			String body = m.group(2);
			String ending = m.group(3);
			if (vAllowed.containsKey(name)) {
				String params = "";
				Pattern p2 = Pattern.compile("([a-z0-9]+)=([\"'])(.*?)\\2",
						REGEX_FLAGS_SI);
				Pattern p3 = Pattern.compile("([a-z0-9]+)(=)([^\"\\s']+)",
						REGEX_FLAGS_SI);
				Matcher m2 = p2.matcher(body);
				Matcher m3 = p3.matcher(body);
				List<String> paramNames = new ArrayList<String>();
				List<String> paramValues = new ArrayList<String>();
				while (m2.find()) {
					paramNames.add(m2.group(1)); // ([a-z0-9]+)
					paramValues.add(m2.group(3)); // (.*?)
				}
				while (m3.find()) {
					paramNames.add(m3.group(1)); // ([a-z0-9]+)
					paramValues.add(m3.group(3)); // ([^\"\\s']+)
				}

				String paramName;
				String paramValue;
				for (int ii = 0; ii < paramNames.size(); ii++) {
					paramName = paramNames.get(ii).toLowerCase();
					paramValue = paramValues.get(ii);
					if (vAllowed.get(name).contains(paramName)) {
						if (inArray(paramName, vProtocolAtts)) {
							paramValue = processParamProtocol(paramValue);
						}
						params += " " + paramName + "=\"" + paramValue + "\"";
					}
				}

				if (inArray(name, vSelfClosingTags)) {
					ending = " /";
				}

				if (inArray(name, vNeedClosingTags)) {
					ending = "";
				}

				if (ending == null || ending.length() < 1) {
					if (vTagCounts.containsKey(name)) {
						vTagCounts.put(name, vTagCounts.get(name) + 1);
					} else {
						vTagCounts.put(name, 1);
					}
				} else {
					ending = " /";
				}
				return "<" + name + params + ending + ">";
			} else {
				return "";
			}
		}
		p = Pattern.compile("^!--(.*)--$", REGEX_FLAGS_SI);
		m = p.matcher(s);
		if (m.find()) {
			String comment = m.group();
			return (STRIP_COMMENTS == true) ? "" : "<" + comment + ">";
		}

		return "";
	}

	/**
	 * Thsi method certain protocols
	 * 
	 * @param s
	 *            It accepts String value.
	 * @return It returns String value.
	 */
	protected String processParamProtocol(String s) {
	//	debug("in input ::: " + s);
		s = decodeEntities(s);
		Pattern p = Pattern.compile("^([^:]+):", REGEX_FLAGS_SI);
		Matcher m = p.matcher(s);
		if (m.find()) {
			String protocol = m.group(1);
			if (!inArray(protocol, vAllowedProtocols)) {
				// bad protocol, turn into local anchor link instead
				s = "#" + s.substring(protocol.length() + 1, s.length());
				if (s.startsWith("#//")) {
					s = "#" + s.substring(3, s.length());
				}
			}
		}
	//	debug("processParamProtocol ::: " + s);
		return s;
	}

	/**
	 * This method decodes entity.
	 * 
	 * @param s
	 *            It accepts String value.
	 * @return It returns String value.
	 */
	protected String decodeEntities(String s) {
	//	debug("input ::: " + s);
		StringBuffer buf = new StringBuffer();

		Pattern p = Pattern.compile("&#(\\d+);?");
		Matcher m = p.matcher(s);
		while (m.find()) {
			String match = m.group(1);
			int decimal = Integer.decode(match).intValue();
			m.appendReplacement(buf, chr(decimal));
		}
		m.appendTail(buf);
		s = buf.toString();

		buf = new StringBuffer();
		p = Pattern.compile("&#x([0-9a-f]+);?");
		m = p.matcher(s);
		while (m.find()) {
			String match = m.group(1);
			int decimal = Integer.decode(match).intValue();
			m.appendReplacement(buf, chr(decimal));
		}
		m.appendTail(buf);
		s = buf.toString();

		buf = new StringBuffer();
		p = Pattern.compile("%([0-9a-f]{2});?");
		m = p.matcher(s);
		while (m.find()) {
			String match = m.group(1);
			int decimal = Integer.decode(match).intValue();
			m.appendReplacement(buf, chr(decimal));
		}
		m.appendTail(buf);
		s = buf.toString();

		s = validateEntities(s);
	//	debug("decodeEntities ::: " + s);
		return s;
	}

	/**
	 * This method validates entities
	 * 
	 * @param s
	 *            It accepts String value.
	 * @return It returns String value.
	 */
	protected String validateEntities(String s) {
	//	debug("validateEntities input  ::: " + s);
		/**
		 * validate entities throughout the string
		 */
		
		Pattern p = Pattern.compile("&([^&;]*)(?=(;|&|$))");
		Matcher m = p.matcher(s);
		if (m.find()) {
			String one = m.group(1); // ([^&;]*)
	//		debug(" aftre checkEntity one  ::: " + one);
			String two = m.group(2); // (?=(;|&|$))
	//		debug(" aftre checkEntity two  ::: " + two);
			s = checkEntity(one, two);
	//		debug(" aftre checkEntity two  ::: " + s);
		}
	//	debug(" aftre checkEntity  ::: " + s);
		/**
		 * validate quotes outside of tags
		 */
		p = Pattern.compile("(>|^)([^<]+?)(<|$)", Pattern.DOTALL);
		m = p.matcher(s);
		StringBuffer buf = new StringBuffer();
		if (m.find()) {
			String one = m.group(1); // (>|^)
			String two = m.group(2); // ([^<]+?)
			String three = m.group(3); // (<|$)
			m.appendReplacement(buf, one + two.replaceAll("\"", "&quot;")
					+ three);
		}
	//	debug(" aftre checkEntity m  ::: " + m);
	//	debug("validateEntities buf  ::: " + buf);
		m.appendTail(buf);
	//	debug("validateEntities buf  ::: " + buf);
		return s;
	}

	/**
	 * This method is for check entity.
	 * 
	 * @param preamble
	 *            It accepts String value.
	 * @param term
	 *            It accepts String value.
	 * @return It returns String value.
	 */
	protected String checkEntity(String preamble, String term) {
		if (!";".equals(term)) {
			return "&amp;" + preamble;
		}

		if (isValidEntity(preamble)) {
			return "&" + preamble;
		}

		return "&amp;" + preamble;
	}

	/**
	 * This method returns boolean value true if entity is valid
	 * 
	 * @param entity
	 *            It accepts String value.
	 * @return It returns boolean value.
	 */
	protected boolean isValidEntity(String entity) {
		return inArray(entity, vAllowedEntities);
	}

	/**
	 * This method checks given string in the String array.
	 * 
	 * @param s
	 *            It accepts String value.
	 * @param array
	 *            It accepts String array value.
	 * @return It returns boolean value.
	 */
	private boolean inArray(String s, String[] array) {
		for (String item : array)
			if (item != null && item.equals(s)) {
				return true;
			}

		return false;
	}

}
