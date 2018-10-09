
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
	var bankfinalData="${responseJSON.bankMultiData}";
	
 	bankfinalData=bankfinalData.slice(1);
	var bankfinalDatarows=bankfinalData.split("#");
	
	if(val % 2 == 0 ) {
	addclass = "even";
	val++;
	}
	else {
	addclass = "odd";
	val++;
	}  
	var rowCount = $('#tbody_data > tr').length;

	rowindex = ++rowindex;
	colindex = ++ colindex; 
		for(var i=0;i<bankfinalDatarows.length;i++){
			var eachrow=bankfinalDatarows[i];
			var eachfieldArr=eachrow.split(",");
			var bin=eachfieldArr[0];
			var binDiscription=eachfieldArr[1];
			var binType = eachfieldArr[2];
			var acquirerId = eachfieldArr[3];
			
			var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
				"<td>"+rowindex+"</td>"+
				"<td>"+bin+"</td>"+	
				"<td>"+binDiscription+" </td>"+ 
				"<td>"+binType+"</td>"+
				"<td>"+acquirerId+"</td>"+
			"</tr>";
			$("#tbody_data").append(appendTxt);	  

		}
		
		
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

	rowindex = ++rowindex;
	colindex = ++ colindex; 
		for(var i=0;i<bankacctfinalDatarows.length;i++){
			var eachrow=bankacctfinalDatarows[i];
			var eachfieldArr=eachrow.split(",");
			var accountCategory=eachfieldArr[0];
			var accountNumber=eachfieldArr[1];
			var acctDescription = eachfieldArr[2];
			var transferCode = eachfieldArr[3];
			
			var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
				"<td>"+rowindex+"</td>"+
				"<td>"+accountCategory+"</td>"+	
				"<td>"+accountNumber+" </td>"+ 
				"<td>"+acctDescription+"</td>"+
				"<td>"+transferCode+"</td>"+
			"</tr>";
			
			$("#tbody_data1").append(appendTxt);	  

		}
		
		
	var val = 1;
	var rowindex = 1;
	var colindex = 0;
	var documentfinalData="${responseJSON.documentMultiData}";
	documentfinalData=documentfinalData.slice(1);
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

	rowindex = ++rowindex;
	colindex = ++ colindex; 
	for(var i=0;i<documentfinalDatarows.length;i++){
		var eachrow=documentfinalDatarows[i];
		var eachfieldArr=eachrow.split(",");
		var documentId=eachfieldArr[0];
		var documentDescription=eachfieldArr[1];
		var gracePeriod = eachfieldArr[2];
		var mandatory = eachfieldArr[3];
		
			var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
				"<td>"+rowindex+"</td>"+
				"<td>"+documentId+"</td>"+	
				"<td>"+documentDescription+" </td>"+ 
				"<td>"+gracePeriod+"</td>"+
				"<td>"+mandatory+"</td>"+
			"</tr>";
			
			$("#tbody_data2").append(appendTxt);	  

	}
			
});
 
	
</script>
<script type="text/javascript">


var enterMerchantName= "Pease enter Merchant Name.";
var enterMerchanID= "Please enter Merchant ID.";
var MerchantIDMinLength="Merchant ID length should be 15";
var MerchantIDMaxLength="Merchant ID length should be 15";
var enterStoreId="Please enter Store ID";
var enterStoreName="Please enter Store Name";
var selectLocation="Please select Location";
var enterKRAPIN = "Please enter KRA PIN";
var enterManagerName = "Please enter Manage Name";
var enterAddressLine1 = "Please enter Address Line1";
var enterAddressLine2 ="Please enter Address Line2";
var enterCity = "Please enter City";
var enterPostBoxNo = "Please enter PO Box No";
var  enterTelephoneNumber1 = "Please enter Tele phone number 1";
var enterMobileNumber = "Please enter Mobile Number";
var enterFaxNumber = "Please enter Fax Number";
var enterPrmContactPerson = "Please enter Primary Contact Person";
var enterPrmContactNumber = "Please enter Primary Contact Number";

var merchantNamerules = {
	required : true
};
var merchantIDrules = {
	required : true,
	minlength :15,
	maxlength :15
};

var storeIdRules = {
	required : true
};

var storeNameRules = {
	required : true
};

var locationRules = {
	required : true
};

var kraPinRules = {
	required : true
};

var managerNameRules = {
	required : true
};

var addressLine1Rules = {
	required : true
};

var addressLine2Rules = {
	required : true
};

var cityRules = {
	required : true
};

var poBoxNumberRules = {
	required : true
};

var telephoneNumber1Rules = {
	required : true
};

var mobileNumberRules = {
	required : true
};

var faxNumberRules = {
	required : true
};

var prmContactPersonRules = {
	required : true
};

var prmContactNumberRules = {
	required : true
};

var merchantNamemessages = {
	required : enterMerchantName	
};

var merchantIDmessgaes = {
	required : enterMerchanID,
	minlength: MerchantIDMinLength,
	maxlength : MerchantIDMaxLength
};

var storeIdMessages = {
	required : enterStoreId	
};

var storeNameMessages = {
	required : enterStoreName	
};
var locationMessages = {
	required : selectLocation
};

var kraPinMessages = {
	required : enterKRAPIN
};

var managerNameMessages = {
	required : enterManagerName
};

var addressLine1Messages = {
	required : enterAddressLine1
};
var addressLine2Messages = {
	required : enterAddressLine2
};

var cityMessages = {
	required : enterCity
};
var poBoxNumberMessages = {
	required : enterPostBoxNo
};
var telephoneNumber1Messages = {
	required : enterTelephoneNumber1
};
var mobileNumberMessages = {
	required : enterMobileNumber
};
var faxNumberMessages = {
	required : enterFaxNumber
};
var prmContactPersonMessages = {
	required : enterPrmContactPerson
};
var prmContactNumberMessages = {
	required : enterPrmContactNumber
};

