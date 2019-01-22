package com.ceva.util;

import org.apache.log4j.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpPostRequestHandler {

	private Logger logger = Logger.getLogger(HttpPostRequestHandler.class);

	public String sendRestPostRequest(String url)
			throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setHeader("channel", "WEB");
		post.setHeader("version", "1.6");
		logger.debug("URL:::: " + url);
		HttpResponse response = client.execute(post);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response
				.getEntity().getContent()));
		String line = "";
		StringBuilder resp = new StringBuilder();

		while ((line = rd.readLine()) != null) {
			resp.append(line);

		}
		rd.close();
		logger.debug("response String in HTTP Class:" + resp.toString());
		return resp.toString();

	}

}
