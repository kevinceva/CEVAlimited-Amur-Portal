
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
	var colindex = 0;
	var bankfinalData="${accountMultiData}";
	var bankfinalDatarows=bankfinalData.split("#");
	if(val % 2 == 0 ) {
	addclass = "even";
	val++;
	}
	else {
	addclass = "odd";
	val++;
	}  
	var rowCount = $('#tbody_data > tr').length;

	
	for(var i=0;i<bankfinalDatarows.length;i++){
		var eachrow=bankfinalDatarows[i];
		var eachfieldArr=eachrow.split(",");
		var merchantId=eachfieldArr[1];
		var storeId=eachfieldArr[2];
		var terminalId=eachfieldArr[3];
		var amount=eachfieldArr[4];		
			var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
			"<td >"+rowindex+"</td>"+
			"<td>"+merchantId+"</td>"+	
			"<td> "+storeId+" </td>"+ 
			"<td> "+terminalId+" </td>"+ 
			"<td>"+amount+" </td>"+ 
			"</tr>";
			$("#tbody_data").append(appendTxt);	  
		rowindex = ++rowindex;
		colindex = ++ colindex; 
	}
		
}); 
 

function getServiceScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/serviceMgmtAct.action';
	$("#form1").submit();
	return true;
}
function createRefund(){ 
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/insertRefundAct.action';
	$("#form1").submit();
	return true;
} 

</script>
 
</head>

<body>
	<form name="form1" id="form1" method="post" action="">
		<!-- topbar ends -->
	
		
			<div id="content" class="span10">  
				<div>
					<ul class="breadcrumb">
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt;  </span> </li>
					  <li><a href="#">Refund Management Confirmation</a></li>
					</ul>
				</div>
				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
						 
						<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Create Refund Confirmation
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

							</div>
						</div> 
							<div id="primaryDetails" class="box-content">
								<input type="hidden" name="accountMultiData" id="accountMultiData" value="${accountMultiData}" ></input>							
								<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
									id="bankData" >
									  <thead>
											<tr >
												<th>Sno</th>
												<th>Merchant Id</th>
												<th>Store Id</th>
												<th>Terminal Id</th>
												<th>Recovery Amount</th>
											</tr>
									  </thead>    
									 <tbody  id="tbody_data">
									 </tbody>
								</table>  
							</div>
						</div>
				</div>  
		<div class="form-actions"> 
			<a  class="btn btn-success" href="#" onClick="createRefund()">Confirm</a>
		</div> 
	</div> 
</form>
</body>
</html>
