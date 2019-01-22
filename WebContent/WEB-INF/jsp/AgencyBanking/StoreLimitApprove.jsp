
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
	var finalData="${responseJSON.TerminalInfo}";
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
			 
				if(approvedBy=="null" )
					approvedBy="";
					
				if(approvedDate=="null")
					approvedDate="";
				
				var user_status="";
			 
				if(limitStatus == 'Approved') {
					user_status = "<a href='#' class='label label-success' index='"+rowindex+"'>"+limitStatus+"</a>";
 				} else  {
					user_status = "<a href='#'  class='label label-warning'  index='"+rowindex+"'>"+limitStatus+"</a>";
 				} 
				
				var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
					"<td>"+rowindex+"</td>"+
					"<td>"+terminalId+"</td>"+	
					"<td> kshs "+limit+" /= </td>"+ 
					"<td> "+user_status+" </td>"+ 
					"<td>"+requestedBy+" </td>"+ 
					"<td>"+requestedDate+" </td>"+ 
					"<td>"+approvedBy+" </td>"+ 
					"<td>"+approvedDate+" </td>"+ 
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
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/limitApproveSubmitAct.action';
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
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Float Management</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Limit Management</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Authorization</a></li>
				</ul>
			</div> 
								  
			<div class="row-fluid sortable"><!--/span--> 
				<div class="box span12">
					  <div class="box-header well" data-original-title>Store Information
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round" data-toggle="tooltip"><i class="icon-chevron-up"></i></a>
							<a href="#" class="btn btn-close btn-round" data-toggle="tooltip"><i class="icon-remove"></i></a> 
						</div>
					</div>
                      
					<div class="box-content" id="primaryDetails">
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
										<td > ${responseJSON.merchantName}
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
						  <div class="box-header well" data-original-title>Store Limit Details
							<div class="box-icon"> 
								<a href="#" class="btn btn-minimize btn-round" data-toggle="tooltip"><i class="icon-chevron-up"></i></a>
								<a href="#" class="btn btn-close btn-round" data-toggle="tooltip"><i class="icon-remove"></i></a> 
							</div>
						</div>
                      
						<div class="box-content"  id="limitDetails">
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
										
										<tr class="odd">
											<td><strong><label for="Store Limit">Approve/Reject <font color="red"> * </font></label></strong></td>
											<td> 
												<select id="approveReject" name="approveReject">
													<option value="">Select</option>
													<option value="Approved">Approve</option>
													<option value="Rejected">Reject</option>
												</select>
											</td>
											<td></td>
											<td></td>
										</tr> 
								</table>
							</fieldset>
							</div>
						</div>
					</div>  
		<div class="form-actions"> 
			<a  class="btn btn-danger" href="#" onClick="getGenerateLimitApproveScreen()">Back</a> &nbsp;&nbsp; 
			<a  class="btn btn-success" href="#" onClick="LimitApprove()">Submit</a> &nbsp;&nbsp; 
		</div> 
	</div>  	 
</form>
</body>
</html>
