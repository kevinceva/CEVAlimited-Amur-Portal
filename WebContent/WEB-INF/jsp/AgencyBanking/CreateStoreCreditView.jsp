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
	var finalData="${responseJSON.StoreCreditAmount}";
	finalData=finalData.slice(1);
	var finalDatarows=finalData.split("#");
	
	if(finalData.length==0){
		$("#storeCreditDetails").hide();
		$("#storeCreditDetails1").hide();
	}else{
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
						"<td  >"+rowindex+"</td>"+
						"<td> "+referenceNo+"</td>"+	
						"<td> "+storeId+"  </td>"+ 
						"<td> "+creditAmt+" /= </td>"+ 
						"<td>"+status+" </td>"+ 
						"<td> "+requestedBy+" </td>"+ 
						"<td>  "+requestedDate+" </td>"+ 
						"<td>  "+approvedBy+" </td>"+ 
						"<td> "+approvedDate+" </td>"+ 
						"</tr>";
						$("#tbody_data").append(appendTxt);	  
					rowindex = ++rowindex;
					colindex = ++ colindex; 
				}
	}
	


var val = 1;
	var rowindex = 1;
	var colindex = 0;
	var finalData="${responseJSON.StoreTerminalCreditInfo}";
	finalData=finalData.slice(1);
	var finalDatarows=finalData.split("#");
	if(finalData.length==0){
		$("#terminalCreditDetails").hide();
		$("#terminalCreditDetails1").hide();
	}else{
		if(val % 2 == 0 ) {
		addclass = "even";
		val++;
		}
		else {
		addclass = "odd";
		val++;
		}  
		var rowCount = $('#Terminaltbody_data > tr').length;

		
			for(var i=0;i<finalDatarows.length;i++){
				var eachrow=finalDatarows[i];
				var eachfieldArr=eachrow.split(",");
				var referenceNo=eachfieldArr[0];
				var terminalId=eachfieldArr[1];
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
					"<td  >"+rowindex+"</td>"+
					"<td> "+referenceNo+"</td>"+	
					"<td>  "+terminalId+"  </td>"+ 
					"<td>  kshs "+creditAmt+" /= </td>"+ 
					"<td>"+status+" </td>"+ 
					"<td> "+requestedBy+" </td>"+ 
					"<td> "+requestedDate+" </td>"+ 
					"<td>  "+approvedBy+" </td>"+ 
					"<td> "+approvedDate+" </td>"+ 
					"</tr>";
					$("#Terminaltbody_data").append(appendTxt);	  
				rowindex = ++rowindex;
				colindex = ++ colindex; 
			}	
	}
		
});
 

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
						  <li><a href="#">View Store Credit </a></li>
						</ul>
				</div>
				<div class="row-fluid sortable"><!--/span-->
					<div class="box span12">
						  <div class="box-header well" data-original-title>Store Details
							<div class="box-icon"> 
								<a href="#" class="btn btn-minimize btn-round" data-toggle="tooltip"><i class="icon-chevron-up"></i></a>
								<a href="#" class="btn btn-close btn-round" data-toggle="tooltip"><i class="icon-remove"></i></a> 
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
								<a href="#" class="btn btn-minimize btn-round" data-toggle="tooltip"><i class="icon-chevron-up"></i></a>
								<a href="#" class="btn btn-close btn-round" data-toggle="tooltip"><i class="icon-remove"></i></a> 
							</div>
						</div>  
						<div id="storeCreditDetails1" class="box-content">
							<fieldset> 
								<div id="storeCreditDetails">
						   		 	<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
										id="bankData">
										  <thead>
												<tr >
													<th>Sno</th>
													<th>Reference No</th>
													<th>Store Id</th>
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
								</div>
							</fieldset> 
						</div>
					</div>
				</div>
				<div class="row-fluid sortable"><!--/span-->
					<div class="box span12">
						  <div class="box-header well" data-original-title>Terminal Credit Details
							<div class="box-icon"> 
								<a href="#" class="btn btn-minimize btn-round" data-toggle="tooltip"><i class="icon-chevron-up"></i></a>
								<a href="#" class="btn btn-close btn-round" data-toggle="tooltip"><i class="icon-remove"></i></a> 
							</div>
						</div>  
						<div id="terminalCreditDetails1" class="box-content">
							<fieldset> 
								<table width="100%" class="table table-striped table-bordered bootstrap-datatable " id="bankData" aria-describedby="DataTables_Table_0_info" style="width: 100%;">
								  <thead>
										<tr >
											<th width="1%"  style="width: 10px;">Sno</th>
											<th width="10%"  style="width: 20px;">Reference No</th>
											<th width="20%"  style="width: 50px;">Terminal Id</th>
											<th width="20%"  style="width: 90px;">Credit Amount</th>
											<th width="20%"  style="width: 20px;">Status</th>
											<th width="20%"  style="width: 70px;">Requested By</th>
											<th width="20%"  style="width: 90px;">Requested Date</th>
											<th width="20%"  style="width: 90px;">Approved By</th>
											<th width="20%"  style="width: 90px;">Approved Date</th>
										</tr>
								  </thead>    
								 <tbody  id="Terminaltbody_data">
								 </tbody>
							</table>
						</fieldset> 
					</div>
					</div>
				</div>
	<div class="form-actions">
		<a  class="btn btn-danger" href="#" onClick="getGenerateLimitScreen()">Next</a>
	</div>
</div> 
</form>
</body>
</html>
