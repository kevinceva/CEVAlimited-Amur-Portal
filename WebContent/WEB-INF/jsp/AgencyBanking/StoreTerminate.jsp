
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
	var bankacctfinalData="${responseJSON.bankAcctMultiData}";
	bankacctfinalData=bankacctfinalData.slice(1);
	var bankacctfinalDatarows=bankacctfinalData.split("#");
	if(val % 2 == 0 ) {
	addclass = "even";
	val++;
	}
	else {
	addclass = "odd";
	val++;
	}  
	var rowCount = $('#tbody_data > tr').length;

		for(var i=0;i<bankacctfinalDatarows.length;i++){
			var eachrow=bankacctfinalDatarows[i];
			var eachfieldArr=eachrow.split(",");
			var accountNumber=eachfieldArr[0];
			var acctDescription=eachfieldArr[1];
			var bankName = eachfieldArr[2];
			var bankBranch = eachfieldArr[3];
			var transferCode = eachfieldArr[4];
			
				var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
				"<td>"+rowindex+"</td>"+
				"<td>"+accountNumber+"</td>"+	
				"<td>"+acctDescription+" </td>"+ 
				"<td>"+bankName+"</td>"+
				"<td>"+bankBranch+"</td>"+
				"<td>"+transferCode+"</td>"+
				"</tr>";
				
				$("#tbody_data1").append(appendTxt);	  
				rowindex = ++rowindex;
				colindex = ++ colindex; 
		}
		
		
	var val = 1;
	var rowindex = 1;
	var colindex = 0;
	var documentfinalData="${responseJSON.documentMultiData}";
	documentfinalData=documentfinalData.slice(1);
	if(documentfinalData.length==0){
	}else{
		var documentfinalDatarows=documentfinalData.split("#");
	if(val % 2 == 0 ) {
	addclass = "even";
	val++;
	}
	else {
		addclass = "odd";
		val++;
		}  
		var rowCount = $('#tbody_data > tr').length;

		
			for(var i=0;i<documentfinalDatarows.length;i++){
				var eachrow=documentfinalDatarows[i];
				var eachfieldArr=eachrow.split(",");
				var documentId=eachfieldArr[0];
				var documentDescription=eachfieldArr[1];
				var gracePeriod = eachfieldArr[2];
				var mandatory = eachfieldArr[3];
				
					var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
					"<td>"+rowindex+"</td>"+
					"<td> "+documentId+"</td>"+	
					"<td> "+documentDescription+" </td>"+ 
					"<td> "+gracePeriod+"</td>"+
					"<td>"+mandatory+"</td>"+
					"</tr>";
					
					$("#tbody_data2").append(appendTxt);	  
					rowindex = ++rowindex;
					colindex = ++ colindex; 
			}
	}
			
			
	var val = 1;
	var rowindex = 1;
	var colindex = 0;
	var agentMultiData="${responseJSON.AgenctAcctMultiData}";
	var agentMultiDatarows=agentMultiData.split("#");
	agentMultiDatarows=agentMultiDatarows.slice(1);
	if(agentMultiData.length==0){
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

			
				for(var i=0;i<agentMultiDatarows.length;i++){
					var eachrow=agentMultiDatarows[i];
					var eachfieldArr=eachrow.split(",");
					var bankAgenctNo=eachfieldArr[0];
					var MPeasAgenctNo=eachfieldArr[1];
					var airtelMoneyAgenetNo = eachfieldArr[2];
					var orangeMoneyAgentNo = eachfieldArr[3];
					var mpesaTillNumber = eachfieldArr[4];
						var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
						"<td  "+rowindex+"</td>"+
						"<td>"+bankAgenctNo+"</td>"+	
						"<td>"+MPeasAgenctNo+" </td>"+ 
						"<td>"+airtelMoneyAgenetNo+"</td>"+
						"<td>"+orangeMoneyAgentNo+"</td>"+
						"<td> "+mpesaTillNumber+"</td>"+
						"</tr>";
						
						$("#tbody_data3").append(appendTxt);	  
						rowindex = ++rowindex;
						colindex = ++ colindex; 
				}
	}
	
		
});
 

