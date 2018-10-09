
<!DOCTYPE html>
<html lang="en">
<%@taglib uri="/struts-tags" prefix="s"%> 
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
function createTerminal(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/terminalCreateConfirmAct.action';
	$("#form1").submit();
	return true;
}	

function getPreviousScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/terminalCreateBackAct.action';
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
				  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">Generate Terminal </a></li>
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
				<div class="row-fluid sortable"><!--/span-->
					<div class="box span12">  
							<div class="box-header well" data-original-title>
									<i class="icon-edit"></i>Terminal Information
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div> 
						<div class="box-content" id="terminalDetails"> 
						 <fieldset>  
								<table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable ">
								<tr class="even">
									<td width="20%"><strong><label for="Merchant ID">Merchant ID</label></strong></td>
									<td width="30%"> ${merchantID}
										<input name="merchantID" type="hidden" id="merchantID" class="field" value="${merchantID}">
									</td>
									<td width="20%"><strong><label for="Store ID">Store ID</label></strong></td>
									<td width="30%"> ${storeId}
										<input name="storeId"  type="hidden" id="storeId" class="field"  value="${storeId}" readonly  > 
									</td>
								</tr>
								<tr class="odd">
									<td><strong><label for="Terminal ID">Terminal ID</label></strong></td>
									<td> ${terminalID}
										<input name="terminalID" type="hidden"  id="terminalID" class="field" value="${terminalID}"  maxlength="8">
									</td>
									<td><strong><label for="Serial Number">Serial Number</label></strong></td>
									<td > ${serialNumber}
										<input name="serialNumber" id="serialNumber" class="field" value="${serialNumber}" type="hidden"  />
									</td>
										<input name="terminalUsage" type="hidden" class="field" id="terminalUsage" value="${terminalUsage}" />	
								</tr>
								<tr class="even">
									<td ><strong><label for="Terminal Make">Terminal Make </label></strong></td>
									<td > ${terminalMake}
										<input name="terminalMake" type="hidden" class="field" id="terminalMake" value="${terminalMake}"  />
									</td>
									<td ><strong><label for="Model Number ">Model Number</label></strong></td>
									<td > ${modelNumber}
										<input name="modelNumber"  type="hidden" id="modelNumber" class="field" value="${modelNumber}"  />
									</td>		
								</tr>
								
								
								 <tr  class="odd">
									<td><strong><label for="Valid From">Valid From (dd-mon-yyyy)</label></strong></td>
									<td > ${validFrom}
										<input name="validFrom" id="validFrom" class="field" value="${validFrom}" type="hidden"  />
									</td>
									<td ><strong><label for="Valid Thru">Valid Thru (dd-mon-yyyy)</label></strong></td>
									<td > ${validThru}
										<input name="validThru" type="hidden" class="field" id="validThru"   value="${validThru}" />
									</td>	
								</tr>
								<input name="pinEntry" type="hidden" class="field" id="pinEntry"  value="${pinEntry}"  />
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
						 <table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable " >
							<tr class="even">
								<td width="20%"><strong><label for="Status">TMK Index</label></strong></td>
								<td width="30%"> ${tpkIndex}
									<input name="tpkIndex"  type="hidden" id="tpkIndex" class="field"  value="${tpkIndex}" readonly  >
								</td>
								<td width="20%"><strong><label for="date">TMK Key</label></strong></td>
								<td width="30%"> ${tpkKey}
									<input name="tpkKey"  type="hidden" id="tpkKey" class="field"  value="${tpkKey}" readonly  >
								</td>
							</tr>
							<tr class="odd">
								<td ><strong><label for="Status">Status</label></strong></td>
								<td> ${status}
									<input name="status" type="hidden" id="status" class="field" value="${status}" >
								</td>
								<td><strong><label for="date">Date</label></strong></td>
								 <td > ${terminalDate}
									<input name="terminalDate"  type="hidden" id="terminalDate" class="field"  value="${terminalDate}"   >
								</td>
							</tr>
						</table>
						</fieldset>  
					</div>
				</div>
			</div>
			<div class="form-actions">
				<a  class="btn btn-danger" href="#" onClick="getPreviousScreen()">Back</a> &nbsp;&nbsp; 
				<a  class="btn btn-success" href="#" onClick="createTerminal()">Confirm</a>
			</div>	
		</div>
 </form>
</body>
</html>
