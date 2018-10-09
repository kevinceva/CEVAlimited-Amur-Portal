package com.ceva.user.security;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class UserLockingClient {

	private static Logger logger = Logger.getLogger(UserLockingClient.class);

	public static String callLockingServer(String ip, int port, String request) {
		String resp = null;
		Socket client = null;

		DataOutputStream out = null;
		DataInputStream ois = null;
		OutputStream outToServer = null;
		try {

			logger.debug("UserLockingClient - " + ip);
			client = new Socket(ip, port);
			logger.debug("Just connected to   " + client.getRemoteSocketAddress());
			outToServer = client.getOutputStream();
			out = new DataOutputStream(outToServer);

			out.writeUTF(request);
			ois = new DataInputStream(client.getInputStream());
			resp = ois.readUTF();
			
			logger.debug("Respose Message : " + resp);

		} catch (UnknownHostException e) {
			logger.debug("UnknownHostException ::: " + e.getMessage());
		} catch (IOException e) {
			logger.debug("IOException :::  " + e.getMessage());
		} catch (Exception e) {
			logger.debug("Exception :::  " + e.getMessage());
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
				if (out != null) {
					out.close();
				}
				if (client != null) {
					client.close();
				}
			} catch (Exception e) {
				logger.debug("In Finally Exceptions is ::: " + e.getMessage());
			}
			client = null;
			
			if (resp == null) {
				resp = "NOVAL";
			}

		}
		return resp;
	}
}