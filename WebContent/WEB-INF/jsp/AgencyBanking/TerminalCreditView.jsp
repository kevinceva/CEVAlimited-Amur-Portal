
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
	var finalData="${responseJSON.TerminalCreditInfo}";
	finalData=finalData.slice(1);
	var finalDatarows=finalData.split("#");
	if(val % 2 == 0 ) {
	addclass = "even";
	val++;
	} else {
	addclass = "odd";
	val++;
	}  
	var rowCount = $('#tbody_data > tr').length;

	
		for(var i=0;i<finalDatarows.length;i++){
			var eachrow=finalDatarows[i];
			var eachfieldArr=eachrow.split(",");
			var referenceNo=eachfieldArr[0];
			var storeId=eachfieldArr[1];
			var creditAmt=eachfieldArr[2];
			var status=eachfieldArr[3];
			var requestedBy=eachfieldArr[4];
			var requestedDate=eachfieldArr[5];
			var approvedBy=eachfieldArr[6];
			var approvedDate=eachfieldArr[7];
				if(approvedBy=="null" )
				approvedBy="";
				if(approvedDate=="null")
				approvedDate="";
				var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
									"<td >"+rowindex+"</td>"+
									"<td> "+referenceNo+"</td>"+	
									"<td> "+storeId+"  </td>"+ 
									"<td> kshs "+creditAmt+" /= </td>"+ 
									"<td>"+status+" </td>"+ 
									"<td> "+requestedBy+" </td>"+ 
									"<td> "+requestedDate+" </td>"+ 
									"<td>  "+approvedBy+" </td>"+ 
									"<td>  "+approvedDate+" </td>"+ 
								"</tr>";
				$("#tbody_data").append(appendTxt);	  
			rowindex = ++rowindex;
			colindex = ++ colindex; 
		}
		
});
 
function getGenerateLimitScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/creditManagementAct.action';
	$("#form1").submit();
	return true;
}


function createTerminal(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/insertTerminalCreateCreditAct.action';
	$("#form1").submit();
	return true;
}	
</script>

	 
</head>

<body> 
	<form name="form1" id="form1" method="post">
		<div id="content" class="span10">
            			 
		<div>
			<ul class="breadcrumb">
				<li><a href="#">Home</a> <span class="divider"> &gt;&gt; </span></li>
				<li><a href="#">Float Management</a> <span class="divider"> &gt;&gt; </span></li>
				<li><a href="#">Credit Management</a> <span class="divider"> &gt;&gt; </span></li>
				<li><a href="#">Terminal Credit View</a></li>
			</ul>
		</div>
		<div class="row-fluid sortable">
			<div class="box span12"> 
				<div class="box-header well" data-original-title>
					 <i class="icon-edit"></i>Terminal Information
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					</div>
				</div>  
				<div id="terminalDetails" class="box-content">
					<fieldset>
						 <table width="950" border="0" cellpadding="5" cellspacing="1"  
								class="table table-striped table-bordered bootstrap-datatable ">
								<tr class="even">
									<td width="20%"><strong><label for="Merchant ID">Merchant ID</label></strong></td>
									<td width="30%"> ${responseJSON.merchantID}
										<input name="merchantID" type="hidden" id="merchantID" class="field" value="${responseJSON.merchantID}">
									</td>
									<td width="20%"><strong><label for="Store ID">Store ID</label></strong></td>
									<td width="30%"> ${responseJSON.storeId}
										<input name="storeId"  type="hidden" id="storeId" class="field"  value="${responseJSON.storeId}"  >
									</td>
								</tr>
								<tr class="odd">
									<td><strong><label for="Terminal ID">Terminal ID</label></strong></td>
									<td> ${responseJSON.terminalID}
										<input name="terminalID" type="hidden"  id="terminalID" class="field" value="${responseJSON.terminalID}"  maxlength="8">
									</td>
									<td ><strong><label for="Terminal Make">Terminal Make </label></strong></td>
									<td > ${responseJSON.terminalMake}
										<input name="terminalMake" type="hidden" class="field" id="terminalMake" value="${responseJSON.terminalMake}"  />
									</td>
											
								</tr>
								<tr class="even">
									<td ><strong><label for="Model Number ">Model Number</label></strong></td>
									<td > ${responseJSON.modelNumber}
										<input name="modelNumber"  type="hidden" id="modelNumber" class="field" value="${responseJSON.modelNumber}"  />
									</td>	
									<td><strong><label for="Serial Number">Serial Number</label></strong></td>
									<td > ${responseJSON.serialNumber}
										<input name="serialNumber" id="serialNumber" class="field" value="${responseJSON.serialNumber}" type="hidden"  />
									</td>	
								</tr>
								<tr class="odd">
									<td ><strong><label for="Status">TPK Status</label></strong></td>
									<td> ${responseJSON.status}
										<input name="status" id="status" class="field" value="${responseJSON.status}" type="hidden"  />
										
									</td>
									<td><strong><label for="date">TPK Date</label></strong></td>
									 <td > ${responseJSON.TERMINAL_DATE}
										<input name="terminalDate"  type="hidden" id="terminalDate" class="field"  value="${responseJSON.TERMINAL_DATE}"   > 
									</td>
								</tr>
							</table>
						<fieldset>		 
					</div>			
					</div>			
					</div>

					<div class="row-fluid sortable">
						<div class="box span12"> 
							<div class="box-header well" data-original-title>
								 <i class="icon-edit"></i>Terminal Credit Amount Details
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
								</div>
							</div>  
							<div id="limitDetails" class="box-content">
								<fieldset> 
										<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
												id="bankData" >
											  <thead>
													<tr >
														<th>Sno</th>
														<th>Reference No</th>
														<th>Terminal Id</th>
														<th>Credit Amount</th>
														<th>Status</th>
														<th>Requested By</th>
														<th>Requested Date</th>
														<th>Approved By</th>
														<th>Approved Date</th>
													</tr>
											  </thead>    
											 <tbody  id="tbody_data">
											 </tbody>
										</table>  
								</fieldset> 
							</div> 
						</div>
					</div>
		 <div align="center">
				<a  class="btn btn-danger" href="#" onClick="getGenerateLimitScreen()">Next</a>
		</div>	
	 </div>	
 </form> 
</body>
</html>