function TerminateConfirm(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/storeTermnateConfirmAct.action';
	$("#form1").submit();
	return true;
}
function getGenerateScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/generateMerchantAct.action';
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
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Store Terminate </a></li>
					</ul>
				</div>
				<div class="row-fluid sortable"><!--/span--> 
						<div class="box span12"> 
							<div class="box-header well" data-original-title>
									<i class="icon-edit"></i>Store Primary Details
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div> 
					
							<div id="primaryDetails" class="box-content">
								<fieldset>
									<table width="950" border="0" cellpadding="5" cellspacing="1" 
										class="table table-striped table-bordered bootstrap-datatable " >
										<tr class="even">
											<td width="20%"><strong><label for="Merchant Name">Merchant Name</label></strong></td>
											<td width="30%"> ${responseJSON.merchantName}
											</td>
											<td width="20%"><strong><label for="Merchant ID">Merchant ID</label></strong></td>
											<td width="30%"> ${responseJSON.merchantID}
												<input type="hidden" name="merchantID" id="merchantID" value="${responseJSON.merchantID}" />
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Store Name">Store Name</label></strong></td>
											<td > ${responseJSON.storeName}
											</td>
											<td ><strong><label for="Store ID">Store ID</label></strong></td>
											<td > ${responseJSON.storeId}	
												<input type="hidden" name="storeId" id="storeId" value="${responseJSON.storeId}" />
											</td>	
										</tr>
										<tr class="even">
											<td ><strong><label for="Location">Location</label></strong></td>
											<td > ${responseJSON.locationName}
											</td>
											<td ><strong><label for="KRA PIN">KRA PIN</label></strong></td>
											<td > ${responseJSON.KRA_PIN}
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
								<i class="icon-edit"></i>Communication Details
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div> 
							<div id="communicationDetails" class="box-content">
								<fieldset>
						   		 	<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
											<tr class="even">
												<td width="20%"><strong><label for="Manager Name">Manager Name</label></strong></td>
												<td width="30%"> ${responseJSON.managerName}
												</td>
												<td width="20%"><strong><label for="Email">Email</label></strong></td>
							                    <td width="30%"> ${responseJSON.email}
												</td>
											</tr>
											<tr class="odd">
												<td ><strong><label for="Address Line 1">Address Line 1</label></strong></td>
												<td > ${responseJSON.addressLine1}
												</td>
												<td ><strong><label for="Address Line 2 ">Address Line 2 </label></strong></td>
												<td > ${responseJSON.addressLine2}
												</td>
											</tr>
											<tr class="even">
												<td ><strong><label for="Address Line 3 ">Address Line 3 </label></strong></td>
												<td > ${responseJSON.addressLine3}
												</td>
												<td><strong><label for="City">City</label></strong></td>
												<td > ${responseJSON.city}
												</td>
											</tr>
											<tr class="odd">
												<td ><strong><label for="P.O. Box Number ">P.O. Box Number</label></strong></td>
												<td > ${responseJSON.poBoxNumber}
												</td>
												<td ><strong><label for="Telephone Number 1">Telephone Number 1</label></strong></td>
												<td>	${responseJSON.telephoneNumber1}
												</td>
											</tr>
							               <tr class="even">
												<td ><strong><label for="Telephone Number 2 ">Telephone Number 2</label></strong>	</td>
												<td > ${responseJSON.telephoneNumber2}
												</td>
												<td ><strong><label for="Mobile Number">Mobile Number</label></strong></td>
												<td > ${responseJSON.mobileNumber}
												</td>
										   </tr>
							               <tr class="odd">
												<td ><strong><label for="Fax Number ">Fax Number </label></strong></td>
												<td > ${responseJSON.faxNumber}
												</td>
												<td ></td>
												<td > </td>
										   </tr>
										   	<tr class="even">
											<td ><strong><label for="Primary Contact Person">Primary Contact Person</label></strong></td>
											<td > ${responseJSON.prmContactPerson}
											</td>
											<td><strong><label for="Primary Contact Number">Primary Contact Number</label></strong></td>
											<td > ${responseJSON.prmContactNumber}
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
								<i class="icon-edit"></i>Bank Account Information
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div> 
							<div id="bankAccountInformation" class="box-content">
								<input type="hidden" name="bankAcctMultiData" id="bankAcctMultiData" value="${bankAcctMultiData}"></input>
								<fieldset> 
									<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
										id="bankAcctData"  >
										  <thead>
												<tr >
													<th>Sno</th>
													<th>Account Number</th>
													<th>Account Description</th>
													<th>Bank Name</th>
													<th>Bank Branch</th>
													<th>Transfer Code(Swift Code)</th>
												</tr>
										  </thead>    
										 <tbody  id="tbody_data1">
										 </tbody>
									</table>
								</fieldset>
							</div>
						</div>
					</div>
							
					<div class="row-fluid sortable"><!--/span--> 
						<div class="box span12"> 			 
							<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>Agent Based Information
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
								</div>
							</div> 
								
							<div id="agentBasedInfo" class="box-content"> 
									<input type="hidden" name="agentMultiData" id="agentMultiData" value="${agentMultiData}"></input>
								<fieldset> 
									<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
										id="documentData"   >
										  <thead>
												<tr >
													<th>Sno</th>
													<th>Bank Agent Number</th>
													<th>MPesa Agent Number</th>
													<th>Airtel Money Agent Number</th>
													<th>Orange Money Agent Number</th>
													<th>MPesa Till Number</th>
												</tr>
										  </thead>    
										 <tbody  id="tbody_data3">
										 </tbody>
									</table> 
								</fieldset> 
							</div>
						</div>
					</div>
					<div class="row-fluid sortable"><!--/span--> 
						<div class="box span12"> 			 
							<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>Document Information
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div> 
							<div id="documentInformation" class="box-content">
									
								<fieldset> 
									<input type="hidden" name="documentMultiData" id="documentMultiData" value="${documentMultiData}"></input>
								
									<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
											id="documentData"  >
										  <thead>
												<tr >
													<th>Sno</th>
													<th>Document ID</th>
													<th>Document Description</th>
													<th>Grace Period</th>
													<th>Mandatory</th>
												</tr>
										  </thead>    
										 <tbody  id="tbody_data2">
										 </tbody>
									</table> 
								</fieldset> 
							</div>  
						</div>
 				</div>
		<div class="form-actions">
			<a  class="btn btn-danger" href="#" onClick="getGenerateScreen()">Back</a> &nbsp;&nbsp;
			<a  class="btn btn-success" href="#" onClick="TerminateConfirm()">Confirm</a>
		</div>  
	</div> 
</form>
</body>
</html>
