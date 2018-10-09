package com.ceva.posta.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AppletToServlet  extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		response.setContentType("application/x-java-serialized-object");
		InputStream inputStream = request.getInputStream();
		ObjectInputStream inputFromApplet = new ObjectInputStream(inputStream);
		String string = (String) inputFromApplet.readObject();
		// getting string value and passing to applet
		OutputStream outputStream = response.getOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(string);
		objectOutputStream.flush();
		objectOutputStream.close();
		} catch (Exception e) {
		e.printStackTrace();
		}
		}
}
