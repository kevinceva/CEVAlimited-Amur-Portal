<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 <html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Ceva</title> 
<meta charset="utf-8">
<title>Amur</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">


<link id="bs-css" href="<%= ctxstr %>/css/bootstrap-cerulean.min.css" rel="stylesheet" />
<style type="text/css">
	  body {
		padding-bottom: 40px;
	  }
	  .sidebar-nav {
		padding: 9px 0;
	  }
	  #nav {
    float: left;
	width:170px;
	color:#ffffff;
    border-top: 1px solid #999;
    border-right: 1px solid #999;
    border-left: 1px solid #999;
	list-style:none;
}
#nav li a {
    display: block;
    padding: 10px 15px;
    background: #AC4A02;
    border-top: 1px solid #eee;
    border-bottom: 1px solid #999;
    text-decoration: none;
    color: #ffffff;
	list-style:none;
}
#nav li a:hover, #nav li a.active {
    background: #FC8023;
    color: #fff;
}
#nav li ul {
    display: none; 
	list-style:none;
}
#nav li ul li a {
    padding: 5px 5px;
    background: #D35B03;
    border-bottom: 1px dotted #ccc;
	list-style:none;
}
.errors {
	background-color:#FFCCCC;
	border:0px solid #CC0000;
	width:400px;
	margin-bottom:8px;
}
.errors li{ 
	list-style: none; 
}
</style>
<script src="<%= ctxstr %>/js/jquery-1.12.3.min.js"></script>    
<script language="javascript" type="text/javascript">
	$(document).ready(function () {
		$("#form1")[0].action = "<%=ctxstr %>/<%=appName %>/home.action";
		$("#form1").submit();
	});
</script> 
</head>
<body>
	<form class="form-horizontal" name="form1" id="form1" action="" method="post">
	</form>
</body>
</html>