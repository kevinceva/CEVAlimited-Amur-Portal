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
<script src='https://www.google.com/recaptcha/api.js'></script>
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

* {
  .border-radius(0) !important;
}

#field {
    margin-bottom:20px;
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
	 	
	 	var mob=document.getElementById("mob").value;
	 	
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
		    	var cln_cnt = html.responseJSON.cln_cnt;
		    	console.log("value of exst_cnt:"+exst_cnt);
		    	/* if (exst_cnt > 0)
		    	{
		    		document.getElementById("val-err").style.display="block";
		    	} */
		    	if (cln_cnt == 0)
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

    var next = 1;
    $(".add-more").click(function(e){
        e.preventDefault();
        var addto = "#field" + next;
        var addRemove = "#field" + (next);
        next = next + 1;
        var newIn = '<input autocomplete="off" class="input form-control" id="field' + next + '" name="field' + next + '" type="text">';
        var newInput = $(newIn);
        var removeBtn = '<button id="remove' + (next - 1) + '" class="btn btn-danger remove-me" >-</button></div><div id="field">';
        var removeButton = $(removeBtn);
        $(addto).after(newInput);
        $(addRemove).after(removeButton);
        $("#field" + next).attr('data-source',$(addto).attr('data-source'));
        $("#count").val(next);  
        
            $('.remove-me').click(function(e){
                e.preventDefault();
                var fieldNum = this.id.charAt(this.id.length-1);
                var fieldID = "#field" + fieldNum;
                $(this).remove();
                $(fieldID).remove();
            });
    });
	
	
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
					<div> <font color="red" size="3" ><b>Champion Client Mapping</b></font> &nbsp;&nbsp;&nbsp; <a href="<%=ctxstr%>/jsp/CICPDF.pdf">Brochure</a> </div>	
				</section>

				<label style="display:none;" id="val-err">Champions Must Be The Active Amur Clients. </label>	
				<label  id="actn-msg" > ${ackmsg} </label> 
				
				<p>
					<input type="text" name="mob" id="mob" autocomplete="off"
						required="true" value="" placeholder="Enter Agent Mobile Number" onblur="validateAgent()">
				</p>
				

						<input type="hidden" name="count" value="1" />
						<p>
				                    <div id="field">
				                    <input autocomplete="off" class="input" id="field1" name="prof1" type="text" placeholder="Type something" data-items="8"/> 
				                    <button id="b1" class="btn add-more" type="button">+</button></div>
				                   </p>
				                   
				<p class="submit">
					<input type="button" name="save" id="save" value="Save">
				</p> 				                   
				           
				          <!--  <button id="b1" class="btn add-more" type="button">+</button> -->
				           
					</div>			
				<!-- <p>
					<input type="text" name="refmob" id="refmob"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 1">
				</p>
				<p>
					<input type="text" name="refmob2" id="refmob2"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 2">
				</p>
				<p>
					<input type="text" name="refmob3" id="refmob3"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 3">
				</p>
				<p>
					<input type="text" name="refmob4" id="refmob4"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 4">
				</p>
				<p>
					<input type="text" name="refmob5" id="refmob5"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 5">
				</p>				<p>
					<input type="text" name="refmob6" id="refmob6"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 6">
				</p>
				<p>
					<input type="text" name="refmob7" id="refmob7"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 7">
				</p>
				
				<p>
					<input type="text" name="refmob8" id="refmob8"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 8">
				</p>
				<p>
					<input type="text" name="refmob9" id="refmob9"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 9">
				</p>
				<p>
					<input type="text" name="refmob10" id="refmob10"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 10">
				</p>
				<p>
					<input type="text" name="refmob11" id="refmob11"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 11">
				</p>
				<p>
					<input type="text" name="refmob12" id="refmob12"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 12">
				</p>
				<p>
					<input type="text" name="refmob13" id="refmob13"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 13">
				</p>
				<p>
					<input type="text" name="refmob14" id="refmob14"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 14">
				</p>
				<p>
					<input type="text" name="refmob15" id="refmob15"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 15">
				</p>
				<p>
					<input type="text" name="refmob16" id="refmob16"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 16">
				</p>
				<p>
					<input type="text" name="refmob17" id="refmob17"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 17">
				</p>
				<p>
					<input type="text" name="refmob18" id="refmob18"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 18">
				</p>
				<p>
					<input type="text" name="refmob19" id="refmob19"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 19">
				</p>
				<p>
					<input type="text" name="refmob20" id="refmob20"
						autocomplete="off" required="true" placeholder="Enter reference Mobile No 20">
				</p>
				
				<div class="g-recaptcha" data-sitekey="6LdyzCYUAAAAAGUcyxTueuFkEtCTonhNlNGy41NA"></div>																																																				
												
				<p class="submit">
					<input type="button" name="save" id="save" value="Save">
				</p> 
			</div>-->
			
		</section> 

<script src="${pageContext.request.contextPath}/js/authenticate.min.js"></script>
<script src="${pageContext.request.contextPath}/js/sha256.min.js"></script> 
<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js"></script>
	</form>
</body>
</html>
