<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 
<script type="text/javascript" >
$(document).ready(function() {
	
	var val = 1;
	var rowindex = 1; 
	var bankfinalData="${bankMultiData}";
	var bankfinalDatarows=bankfinalData.split("#");
	if(val % 2 == 0 ) {
		addclass = "even";
		val++;
	}
	else {
		addclass = "odd";
		val++;
	} 
	
	for(var i=0;i<bankfinalDatarows.length;i++)
	{
		var eachrow=bankfinalDatarows[i];
		var eachfieldArr=eachrow.split(",");
		var service=eachfieldArr[0];
		var accountname=eachfieldArr[1];
		var openbalance=eachfieldArr[2];
		var closebalance = eachfieldArr[3];
		var accounttype = eachfieldArr[4]; 

		var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
			 "<td>"+rowindex+"</td>"+ 
			 "<td>"+service+"</td>"+
			 "<td>"+accountname+"</td>"+
			 "<td>"+openbalance+" </td>"+
 			 "<td >"+accounttype+"</td>"+
		 "</tr>";
		$("#tbody_data").append(appendTxt);  
		rowindex = ++rowindex;
	}

});
	
 
function paymentConfirm(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/insertBankDetials.action';
	$("#form1").submit();
	return true;
}

function next(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/bankact.action';
	$("#form1").submit();
	return true;
}
 </script>

<style type="text/css">

.hide
.hide2
{
	display:none;
}

</style>  
</head>
<body>
<form name="form1" id="form1" method="post" action="">
	<div id="content" class="span10">  
	    <div>
			<ul class="breadcrumb">
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
 			  <li><a href="#">Bank Account Creation Acknowledgement</a></li>
			</ul>
		</div>
		 <div class="row-fluid sortable"> 
			<div class="box span12">
				<div class="box-header well" data-original-title>
					 <i class="icon-edit"></i>Bank Account Acknowledgement
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					</div>
				</div> 		
				<div class="box-content">   
					<strong>Bank Account Details Inserted Successfully. </strong>
				</div>
			</div>  
		</div>
		<div class="form-actions">
			<input type="button" class="btn btn-primary" name="save" id="save" value="Next" width="100" onClick="next()" />  
		</div> 
	</div>
</form>
</body>
</html>
