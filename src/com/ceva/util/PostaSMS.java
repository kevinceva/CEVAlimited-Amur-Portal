package com.ceva.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PostaSMS {

	private static Logger logger = Logger.getLogger(PostaSMS.class);

	public static String sendJson(String jsonString, String smsUrl) {
		StringBuffer buffer = null;
		JSONArray array = null;
		JSONArray array1 = null;
		try {
			logger.info("The JSON String is :::: " + jsonString);
			buffer = new StringBuffer(20);
			buffer.append("");
			array = new JSONArray(jsonString);
			array1 = new JSONArray();
			
			for (int i = 0; i < array.length(); i++) { 
				array1.put(testService(array.getJSONObject(i), smsUrl));
			}

		} catch (Exception e) {
			logger.info("Got Exception while sending sms....");
		}

		return array1.toString();
	}

	private static JSONObject testService(JSONObject jsonData, String smsUrl)
			throws UnsupportedOperationException, SOAPException, IOException,
			JSONException {
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory
				.newInstance();
		SOAPConnection connection = soapConnectionFactory.createConnection();

		URL endpoint = new URL(smsUrl);

		MessageFactory factory = MessageFactory.newInstance();

		final SOAPMessage message = factory.createMessage();

		message.getSOAPPart()
				.getEnvelope()
				.setAttributeNS("http://www.w3.org/2000/xmlns/",
						"xmlns:soapenv",
						"http://schemas.xmlsoap.org/soap/envelope/");
		// message.getSOAPPart().getEnvelope().removeAttributeNS("http://schemas.xmlsoap.org/soap/envelope/",
		// "env");
		message.getSOAPPart().getEnvelope().removeAttribute("xmlns:SOAP-ENV");
		message.getSOAPPart().getEnvelope().setPrefix("soapenv");
		message.getSOAPHeader().setPrefix("soapenv");
		message.getSOAPBody().setPrefix("soapenv");
		// message.getSOAPPart().getEnvelope().getHeader().detachNode();

		SOAPPart soapPart = message.getSOAPPart();
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration("xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		envelope.addNamespaceDeclaration("xsd",
				"http://www.w3.org/2001/XMLSchema");
		envelope.addNamespaceDeclaration("urn", "urn:xmethods-delayed-quotes");
		envelope.setEncodingStyle("http://schemas.xmlsoap.org/soap/encoding/");

		SOAPBody soapBody = envelope.getBody();
		SOAPElement soapBodyElem = soapBody.addChildElement("processSendSMS",
				"urn");
		soapBodyElem.setAttribute("encodingStyle",
				"http://schemas.xmlsoap.org/soap/encoding/");

		SOAPElement soapBodyElem1 = null;
		String txnRef = "";
		for (Iterator<?> itr = jsonData.sortedKeys(); itr.hasNext();) {
			String key = (String) itr.next();
			String value = jsonData.getString(key);

			if (value.indexOf("~~") != -1) {
				soapBodyElem1 = soapBodyElem.addChildElement(key);
				soapBodyElem1.addTextNode(value.split("~~")[0]).setAttribute(
						"xsi:type", "xsd:" + value.split("~~")[1]);
			}

			if (key.contains("txnRefNo")) {
				txnRef = value;
			}
		}

		SOAPMessage response = null;
		String returnCode = "";
		String desc = "";
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();

			message.saveChanges();
			message.writeTo(os);
			logger.info("\n Soap request : "
					+ new String(os.toByteArray(), "UTF-8"));

			logger.debug("soap connection::"+connection);
			logger.debug("message:::"+message);
			logger.debug("endpoint:::"+endpoint);
			
			response = connection.call(message, endpoint);

			logger.info(response.getContentDescription());

			returnCode = response.getSOAPBody()
					.getElementsByTagName("return_code").item(0)
					.getFirstChild().getNodeValue();

			logger.info("returnCode::"+returnCode);

			desc = response.getSOAPBody().getElementsByTagName("description")
					.item(0).getFirstChild().getNodeValue();
			logger.info("description::"+desc);
			
			jsonData.put("respcode", "00");
			jsonData.put("desc", "Success");

		} catch (Exception e) {
			logger.info("Exception while getting response from gateway :::: "
					+ e.getMessage());
			returnCode = "99";
			desc = "Gateway Exception.";
		} finally {
			if (os != null)
				os.close();
		}

		

		// return txnRef + "$$" + returnCode + "$$" + desc;
		return jsonData;
	}

}
