 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
 <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Amur | Change Password Acknowledge</title>

<%@ include file="css.jsp" %>
<style type="text/css">
.errors {
	background-color:#FFCCCC;
	border:0px solid #CC0000;
	width:400px;
	margin-bottom:8px;
}
.error {
	background-color:#FFCCCC;
	border:0px solid #CC0000;
	width:400px;
	margin-bottom:8px;
}
.errors li{ 
	list-style: none; 
}
 
body {
	padding-bottom: 40px;
  }
.sidebar-nav {
	padding: 9px 0;
  }
.full-spn-right{ 
margin-left: 1200px; 
}	
.messages {
  font-weight: bold;
  color: green;
  padding: 2px 8px;
  margin-top: 2px;
}

.errors{
  font-weight: bold;
  color: red;
  padding: 2px 8px;
  margin-top: 2px;
} 
</style>  
 
</head>
<body>
	<form name="form1" id="form1" method="post" action="">
		<section class="container"> 
			<div class="login"> 
				<section class="logoheader">
					<img src="images/Posta_logo.jpg" width="200" height="119"
						alt="logo">
				</section> 
				 
				<div class="box-content alerts"> 
					<div class="alert alert-success">
						<button type="button" class="close" data-dismiss="alert">×</button>
						<strong>Password changed successfully, </strong> Please click <a href='cevaappl.action'>here</a> to redirect to Login page. 
					</div> 
				</div>  
			</div> 
		</section>  
	</form> 
</body> 
</html>