<%@taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>

<!DOCTYPE html>
<html lang="en" >

<%@ include file="css.jsp" %>
<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="Antony Kithaka">

<%
	String ctxstr = request.getContextPath();
%>
<%
	String appName = "";
	String random = "";
	try {
		appName = (session.getAttribute(
				CevaCommonConstants.ACCESS_APPL_NAME).toString() == null
				? "AgencyBanking"
				: session.getAttribute(
						CevaCommonConstants.ACCESS_APPL_NAME)
						.toString());
		random = session.getAttribute("RANDOM_VALUE").toString();
		if (random == null) //session.setAttribute("RANDOM_VALUE", System.currentTimeMillis());
			response.sendRedirect(ctxstr + "/logout.action");

	} catch (Exception ee) {
		response.sendRedirect(ctxstr + "/cevaappl.action");
	}
%>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Amur | LOGIN</title>


<style type="text/css">

.sidebar-nav {
	padding: 9px 0;
}

.full-spn-right {
	margin-left: 1200px;
}

</style>

<script type="text/javascript">
 
function encryptPassword(){
	var appPassword;
	var encryptPass;
 	var randomNumber= '<%=random%>';
	//alert("RANDOM_VALUE>>>>"+randomNumber);
	var password=$("#password").val();
	encryptPass = b64_sha256(password);	
	console.log(encryptPass);
	appPassword = encryptPass.concat(randomNumber);
	finalPassword = b64_sha256(appPassword);
	$("#encryptPassword").val(finalPassword);
}

function testPassword(pwString) {
    var strength = 0;

    strength += /[A-Z]+/.test(pwString) ? 1 : 0;
    strength += /[a-z]+/.test(pwString) ? 1 : 0;
    strength += /[0-9]+/.test(pwString) ? 1 : 0;
    strength += /[\W]+/.test(pwString) ? 1 : 0;

    switch(strength) {
        case 2:
            // its's medium!
            break;
        case 3:
            // it's strong!
            break;
        default:
            // it's weak!
            break;
    }
	return strength;
}

var loginvalidationRules = {
   rules : {
    userid : { required : true },
	password : { required : true, minlength:7,maxlength:16 }
   },  
   messages : {
    userid : { 
			required : "User Name Required"
        },
	password : { 
			required : "User Password Required",
			minlength : "Password required minimum 7 characters",
			maxlength : "Password allows maximum 16 characters"
        }
   } 
 };
 
 function submit(){
	 $("#form1").validate(loginvalidationRules);
		if($("#form1").valid()){
			var password=$("#password").val();
			var level=testPassword(password); 
			if(level==1 ||level==2 || level==3 || level ==4) {
				$('#userid').val($('#userid').val().toUpperCase());
				encryptPassword();
				$("#form1")[0].action='<%=request.getContextPath()%>/weblogin.action';
				$("#form1").submit();
				return true;
			} else {
				alert("Password must contain upper and lower case letters and number.");
				return false;
			}

		} else {
			return false;
		}

	}

$(document).ready(function() {

	$('#password').bind('keypress', function(e) {
		if (e.keyCode == 13) {
			submit();
		}
	});

	$('#login').on('click', function() {
		submit();
	});
});
</script>

</head>

<body class="bg-dark login-screen">
	<div class="container-fluid">
		<div class="row" id="login-top-row">
			<div class="col-sm-6" id="login-logo-container">
				<img src="images/amur_logo.png">
			</div>
			<div class="col-sm-6" id="login-label-container">
				<label>Developed by CEVA Limited 2018.</label>
			</div>
		</div>
		<div class="login-body">
			<div id="login-form-container">
				<div id="login-form-title">
					<label>Sign In</label>
				</div>
				<form name="form1" id="form1" class="login-form" method="post" action="">
					<div class="form-group">
						<input class="form-control" required="true" name="userid" id="userid" type="text" placeholder="Enter Username">
					</div>
					<div class="form-group">
						<input class="form-control" required="true" name="password"  id="password" type="password" placeholder="Enter Password">
						<input type="hidden" name="encryptPassword" id="encryptPassword" value="" />
					</div>
					<div class="messages" id="messages"><s:actionmessage /></div>
					<div class="errors" id="errors"><s:actionerror /></div>   
				</form>
			</div>
			<a class="btn btn-primary btn-block" name="login" id="login" href="#">Login</a>
		</div>
	</div>
	
	<!-- Core plugin JavaScript-->
	<script src="vendor/jquery-easing/jquery.easing.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/authenticate.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/sha256.min.js"></script> 
	<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js"></script>
	
</body>

</html>
