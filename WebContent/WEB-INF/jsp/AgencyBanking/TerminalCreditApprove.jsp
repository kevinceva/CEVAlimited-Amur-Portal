
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
	var mydata ='${responseJSON.ReferenceNoList}';
    	 
    	var json = jQuery.parseJSON(mydata);
    	 
    	$.each(json, function(i, v) {
    	    // create option with value as index - makes it easier to access on change
    	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
    	    // append the options to job selectbox
    	    $('#storeCreditRefNo').append(options);
    	});
});
 
function getGenerateLimitScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/creditManagementAct.action';
	$("#form1").submit();
	return true;
}


function createTerminal(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/terminalCreditApproveConfirmAct.action';
	$("#form1").submit();
	return true;
}	
</script>
 
</head>

<body>
<form name="form1" id="form1" method="post">
	 <div id="content" class="span10">
            			<!-- content starts -->
		<div>
				<ul class="breadcrumb">
					<li><a href="#">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Float Management</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Credit Management</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Authorization</a></li>
				</ul>
		</div>
		<table height="3">
			 <tr>
				<td colspan="3">
					<div class="messages" id="messages"><s:actionmessage /></div>
					<div class="errors" id="errors"><s:actionerror /></div>
				</td>
			</tr>
		 </table>
				 
 			<div class="row-fluid sortable"> 
				<div class="box span12">	 
						<div class="box-header well" data-original-title >
								<i class="icon-edit"></i>Terminal Information
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i
									class="icon-cog"></i></a> <a href="#"
									class="btn btn-minimize btn-round"><i
									class="icon-chevron-up"></i></a>
			
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
						</fieldset>
					</div>			
					</div>			
					</div>	
					<div class="row-fluid sortable"> 
						<div class="box span12">	 
								<div class="box-header well" data-original-title >
										<i class="icon-edit"></i>Credit Details
									<div class="box-icon">
										<a href="#" class="btn btn-setting btn-round"><i
											class="icon-cog"></i></a> <a href="#"
											class="btn btn-minimize btn-round"><i
											class="icon-chevron-up"></i></a> 
									</div>
								</div>  
					 
							<div id="limitDetails" class="box-content"> 
								<fieldset>
									<table width="950" border="0" cellpadding="5" cellspacing="1" 
										class="table table-striped table-bordered bootstrap-datatable " >
											<tr class="even">
												<td width="20%">Store Credit Reference No</td>
												<td width="30%"> ${responseJSON.referenceNo}
													<input name="storeCreditRefNo" type="hidden" id="storeCreditRefNo" class="field" value="${responseJSON.referenceNo}" >
												</td>
												<td width="20%"><strong><label for="Store Limit">Terminal Credit Amount</label></strong></td>
												<td width="30%"> ${responseJSON.terminalCreditAmt}
													<input name="terminalCreditAmt" type="hidden" id="terminalCreditAmt" class="field" value="${responseJSON.terminalCreditAmt}" >
												</td>
											</tr>
											<tr class="odd">
														<td><strong><label for="Store Limit">Requested By</label></strong></td>
														<td> 
															  ${responseJSON.terminalRequestedBy} 
															<input name="requestedBy" type="hidden" id="requestedBy" class="field" value="${responseJSON.terminalRequestedBy}" >
														</td>
														<td><strong><label for="Store Limit">Requested Date</label></strong></td>
														<td> 
															  ${responseJSON.terminalRequestedDate} 
															<input name="requestedDate" type="hidden" id="requestedDate" class="field" value="${responseJSON.terminalRequestedDate}" >
															
														</td>
													</tr>
											<tr class="even">
												<td >Terminal Credit Reference No</td>
												<td> ${responseJSON.terminalReferenceNo}
													<input name="terminalReferenceNo" type="hidden" id="terminalReferenceNo" class="field" value="${responseJSON.terminalReferenceNo}" >
												</td>
												<td><strong><label for="Store Limit">Approve/Reject <font color="red"> * </font></label></strong></td>
												<td> 
													<select id="approveReject" name="approveReject" class="field">
														<option value="">Select</option>
														<option value="Approved">Approve</option>
														<option value="Rejected">Reject</option>
													</select>
												</td>
											</tr>
									</table>
								</fieldset>
							</div>  
						</div>
					</div>
			<div class="form-actions">
				<a  class="btn btn-danger" href="#" onClick="getGenerateLimitScreen()">Back</a> &nbsp;&nbsp;
				<a  class="btn btn-danger" href="#" onClick="createTerminal()">Submit</a>
			</div>	
	</div> 
</form>	
</body>
</html>
