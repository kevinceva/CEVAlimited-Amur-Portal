
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
	var finalData="${responseJSON.AllTerminalLimitInfo}";
	finalData=finalData.slice(1);
	//alert(finalData);
	if(finalData.length==0){
		$("#terminalLimitDetails").hide();
	}else{
	
		var finalDatarows=finalData.split("#");
		if(val % 2 == 0 ) {
		addclass = "even";
		val++;
		}
		else {
		addclass = "odd";
		val++;
		}  
		var rowCount = $('#tbody_data > tr').length;

		
			for(var i=0;i<finalDatarows.length;i++){
				var eachrow=finalDatarows[i];
				var eachfieldArr=eachrow.split(",");
				var terminalId=eachfieldArr[0];
				var limit=eachfieldArr[1];
				var limitStatus=eachfieldArr[2];
				var requestedBy=eachfieldArr[3];
				var requestedDate=eachfieldArr[4];
				var approvedBy=eachfieldArr[5];
				var approvedDate=eachfieldArr[6];
				//alert(approvedDate.length);
				if(approvedBy=="null" )
					approvedBy="";
				if(approvedDate=="null")
					approvedDate="";
					var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
										"<td >"+rowindex+"</td>"+
										"<td> "+terminalId+"</td>"+	
										"<td> kshs "+limit+" /= </td>"+ 
										"<td> "+limitStatus+" </td>"+ 
										"<td>  "+requestedBy+" </td>"+ 
										"<td>  "+requestedDate+" </td>"+ 
										"<td>  "+approvedBy+" </td>"+ 
										"<td>  "+approvedDate+" </td>"+ 
									"</tr>";
					$("#tbody_data").append(appendTxt);	  
				rowindex = ++rowindex;
				colindex = ++ colindex; 
			}
			
	}
});
 
function getGenerateLimitApproveScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/limitAuthManagentAct.action';
	$("#form1").submit();
	return true;
}
function LimitApprove(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/TerminallimitApproveConfirmAct.action';
	$("#form1").submit();
	return true;
}	
</script>

	 
</head>

<body>
	<form name="form1" id="form1">
		<div id="content" class="span10">
			<div>
				<ul class="breadcrumb">
				  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">Terminal Limit View</a></li>
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
					<div class="box-content"  id="terminalDetails"> 
						<fieldset>
							 <table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable ">
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
						</fieldset>	 
					</div>		
				</div>		
			</div>		

			<div class="row-fluid sortable"> 
				<div class="box span12"> 
					<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Store Limit Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

						</div>
					</div>  
					<div class="box-content" id="storelimitDetails">
						<fieldset>  
							<table width="950" border="0" cellpadding="5" cellspacing="1" 
								class="table table-striped table-bordered bootstrap-datatable " >
									<tr class="even">
										<td width="20%"><strong><label for="Store Limit">Store Limit</label></strong></td>
										<td width="30%"> 
											 kshs ${responseJSON.storeLimit} /=
											<input name="storeLimit" type="hidden" id="storeLimit" class="field" value="${responseJSON.storeLimit}" >
										</td>
										<td width="20%"><strong><label for="Store Limit">Store Limit Status</label></strong></td>
										<td width="30%"> 
											  ${responseJSON.storeLimitStatus} 
											<input name="storeLimitStatus" type="hidden" id="storeLimitStatus" class="field" value="${responseJSON.storeLimitStatus}" >
										</td>
									</tr> 
									<tr class="odd">
										<td><strong><label for="Store Limit">Requested By</label></strong></td>
										<td> 
											  ${responseJSON.requestedBy} 
											<input name="requestedBy" type="hidden" id="requestedBy" class="field" value="${responseJSON.requestedBy}" >
										</td>
										<td><strong><label for="Store Limit">Requested Date</label></strong></td>
										<td> 
											  ${responseJSON.requestedDate} 
											<input name="requestedDate" type="hidden" id="requestedDate" class="field" value="${responseJSON.requestedDate}" >
											
										</td>
									</tr>
									<tr class="even">
										<td><strong><label for="Store Limit">Approved By</label></strong></td>
										<td> 
											  ${responseJSON.approvedBy} 
											<input name="approvedBy" type="hidden" id="approvedBy" class="field" value="${responseJSON.approvedBy}" >
										</td>
										<td><strong><label for="Store Limit">Approved Date</label></strong></td>
										<td> 
											  ${responseJSON.approvedDate} 
											<input name="approvedDate" type="hidden" id="approvedDate" class="field" value="${responseJSON.approvedDate}" >
										
										</td>
									</tr> 
							</table>
						</fieldset>
					</div>
					<div id="terminalLimitDetails">
						<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
								id="bankData" >
							  <thead>
									<tr >
										<th>Sno</th>
										<th>Terminal ID</th>
										<th>Limit</th>
										<th>Limit Status</th>
										<th>Requested By</th>
										<th>Requested Date</th>
										<th>Approved By</th>
										<th>Approved Date</th>
									</tr>
							  </thead>    
							 <tbody  id="tbody_data">
							 </tbody>
						</table> 
					</div>
				</div>
			</div>
			<div class="row-fluid sortable"> 
				<div class="box span12"> 
					<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Terminal Limit Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

						</div>
					</div>  
					<div class="box-content" id="limitDetails">
						<fieldset>  
							<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable " >
									<tr class="even">
										<td width="20%"><strong><label for="Store Limit">Terminal Limit</label></strong></td>
										<td width="30%">
											${responseJSON.terminalLimit}
										</td>
										<td width="20%"><strong><label for="Store Limit">Terminal Limit Status</label></strong></td>
										<td width="30%">
											${responseJSON.terminalLimitStatus}
										</td>
									</tr>
									<tr class="odd">
										<td><strong><label for="Store Limit">Requested By</label></strong></td>
										<td> 
											  ${responseJSON.terminalRequestedBy} 
											<input name="terminalRequestedBy" type="hidden" id="terminalRequestedBy" class="field" value="${responseJSON.terminalRequestedBy}" >
										</td>
										<td><strong><label for="Store Limit">Requested Date</label></strong></td>
										<td> 
											  ${responseJSON.terminalRequestedDate} 
											<input name="terminalRequestedDate" type="hidden" id="terminalRequestedDate" class="field" value="${responseJSON.terminalRequestedDate}" >
											
										</td>
									</tr>
									<tr class="even">
										<td><strong><label for="Store Limit">Approved By</label></strong></td>
										<td> 
											  ${responseJSON.terminalApprovedBy} 
											<input name="terminalApprovedBy" type="hidden" id="terminalApprovedBy" class="field" value="${responseJSON.terminalApprovedBy}" >
										</td>
										<td><strong><label for="Store Limit">Approved Date</label></strong></td>
										<td> 
											  ${responseJSON.termianlApprovedDate} 
											<input name="termianlApprovedDate" type="hidden" id="termianlApprovedDate" class="field" value="${responseJSON.termianlApprovedDate}" >
										
										</td>
									</tr>
							</table>
						</fieldset>
					</div>
				</div>
			</div>
			<div class="form-actions">
					<a  class="btn btn-danger" href="#" onClick="getGenerateLimitApproveScreen()">Next</a> &nbsp;&nbsp;
			</div>	 
	</div>
 </form>
             
</body>
</html>
