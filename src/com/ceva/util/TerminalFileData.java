package com.ceva.util;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

public class TerminalFileData {

	public static String CreateFileData(HashMap<String, String> users,
			String tid, String mid, String adminAndSupervisorPwd, String tmk,
			String location) {
		StringBuilder data = null;
		String loc[] = null;
		String totalStr = "";
		Iterator<String> it = null;
		try {
			data = new StringBuilder();

			it = users.keySet().iterator();
			while (it.hasNext()) {
				String string = (String) it.next();
				data.append(string).append(",").append(users.get(string));
				data.append("*");
			}
			data.append("*");

			try {
				loc = location.split("##");

				totalStr = StringUtils.rightPad(loc[0], 22, " ")
						+ StringUtils.rightPad(loc[1], 13, " ")
						+ StringUtils.rightPad(loc[2], 3, " ")
						+ StringUtils.rightPad(loc[3], 2, " ");
			} catch (Exception e) {
				totalStr = StringUtils.rightPad(" ", 40, " ");
				 
			}

			data = data.deleteCharAt(data.length() - 1);
			data.append("^#");
			data.append(tid).append("#").append(mid);
			data.append("#").append(totalStr);
			data.append("@");
			data.append(adminAndSupervisorPwd);
			data.append("|").append(tmk);
		} catch (Exception e) {
			return " ";
		}

		return data.toString();
	}
}
