package com.ceva.base.common.dto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.owasp.esapi.ESAPI;

public class JsonValueValidator {
	 private static List<Pattern> XSS_INPUT_PATTERNS = new ArrayList<Pattern>();
	public static JSONObject call(JSONObject obj) throws Exception {
	    Iterator<String> iterator = obj.keys();
	    String key = null;
	    while (iterator.hasNext()) {
	        key = (String) iterator.next();
	        if ((obj.optJSONArray(key)==null) && (obj.optJSONObject(key)==null)) {
	               
	        	obj.put(key, stripXSS(obj.getString(key)));
	        }
	        if (obj.optJSONObject(key) != null) {
	            call(obj.getJSONObject(key));
	        }
	        if (obj.optJSONArray(key) != null) {
	            JSONArray jArray = obj.getJSONArray(key);
	            for (int i=0;i<jArray.size();i++) {
	                    call(jArray.getJSONObject(i));
	            }
	        }
	    }
	    return obj;
	}
	public static String stripXSS(String value) {

		try {

			if (value != null) {
				// NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
				// avoid encoded attacks.
				value = ESAPI.encoder().canonicalize(value);

				// Avoid null characters
				value = value.replaceAll("\0", "");
				value = value.replaceAll("&", "&amp;");
				value = value.replaceAll("<", "&lt;");
				value = value.replaceAll(">", "&gt;");
				value = value.replaceAll("\"", "&quot;");
				value = value.replaceAll("'", "&#x27;");
				//value = value.replaceAll("/", "&#x2F;");

				// test against known XSS input patterns
				for (Pattern xssInputPattern : XSS_INPUT_PATTERNS) {
					value = xssInputPattern.matcher(value).replaceAll("");
				}
			}

		} catch (Exception ex) {
			System.out.println("Could not strip XSS from value = " + value + " | ex = " + ex.getMessage());
		}

		return value;
	}
	
    
	 static { 
	   // Avoid anything between script tags 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE)); 
	 
	   // avoid iframes 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("<iframe(.*?)>(.*?)</iframe>", Pattern.CASE_INSENSITIVE)); 
	 
	   // Avoid anything in a src='...' type of expression 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)); 
	 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)); 
	 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("src[\r\n]*=[\r\n]*([^>]+)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)); 
	 
	   // Remove any lonesome </script> tag 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("</script>", Pattern.CASE_INSENSITIVE)); 
	 
	   // Remove any lonesome <script ...> tag 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)); 
	 
	   // Avoid eval(...) expressions 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)); 
	 
	   // Avoid expression(...) expressions 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)); 
	 
	   // Avoid javascript:... expressions 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE)); 
	 
	   // Avoid vbscript:... expressions 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE)); 
	 
	   // Avoid onload= expressions 
	   XSS_INPUT_PATTERNS.add(Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)); 
	 } 
	public static void main(String[] args) {
		String data ="{\"userId\":\"PANDEY\",\"password\":\"4pL8AkLbWZiP0fIUhf93qQ==\",\"APPL_NAME\":\"banking\",\"RANDOM_VALUE\":\"104292\",\"PASSWORDKEY\":\"P@()34Hjk29$#@bdeHYE!\",\"SALT\":\"a00448af12a60bcbff48b1a698280558\",\"IV\":\"01928374565648392012910388456912\",\"REMOTE_ADDR\":\"0:0:0:0:0:0:0:1\",\"REMOTE_IP\":\"0:0:0:0:0:0:0:1\",\"getHiddenPassword\":\"4pL8AkLbWZiP0fIUhf93qQ==\",\"ip\":\"0:0:0:0:0:0:0:1\"}";
		data="{\"userID\":\"pandey' or a='kailash\",\"fromDate\":\"30/01/2017\",\"toDate\":\"30/01/2017\",\"actionname\":\"\"}";
		try {
			
			JSONObject obj1 = JSONObject.fromObject(data);
			JSONObject obj = new JSONObject();
			JSONArray jsonarray = new JSONArray();
			jsonarray.add(obj1);
			obj.put("name", "name");
			obj.put("name1", "name1");
			obj.put("name2", "name2");
			obj.put("name3", jsonarray);
			
			System.out.println(data);
			System.out.println(call(obj).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
