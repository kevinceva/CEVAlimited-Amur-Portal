<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link href="${pageContext.request.contextPath}/css/loginstyle.css" rel="stylesheet" media="screen" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=application.getInitParameter("pageTitle") %></title>

<style type="text/css">
html, body {
  font-size: 62.5%;
  height: 100%;
  overflow: hidden;
}
.demoerror {
 overflow: hidden;
font-size: 20px;
  color:#00AEEF;
 

}
</style>
</head>
<body>
	<br/><br/>
	<div class="demoerror" >
	<img src="../images/logo.png" width="200" height="80" 
						alt="logo">
						<br/><br/><br/><br/><br/>
	<center><strong><font color="#00AEEF">client id is not valid from server,and receives a BAD_REQUEST status from server .</font></strong></center>
</div>
</body>
</html>