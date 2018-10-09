package com.ceva.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

public class PasswordValidation {

	private static Logger logger = Logger.getLogger(PasswordValidation.class);

	public static Map<String, String> maxCheck(String currentPwd, String dbpwd,
			final String oldPwds) {
		logger.debug("Inside PasswordValidation... ");
		Map<String, String> map = null;
		ArrayList<String> al = null;
		StringTokenizer st = null;
		int len = 0;
		try {
			map = new HashMap<String, String>();

			if (isNotEmpty(currentPwd) && isNotEmpty(dbpwd)
					&& isNotEmpty(oldPwds)) {
				logger.debug("--");
				st = new StringTokenizer(oldPwds, ",");
				len = st.countTokens();
				al = new ArrayList<String>();

				while (st.hasMoreTokens()) {
					al.add(st.nextToken());
				}

				if (!al.contains(currentPwd) && !dbpwd.equals(currentPwd)) {
					if (len >= 5) {
						al.remove(0);
					}
					al.add(al.size(), dbpwd);
					map.put("DBPWD", currentPwd);
					map.put("OLDPWDS", altoString(al));
					map.put("RESPCODE", "00");
					map.put("RESPDESC", "No Problem");
					return map;
				} else {
					map.put("RESPCODE", "01");
					map.put("RESPDESC", "Password Already exsists");
					return map;
				}
			} else {
				if (isNotEmpty(dbpwd) && !isNotEmpty(oldPwds)) {

					map.put("DBPWD", currentPwd);
					map.put("OLDPWDS", dbpwd);
					map.put("RESPCODE", "00");
					map.put("RESPDESC", "DB password moved to Back up");
					return map;
				}
				if (!isNotEmpty(dbpwd) && !isNotEmpty(oldPwds)) {

					map.put("DBPWD", currentPwd);
					map.put("OLDPWDS", dbpwd);
					map.put("RESPCODE", "00");
					map.put("RESPDESC", "DB password moved to Back up");
					return map;
				}

			}
			logger.debug("|MaxCheck| Before Exiting.");
		} catch (Exception e) {
			logger.debug("|MaxCheck| Exception is ::: " + e.getMessage());
		}

		return map;
	}

	private static String altoString(final ArrayList<String> al) {
		StringBuilder sb = null;

		try {
			sb = new StringBuilder();
			for (String s : al) {
				sb.append(s).append(",");
			}
			if (sb.length() > 1) {
				sb = sb.deleteCharAt(sb.length() - 1);
			}
		} catch (Exception e) {
			logger.debug("|PasswordValidation| Exception is ::: "
					+ e.getMessage());
		}

		return sb.toString();
	}

	private static boolean isNotEmpty(String str) {
		boolean resp = false;
		if (str != null && str.trim().length() > 0) {
			resp = true;
		}

		return resp;
	}

	public static void main(String[] args) {

		String currentPassword = "dbpwd";
		String dbpwd = "dbpwd";
		String oldPasswords = "Pass12!3,pandey12,surya12,text12,poopo12@!";
		System.out.println(maxCheck(currentPassword, dbpwd, oldPasswords));
	}

}
