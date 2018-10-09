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
 

 function submit(){	
	 	document.getElementById("val-err").style.display="none";
	 	document.getElementById("mob-err").style.display="none";
	 	document.getElementById("email-err").style.display="none";
	 	var mob=document.getElementById("mob").value;
	 	var email=document.getElementById("email").value;
	 	console.log("mob :"+mob+"--"+email);
	 	
	 	if (mob == null || mob == "")
	 	{
	 		document.getElementById("actn-msg").style.display="none";
	 		document.getElementById("mob-err").style.display="block";
	 		return false;
	 	}else
	 	{
		 	document.getElementById("actn-msg").style.display="block";
		 	
	 	}	
	 	
	 	if (email == null || email == "")
	 	{
	 		document.getElementById("actn-msg").style.display="none";
	 		document.getElementById("email-err").style.display="block";
	 		
	 		return false;
	 	}else
	 	{
		 	document.getElementById("actn-msg").style.display="block";
		 	
	 	}	
	 	
		if($("#form1").valid()){
				$("#form1")[0].action='<%=request.getContextPath()%>/cicagentregsave.action';
				$("#form1").submit();
				return true;
			} else {
				alert("Mobile Number and E-mail ID are mandatory.");
				return false;
			}
	}
 
 function validateAgent(){
	 

	 	document.getElementById("actn-msg").style.display="none";
	 	var mob=document.getElementById("mob").value;
	 	console.log("mob :"+mob);
	 	if (mob == null || mob == "")
	 	{
	 		document.getElementById("mob-err").style.display="block";
	 		return false;
	 	}else		 	
		{
		 	document.getElementById("mob-err").style.display="none";
		 	document.getElementById("email-err").style.display="none";
		 	document.getElementById("val-err").style.display="none";
		 	var formInput = $('#form1').serialize();
			console.log("value of formInput :"+formInput);
			var agnurl ='<%=request.getContextPath()%>/validateAgent.action';
		    $.ajax
		    ({
		    type: "POST",
		    url: agnurl,
		    data:formInput,
		    cache: false,
		    success: function(html)
		    {
				console.log("html:"+html+"--"+html.responseJSON+"--"+JSON.stringify(html)+"--"+JSON.stringify(html.responseJSON));
		    	var exst_cnt = html.responseJSON.exst_cnt;
		    	console.log("value of exst_cnt:"+exst_cnt);
		    	if (exst_cnt > 0)
		    	{
		    		document.getElementById("val-err").style.display="block";
		    	}	
		    	
		    }, 
		     error: function (jqXHR, textStatus, errorThrown)
		      {
		      alert("Error moving to next");
		      }
		    });
		}
	}

$(document).ready(function() {


	
	
	$('#save').on('click', function() {
		submit();
	});
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
					<div> <font color="red" size="3" ><b>Champion Registration</b></font> &nbsp;&nbsp;&nbsp; <a href="<%=ctxstr%>/jsp/CICPDF.pdf">Brochure</a> </div>	
				</section>

				<label style="display:none;" id="val-err">Champion Already Registered </label>	
				<label style="display:none;" id="mob-err">Please Enter Mobile Number </label>	
				<label style="display:none;" id="email-err">Please Enter E-mail ID </label>	
				<label  id="actn-msg" > ${ackmsg} </label> 
				
				<p>
					<input type="text" name="mob" id="mob" autocomplete="off"
						required="true" value="" placeholder="Enter Mobile Number" onblur="validateAgent()">
				</p>
				<p>
					<input type="text" name="refmob" id="refmob"
						autocomplete="off" required="true" placeholder="Enter reference Name">
				</p>
				<p>
					<input type="text" name="comp" id="comp"
						autocomplete="off" required="true" placeholder="Company (If Student - University)">
				</p>
				<p>
					<input type="text" name="email" id="email"
						autocomplete="off" required="true" placeholder="E-mail ID">
				</p>
				<p class="submit">
					<input type="button" name="save" id="save" value="Save">
				</p>
				<input type="hidden" name="encryptPassword" id="encryptPassword" value="" />
			</div>
			
		</section> 
<script src="${pageContext.request.contextPath}/js/authenticate.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sha256.min.js"></script> 
<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js"></script>
	</form>
</body>
</html>
