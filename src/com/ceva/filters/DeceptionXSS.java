package com.ceva.filters;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.ceva.base.common.utils.DBConnector;

public final class DeceptionXSS extends HttpServletRequestWrapper {  
	Hashtable<String, String> XSSRocks=null;
	HttpServletRequest dirvertRequest=null;
	HttpServletResponse dirvertResponse=null;

	private void init() {
	}

	protected static final Logger logger = Logger
	.getLogger(DeceptionXSS.class);
	public DeceptionXSS(HttpServletRequest servletRequest,HttpServletResponse servletResponse) {  
		super(servletRequest);
		dirvertRequest= servletRequest;
		dirvertResponse=servletResponse;
		XSSRocks= new Hashtable<String, String>();
		ResourceBundle securityProp=null;
		try {
			securityProp = ResourceBundle.getBundle("resource/XSS");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		XSSRocks.put("substitute", securityProp.getString("substitute"));
		XSSRocks.put("checkrudly", securityProp.getString("checkrudly"));
		XSSRocks.put("exemptedFields", securityProp.getString("exemptedFields"));
	}  

	public String[] getParameterValues(String parameter) {  
		String[] values = super.getParameterValues(parameter);  
		if (values==null)  {  
			return null;  
		}  
		int count = values.length;  
		String[] encodedValues = new String[count];  
		
			for (int i = 0; i < count; i++) {  

				dirvertUrl(noPardon(values[i], XSSRocks.get("checkrudly")));
				encodedValues[i] = substituteValue(values[i],XSSRocks.get("substitute"));
			}

		return encodedValues;   
	}  

	public String getParameter(String parameter) {  
		String value = super.getParameter(parameter);  
		if (value == null) {  
			return null;   
		}  
		if(!exemptFeild(parameter, XSSRocks.get("exemptedFields")))
		{
			dirvertUrl(noPardon(value, XSSRocks.get("checkrudly")));
			value= substituteValue(value,XSSRocks.get("substitute"));
			System.out.println("value ["+value+"]");
		}
		return value;
	}  

	public String getHeader(String name) {  
		String value = super.getHeader(name);  
		if (value == null)  
			return null;  
		dirvertUrl(noPardon(value, XSSRocks.get("checkrudly")));
		return substituteValue(value,XSSRocks.get("substitute")); 

	}  

	@Override
	public Map getParameterMap() {

		Map<String,String[]> map= super.getParameterMap();
		Map<String,String[]> sendMap= new HashMap<String, String[]>();

		Set<Entry<String, String[]>> set=map.entrySet();

		Iterator<Map.Entry<String, String[]>> it=set.iterator();
		while(it.hasNext())
		{
			Entry<String, String[]>keyValue=it.next();
			String [] value=keyValue.getValue();
			if(!exemptFeild(keyValue.getKey(), XSSRocks.get("exemptedFields")))
			{
				for (int i = 0; i < value.length; i++) {
					dirvertUrl(noPardon(value[i], XSSRocks.get("checkrudly")));
					value[i]=substituteValue(value[i],XSSRocks.get("substitute"));
				}
			}
			System.out.println("value ["+value+"]");
			sendMap.put(keyValue.getKey(), value);
		}



		return sendMap;
	}


	private String substituteValue(String input,String substitute)
	{
		StringTokenizer stoken= new StringTokenizer(substitute,"$");
		while(stoken.hasMoreTokens())
		{
			String supplant=stoken.nextToken();
			input=input.replace(supplant.split(",")[0], supplant.split(",")[1]);
		}
		
		return input;
	}

	private boolean noPardon(String paramValue,String checkrudlyValue)
	{

		boolean corrupted=false;
		StringTokenizer stoken= new StringTokenizer(checkrudlyValue,"$");
		while(stoken.hasMoreTokens())
		{
			if(paramValue.toUpperCase().indexOf(stoken.nextToken().toUpperCase())!=-1)
			{
				corrupted=true;
				break;
			}
		}
	
		return corrupted;
	}
	private void dirvertUrl(boolean isCorrupted)
	{
		if(isCorrupted)
		{
		if(dirvertRequest.getSession(false)!=null)
		{
			dirvertRequest.getSession(false).invalidate();
		}
		dirvertResponse.setStatus(301);
		dirvertResponse.setHeader( "Location",
				""+(String)dirvertRequest.getContextPath()+"/error/error.jsp");
		}
	}
	
	private boolean exemptFeild(String field, String allVlaues)
	{
		boolean trueRfalse=false;

		if(allVlaues.indexOf(field)!=-1)
		{
			trueRfalse=true;
		}

		return trueRfalse;
	}
}  