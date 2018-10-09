<%-- <%@taglib uri="/struts-tags" prefix="s"%> --%>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>

<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!-->
<html lang="en" >
<!--<![endif]-->

<%@ include file="css.jsp" %>
<head>
<%
	String ctxstr = request.getContextPath();
%>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Amur | LOGIN</title>


<style type="text/css">
.errorMessage {
	font-weight: bold;
	color: red;
	padding: 2px 8px;
	margin-top: 2px;
}

.errors {
	background-color: #FFCCCC;
	border: 0px solid #CC0000;
	width: 400px;
	margin-bottom: 8px;
}

.error {
	background-color: #FFCCCC;
	border: 0px solid #CC0000;
	width: 400px;
	margin-bottom: 8px;
}

.errors li {
	list-style: none;
}

body {
	padding-bottom: 40px;
}

.sidebar-nav {
	padding: 9px 0;
}

.full-spn-right {
	margin-left: 1200px;
}
input#userid{text-transform:uppercase};
</style>

<script type="text/javascript">
 

$(document).ready(function() {

var resmsg = '${responseJSON.ackmsg}';
console.log("resmsg : ="+resmsg);

});
</script>
</head>
<body>
	<form name="form1" id="form1" method="post" action="">
		<section class="container">
	 
			<div class="login" id="reg-params" >
				<!-- <h1>BIMA Credo LOGIN PAGE</h1> -->
				<section class="logoheader">
					<img src="<%=ctxstr%>/images/Posta_logo.jpg" width="200" height="119"
						alt="logo">
					<div> <font color="red" size="3" ><b>Agent Registration</b></font> &nbsp;&nbsp;&nbsp; <a href="<%=ctxstr%>/jsp/CICPDF.pdf">Brochure</a> </div>	
				</section>
				<label> <font color="green" size="3" ><b>Agent Registered Successfully</b></font> </label>
				<s:actionmessage />
				<s:actionerror />
				
			</div>
			
		</section> 
<script src="${pageContext.request.contextPath}/js/authenticate.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sha256.min.js"></script> 
<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js"></script>
	</form>
</body>
</html>
