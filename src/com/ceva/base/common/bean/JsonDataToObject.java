package com.ceva.base.common.bean;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonDataToObject {

	private Logger logger = Logger.getLogger(JsonDataToObject.class);

	private String jsonData;
	private String searchData;
	private Object data;

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getSearchData() {
		return searchData;
	}

	public void setSearchData(String searchData) {
		this.searchData = searchData;
	}

	public Object getData() {
		boolean jsonCheck = false;
		synchronized (this) {
			JSONObject jsonObject = null;

			JSONArray jsonArray = null;
			net.sf.json.JSONObject jsonResult = null;
			net.sf.json.JSONArray jsonArray1 = null;

			try {
				logger.debug("Json Data ::: " + jsonData);
				logger.debug("searchData ::: " + searchData);

				jsonObject = new JSONObject(jsonData);
				jsonCheck = true;
			} catch (Exception e) {
				logger.debug("Not a JSON String... Passing To If ::: "
						+ jsonResult);
			}

			try {
				if (!jsonCheck) {
					logger.debug("Inside String To Array ");
					if(jsonData != null && !jsonData.equalsIgnoreCase("")) {
						if (jsonData.startsWith("#")) {
							// data = "{}";
							jsonData = jsonData.substring(1, jsonData.length());
						}

						if (jsonData.lastIndexOf("#") == (jsonData.length() - 1)) {
							jsonData = jsonData.substring(0, jsonData.length() - 1);
						}

						char ch[] = searchData.toCharArray();

						if (ch.length == 1 && ch[0] == '#') {

							String str1[] = jsonData.split(String.valueOf(ch[0]));
							jsonArray1 = new net.sf.json.JSONArray();

							for (String string : str1) {
								jsonArray1.add(string);
							}

						} else {
							System.out
									.println("Invalid Format .. please check the search string.. the searchformat should be #,");
						}

						data = jsonArray1;
					} else {
						data = null;
					}
					

				} else {

					jsonArray = jsonObject.names();
					jsonResult = new net.sf.json.JSONObject();

					for (int index = 0; index < jsonArray.length(); index++) {
						if (jsonArray.get(index).toString().indexOf(searchData) != -1) {
							jsonResult
									.put(jsonArray.get(index).toString()
											.replace(".", "_"),
											jsonObject.getString(jsonArray.get(
													index).toString()));

						}
					}

					logger.debug("Result is :: " + jsonResult);
					data = jsonResult;
				}

			} catch (Exception e) {
				e.printStackTrace();
				data = null;
			} finally {
				jsonObject = null;
				jsonArray = null;
			}
		}

		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static void main(String[] args) {
		String data = "one,two,three,four#five,six,seven,,#";
		String search = "_RIGHTS";

		JsonDataToObject json1 = new JsonDataToObject();
		json1.setJsonData(data);
		json1.setSearchData("#");
		json1.getData();

	}
}
