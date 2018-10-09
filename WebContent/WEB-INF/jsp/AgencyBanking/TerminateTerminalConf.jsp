
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
 
 
function getGenerateMerchantScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/generateMerchantAct.action';
	$("#form1").submit();
	return true;
}

function terminateTerminal(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/terminateTerminalAct.action';
	$("#form1").submit();
	return true;
}

</script>
 
</head>

<body>
		
	<form name="form1" id="form1" method="post" action="">
		
	 <div id="content" class="span10"> 
	 
		<div>
				<ul class="breadcrumb">
				  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">Terminate Terminal</a></li>
				</ul>
		</div>
				
				<div class="row-fluid sortable"><!--/span--> 
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
											<input name="merchantID" type="hidden" id="merchantID" class="field" value=" ${responseJSON.merchantID}">
										</td>
										<td width="20%"><strong><label for="Store ID">Store ID</label></strong></td>
										<td width="30%"> ${responseJSON.storeId}
											<input name="storeId"  type="hidden" id="storeId" class="field"  value=" ${responseJSON.storeId}" readonly  > 
										</td>
									</tr>
									<tr class="odd">
										<td><strong><label for="Terminal ID">Terminal ID</label></strong></td>
										<td> ${responseJSON.terminalID}
											<input name="terminalID" type="hidden"  id="terminalID" class="field" value="${responseJSON.terminalID}"  maxlength="8">
										</td>
										<td><strong><label for="Terminal Usage">Terminal Usage</label></strong></td>
										<td > ${responseJSON.terminalUsage}
										</td>
												
									</tr>
									<tr class="even">
										<td ><strong><label for="Terminal Make">Terminal Make </label></strong></td>
										<td > ${responseJSON.terminalMake}
										</td>
										<td ><strong><label for="Model Number ">Model Number</label></strong></td>
										<td > ${responseJSON.modelNumber}
										</td>		
									</tr>
											
									 <tr  class="odd">
										<td><strong><label for="Serial Number">Serial Number</label></strong></td>
										<td > ${responseJSON.serialNumber}
										</td>
										<td ><strong><label for="PIN Entry">PIN Entry</label></strong></td>
										<td > ${responseJSON.pINEntry}
										</td>
									</tr>
									
									 <tr  class="even">
										<td><strong><label for="Valid From">Valid From (dd-mon-yyyy)</label></strong></td>
										<td > ${responseJSON.validFrom}
										</td>
										<td ><strong><label for="Valid Thru">Valid Thru (dd-mon-yyyy)</label></strong></td>
										<td > ${responseJSON.validThru}
										</td>	
									</tr>
									
								</table> 
							</fieldset>
					</div>
					</div>
					</div>
				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
					<div class="box-header well" data-original-title>
						<i class="icon-edit"></i>TMK Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

						</div>
					</div> 	
					<div id="feeDetails" class="box-content">
						<fieldset>
							 <table width="950" border="0" cellpadding="5" cellspacing="1"  
									class="table table-striped table-bordered bootstrap-datatable " >
								<tr class="even">
									<td width="20%"><strong><label for="Status">Status</label></strong></td>
									<td colspan="3"> ${responseJSON.status}
									</td>
								</tr>
								<tr class="odd">
									<td><strong><label for="date">Date</label></strong></td>
									 <td > ${responseJSON.TERMINAL_DATE}
									</td>
								</tr>
							</table>
						</fieldset>
					</div>
					</div>
                </div>
	 
				<div class="form-actions">
						 <a  class="btn btn-danger" href="#" onClick="getGenerateMerchantScreen()">Back</a> &nbsp;&nbsp;
						<a  class="btn btn-success" href="#" onClick="terminateTerminal()">Confirm</a>
				</div> 
	</div>
 </form>	 
</body>
</html>
