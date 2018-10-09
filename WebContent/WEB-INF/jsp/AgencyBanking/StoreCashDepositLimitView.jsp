
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

function getGenerateLimitScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/cashDepositLimit.action';
	$("#form1").submit();
	return true;
}
function updateStoreLimit(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/updateStoreLimitAct.action';
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
					  <li><a href="#">Create Store Cash Deposit Limit </a></li>
					</ul>
				</div>
				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
							<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>Store Details
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
								</div>
							</div> 
							<div id="primaryDetails"  class="box-content">
								<fieldset>
									<table width="950" border="0" cellpadding="5" cellspacing="1" 
										class="table table-striped table-bordered bootstrap-datatable " >
										<tr class="even">
											<td width="20%"><strong><label for="Store Name">Store Name</label></strong></td>
											<td width="30%">  ${responseJSON.storeName}
												<input name="storeName" type="hidden" class="field" id="storeName" value="${responseJSON.storeName}" />
											</td>
											<td width="20%"><strong><label for="Store ID">Store ID</label></strong></td>
											<td width="30%"> ${responseJSON.storeId}
												<input name="storeId"  type="hidden" id="storeId" class="field"  value="${storeId}" > 
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
												<input name="location" type="hidden" id="location" class="field" value="${responseJSON.locationName}"  >
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
					<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Store Cash Deposit Limit Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
						</div>
					</div>
						<div id="limitDetails" class="box-content">
							<fieldset>
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable " >
										<tr class="even">
											<td width="20%"><strong><label for="Store Limit">Store Cash Deposit Limit</label></strong></td>
											<td width="30%"> 
												 kshs ${responseJSON.storeLimit} /=
												<input name="storeLimit" type="hidden" id="storeLimit" class="field" value="${responseJSON.storeLimit}" >
											</td>
											<td width="20%"><strong><label for="Store Limit">Store Cash Deposit Limit Status</label></strong></td>
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
					</div>
				</div> 	 
			<div class="form-actions">
				<a  class="btn btn-danger" href="#" onClick="getGenerateLimitScreen()">Next</a> &nbsp;&nbsp; 
			</div> 
	</div> 
</form>
</body>
</html>
