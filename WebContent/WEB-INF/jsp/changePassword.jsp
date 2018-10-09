 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
 <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Amur | Change Password</title>

<%@ include file="css.jsp" %>
 <script src="${pageContext.request.contextPath}/js/jquery.validate.min.js"></script>
 

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
</style>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.min.css"/>
<script type="text/javascript">
	
function encryptPassword(){
	var encryptPass;
	var password=$("#newPassword").val();
	encryptPass = b64_sha256(password);	
	$("#newPassword").val(encryptPass);
	
	var confirmpassword=$("#confirmNewPassword").val();
	confirmEncryptPass = b64_sha256(confirmpassword);	
	$("#confirmNewPassword").val(confirmEncryptPass);
}

var validationRules = {
   rules : {
	newPassword : { required : true, minlength:7 },
	confirmNewPassword : { required : true, minlength:7,equalTo: "#newPassword"}
   },  
   messages : {
	newPassword : { 
			required : "Enter New Password Required",
			minlength : "Enter New Password required minimum 7 characters"
        },
	confirmNewPassword : { 
			required : "Confirm New Password Required",
			minlength : "Confirm New Password required minimum 7 characters"
        }
   } 
 };
 
function submitPasswordInfo(){
	$("#form1").validate(validationRules);
	if($("#form1").valid()){
		encryptPassword();
		$("#form1")[0].action='<%=request.getContextPath()%>/changePassword.action';
		$("#form1").submit();
		return true; 
	}else{
		return false;
	}
}
$(document).ready(function() {

	$('#confirmNewPassword').bind('keypress', function(e) {
		if (e.keyCode == 13) {
			submitPasswordInfo();
		}
	});

	$('#confirm-save').on('click', function() {
		submitPasswordInfo();
	}); 
});
</script>
</head>
<body>
	<form name="form1" id="form1" method="post" action="">
		<section class="container"> 
			<div class="login"> 
				<section class="logoheader">
					<img src="images/Posta_logo.jpg" width="200" height="119"
						alt="logo">
				</section>

				<s:actionmessage />
				<s:actionerror />
				<p>
					<%= session.getAttribute(CevaCommonConstants.MAKER_ID) %>
					<input type="hidden" name="userid" id="userid" value="<%= session.getAttribute(CevaCommonConstants.MAKER_ID) %>" />
				</p>
				<p>
					<input  name="newPassword" id="newPassword" type="password" autocomplete="off"
						required="true" value="" placeholder="Enter New Password" />
				</p>
				<p>
					<input  name="confirmNewPassword" id="confirmNewPassword" type="password" autocomplete="off"
						required="true" value="" placeholder="Confirm Password" />
				</p>
				<p class="submit">
					<input type="button" name="confirm-save" id="confirm-save" value="Submit"  
						class="btn btn-primary"    />
					 
				</p> 
			</div>

		</section> 
<script src="${pageContext.request.contextPath}/js/authenticate.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sha256.min.js"></script> 
	</form> 
</body> 
</html>