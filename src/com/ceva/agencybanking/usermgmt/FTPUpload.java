package com.ceva.agencybanking.usermgmt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class FTPUpload {

	private String server;
	private int port;
	private String username;
	private String password;
	private String localFilePath;

	private static Logger logger = Logger.getLogger(FTPUpload.class);

	public FTPUpload() {
	}

	public FTPUpload(String server, int port, String username, String password,
			String localFilePath) {
		this.server = server;
		this.port = port;
		this.username = username;
		this.password = password;
		this.localFilePath = localFilePath;
	}

	private boolean doJSCHSftp(String fileName) {

		boolean done = false;
		Session session = null;
		Channel channel = null;
		ChannelSftp channelSftp = null;
		JSch jsch = null;

		java.util.Properties config = null;

		try {
			logger.debug("Inside JSCH File Upload...");
			jsch = new JSch();
			logger.debug("Username[" + username + "] Server[" + server
					+ "] Port[" + port + "]");
			session = jsch.getSession(username, server, port);
			logger.debug("Session Is About To Create....");
			session.setPassword(password);
			config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");

			session.setConfig(config);
			session.setTimeout(7000);
			session.connect();
			logger.debug("Finally Got Session created....");
			channel = session.openChannel("sftp");
			channel.connect();
			logger.debug("Channel Opened & Connected...");
			channelSftp = (ChannelSftp) channel;

			logger.debug("Channel is ::: " + channelSftp.isConnected());

			boolean statusFtp = false;

			try {
				statusFtp = uploadFile(localFilePath + fileName,
						channelSftp.pwd() + fileName, channelSftp);
			} catch (Exception e) {
				logger.debug(" Exception while uploading.... " + e.getMessage());
			}
			logger.debug("Files Uploaded Successfully.... " + statusFtp);

			channelSftp.exit();
			if (statusFtp) {
				done = true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.debug("FException is :" + ex.getMessage());
		} finally {
			if (channel.isConnected()) {
				channel.disconnect();
			}
			if (channelSftp.isConnected()) {
				channelSftp.disconnect();
			}

			if (session.isConnected()) {
				session.disconnect();
			}

			jsch = null;
			config = null;
		}

		return done;
	}

	private String ext = ".zip";

	public boolean ftpRemote(String fileName) {
		boolean flag = false;
		FilenameFilter filter = null;

		if (fileName.indexOf("KCB") != -1) {
			ext = ".pgp";
		} else {
			ext = ".zip";
		}
		filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (name.endsWith(ext))
					return true;
				return false;
			}
		};

		File f[] = new File(localFilePath).listFiles(filter);
		File pickupFile = null;
		if (f == null || f.length == 0) {
			logger.debug(ext + " File Not Found....");
		} else {
			for (int index = 0; index < f.length; index++) {
				pickupFile = f[index];
				flag = doJSCHSftp(pickupFile.getName());
			}
		}

		return flag;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	private boolean uploadFile(String localPath, String remotePath,
			ChannelSftp channelSftp) throws IOException {
		InputStream inputSrr = null;
		boolean flag = false;
		try {
			inputSrr = new FileInputStream(localPath);
			channelSftp.put(inputSrr, remotePath);
			flag = true;
		} catch (SftpException e) {
			e.printStackTrace();
			logger.debug("Got Exception while doing Uploading File To Remote Server ::: "
					+ e.getMessage());
		} finally {
			if (inputSrr != null) {
				inputSrr.close();
			}
		}

		return flag;
	}

	public static void main(String[] args) {

	}

}