var merchantCreateRules= {
	rules : {
		merchantName : merchantNamerules,
		merchantID : merchantIDrules,
		storeId : storeIdRules,
		storeName : storeNameRules,
		location : locationRules,
		kraPin : kraPinRules,
		managerName : managerNameRules,
		addressLine1 : addressLine1Rules,
		addressLine2 : addressLine2Rules,
		city		: cityRules,
		poBoxNumber : poBoxNumberRules,
		telephoneNumber1 : telephoneNumber1Rules,
		mobileNumber : mobileNumberRules,
		faxNumber : faxNumberRules,
		prmContactPerson : prmContactPersonRules,
		prmContactNumber : prmContactNumberRules
	},
	messages : {
		merchantName : merchantNamemessages,
		merchantID : merchantIDmessgaes,
		storeId : storeIdMessages,
		storeName : storeNameMessages,
		location : locationMessages,
		kraPin : kraPinMessages,
		managerName : managerNameMessages,
		addressLine1 : addressLine1Messages,
		addressLine2 : addressLine2Messages,
		city		: cityMessages,
		poBoxNumber : poBoxNumberMessages,
		telephoneNumber1 : telephoneNumber1Messages,
		mobileNumber : mobileNumberMessages,
		faxNumber : faxNumberMessages,
		prmContactPerson : prmContactPersonMessages,
		prmContactNumber : prmContactNumberMessages
	}
};
function createMerchant(){
	
	$("#form1").validate(merchantCreateRules);
	if($("#form1").valid()){
			
			if($('#bankMultiData').val()==""){
				alert("Bank Information Missing");
				return false;
			}
			else if($('#bankAcctMultiData').val()==""){
				alert("Bank Information Missing");
				return false;
			}
			else if($('#documentMultiData').val()==""){
				alert("Bank Information Missing");
				return false;
			}
			else{
				$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/merchantCreateConfirmAct.action';
				$("#form1").submit();
				return true;
			}
	}else{
			return false;
	}
}


function getGenerateScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/getDashboardAct.action';
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
					  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Merchant View</a></li>
					</ul>
				</div>
				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
							<div class="box-header well" data-original-title>
									<i class="icon-edit"></i>Merchant Primary Details
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div> 							
					
							<div id="primaryDetails" class="box-content">
								<fieldset>
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable ">
									<tr class="even">
										<td width="20%"><strong><label for="Merchant Name">Merchant Name</label></strong></td>
										<td width="30%">	${responseJSON.merchantName}
										</td>
										<td width="20%"><strong><label for="Merchant ID">Merchant ID</label></strong></td>
										<td width="30%"> ${responseJSON.merchantID}
										</td>
									</tr>
									<tr class="odd">
										<td ><strong><label for="Location">Location</label></strong></td>
										<td > ${responseJSON.locationName}
										</td>
										<td ><strong><label for="KRA PIN">KRA PIN</label></strong></td>
										<td > ${responseJSON.KRA_PIN}
										</td>	
									</tr> 
									<tr class="even">
										<td ><strong><label for="Merchant Type">Merchant Type</label></strong></td>
										<td >
											${responseJSON.MERCHANT_TYPE}
										</td>
										<td ></td>
										<td ></td>	
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
						   		 	<table width="950" border="0" cellpadding="5" cellspacing="1" 
										class="table table-striped table-bordered bootstrap-datatable ">
											<tr class="even">
												<td width="20%"><strong><label for="Manager Name">Manager Name</label></strong></td>
												<td width="30%">${responseJSON.managerName}
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
												<td ><strong><label for="Address Line 3 ">Address Line 3</label></strong></td>
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
												<td> ${responseJSON.telephoneNumber1}
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
												<td ></td>
										   </tr>
										   	<tr class="even">
											<td ><strong><label for="Primary Contact Person">Primary Contact Person</label></strong></td>
											<td > ${responseJSON.prmContactNum}
											</td>
											<td><strong><label for="Primary Contact Number">Primary Contact Number</label></strong></td>
											<td > ${responseJSON.PRIMARY_CONTACT_NUMBER}
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
									<i class="icon-edit"></i>Bank Information
									<div class="box-icon">
										<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
										<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

									</div>
								</div> 	 
										<div id="bankInformation" class="box-content">
											<fieldset>
												<input type="hidden" name="bankMultiData" id="bankMultiData" value="${responseJSON.bankMultiData}"></input>
											
											<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
												id="bankData"  >
												  <thead>
														<tr >
															<th>Sno</th>
															<th>BIN</th>
															<th>Bin Description</th>
															<th>Bin Type </th>
															<th>Acquirer ID</th>
														</tr>
												  </thead>    
												 <tbody  id="tbody_data">
												 </tbody>
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
											id="bankAcctData" >
												  <thead>
														<tr >
															<th>Sno</th>
															<th>Account Category</th>
															<th>Account Number</th>
															<th>Account Description</th>
															<th>Transfer Code</th>
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
											<i class="icon-edit"></i>Document Information
											<div class="box-icon">
												<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
												<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

											</div>
										</div>
									<div id="documentInformation" class="box-content"> 
											<input type="hidden" name="documentMultiData" id="documentMultiData" value="${documentMultiData}"></input>
										<fieldset>	
											<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
													id="documentData" >
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
		<a  class="btn btn-danger" href="#" onClick="getGenerateScreen()">Next</a>
	</div> <!-- content ends -->
</div><!--/#content.span10-->
		 
</form>
</body>
</html>
