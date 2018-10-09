
<!DOCTYPE html>

<%@taglib uri="/struts-tags" prefix="s"%> 
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<script type="text/javascript" >
 
function getGenerateLimitScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/creditManagementAct.action';
	$("#form1").submit();
	return true;
}

function insertStoreLimit(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/insertStoreCredit.action';
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
					  <li> <a href="#">Float Management</a> <span class="divider">&gt;&gt; </span></li>
					   <li> <a href="#">Credit Management</a> <span class="divider"> &gt;&gt;</span></li>
					  <li><a href="#">Create Store Credit </a></li>
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
						  <div class="box-header well" data-original-title>Store Details
							<div class="box-icon"> 
								<a href="#" class="btn btn-minimize btn-round" data-rel="tooltip"><i class="icon-chevron-up"></i></a>
								<a href="#" class="btn btn-close btn-round" data-rel="tooltip"><i class="icon-remove"></i></a> 
							</div>
						</div>  
					
						<div id="primaryDetails" class="box-content">
							<fieldset> 
								<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
									<tr class="even">
										<td width="20%"><strong><label for="Store Name">Store Name</label></strong></td>
										<td width="30%">  ${responseJSON.storeName}
											<input name="storeName" type="hidden" class="field" id="storeName" value="${responseJSON.storeName}" />
										</td>
										<td width="20%"><strong><label for="Store ID">Store ID</label></strong></td>
										<td width="30%"> ${responseJSON.storeId}
											<input name="storeId"  type="hidden" id="storeId" class="field"  value="${responseJSON.storeId}" > 
										</td>	
									</tr>
									<tr class="odd">
										<td ><strong><label for="Merchant Name">Merchant Name</label></strong></td>
										<td> ${responseJSON.merchantName}
											<input name="merchantName" type="hidden"  id="merchantName" class="field"  value="${responseJSON.merchantName}" >
										</td>
										<td ><strong><label for="Merchant ID">Merchant ID</label></strong></td>
										<td> ${responseJSON.merchantID}
											<input name="merchantID" type="hidden" id="merchantID" class="field" value="${responseJSON.merchantID}"  >
										</td>
									</tr>
									<tr class="even">
										<td ><strong><label for="Location">Location</label></strong></td>
										<td >
											${responseJSON.locationName}
											<input name="location" type="hidden" id="location" class="field" value="${responseJSON.location}"  >
										</td>
										<td ><strong><label for="KRA PIN">KRA PIN</label></strong></td>
										<td > ${responseJSON.KRA_PIN}
											<input name="kraPin" type="hidden" class="field" id="kraPin" value="${responseJSON.KRA_PIN}"  />
										</td>	
									</tr>    
								</table>
							</fieldset> 
							</div>
							</div>
							</div>
				 <div class="row-fluid sortable"><!--/span-->
				<div class="box span12">
					<div class="box-header well" data-original-title>Store Credit Details
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round" data-rel="tooltip"><i class="icon-chevron-up"></i></a>
							<a href="#" class="btn btn-close btn-round" data-rel="tooltip"><i class="icon-remove"></i></a> 
						</div>
					</div>  
					<div id="storeCreditDetails1" class="box-content">
						<fieldset>  
							<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
								<tr class="even">
									<td><strong><label for="Store Limit">Store Credit Amount</label></strong></td>
									<td>  kshs &nbsp; ${responseJSON.storeCreditAmt} /=
										<input name="storeCreditAmt" type="hidden" id="storeCreditAmt" class="field" value="${responseJSON.storeCreditAmt}" >
										</td>
									<td ></td>
									<td></td>
								</tr> 
							</table>
						</fieldset>  
					</div> 
                </div>
			</div>
		<div class="form-actions">
			<a  class="btn btn-success" href="#" onClick="insertStoreLimit()">Save</a>
		</div>
	</div> 
</form>
</body>
</html>
