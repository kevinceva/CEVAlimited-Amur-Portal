
<!DOCTYPE html>
<html lang="en">
<%@taglib uri="/struts-tags" prefix="s"%> 
<head>
<meta charset="utf-8">
<title>IMPERIAL</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>

  <s:set value="responseJSON" var="respData"/>
<script type="text/javascript" >  
function getGenerateMerchantScreen(){
	$("#form1")[0].action='<%=ctxstr%>/<%=appName %>/generateMerchantAct.action';
	$("#form1").submit();
	return true;
}
function updateStore(){
	
	$("#form1").validate(merchantCreateRules);
	if($("#form1").valid()){ 
		$("#form1")[0].action='<%=ctxstr%>/<%=appName %>/modifyStoreConfirm.action';
		$("#form1").submit();
		return true;
	} else {
		return false;
	}
}

var merchantCreateRules= {
		rules : { 
 			storeName : {required : true},
			location : {required : true},
	  		managerName : {required : true},
			addressLine1 : {required : true},
			addressLine2 : {required : true},
			city		: {required : true},
			poBoxNumber : {required : true},
			//telephoneNumber1 : {required : true},
			mobileNumber : {required : true , number : true},
			//faxNumber : {required : true , number : true},
			prmContactPerson : {required : true},
			prmContactNumber : {required : true , number : true},
			email : {required : true, email : true} 
		}, 
		messages : { 
 			storeName : { required : "Please Enter Store Name."},
			location : { required : "Please Select Location."},
	 		managerName : { required : "Please Enter Manager Name."},
			addressLine1 : { required : "Please Enter Address1."},
			addressLine2 : { required : "Please Enter Address2."},
			city		: { required : "Please Enter City."},
			poBoxNumber : { required : "Please Enter P.O. Box Number."},
			//telephoneNumber1 : { required : "Please Enter Merchant Name."},
			mobileNumber : {required : "Please Enter Mobile Number."},
			//faxNumber : { required : "Please Enter Merchant Name."},
			prmContactPerson : { required : "Please Enter Primary Contact Person."},
			prmContactNumber : { required : "Please Enter Primary Contact Number."},
			email : { required : "Please Enter Email."} 
		}
	};

</script>
 <style type="text/css">
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
.errmsg {
	color: red;
}
.errors {
	color: red;
}
</style > 
</head>

<body>
	<form name="form1" id="form1" method="post" action="">	
		<div id="content" class="span10">  
			    <div>
					<ul class="breadcrumb">
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#"> Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Store Modify </a></li>
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
									<i class="icon-edit"></i>Store Primary Details
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div>  
								
						<div class="box-content" id="primaryDetails"> 
							<fieldset>  
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable " >
									<tr class="even">
										<td width="20%"><label for="Merchant Name"><strong>Merchant Name</strong></label></td>
										<td width="30%"> 
											<input name="merchantName" type="text" id="merchantName" class="field" value="${responseJSON.merchantName}" readonly >
										</td>
										<td width="20%"><label for="Merchant ID"><strong>Merchant ID</strong></label></td>
										<td width="30%"> 
											<input name="merchantID" type="text" id="merchantID" class="field" value="${responseJSON.merchantID}" readonly >
										</td>
									</tr>
									<tr class="odd">
										<td><label for="Store Name"><strong>Store Name</strong></label></td>
										<td> 
											<input name="storeName" type="text" id="storeName" class="field" value="${responseJSON.storeName}" >
										</td>
										<td ><label for="Store ID"><strong>Store ID</strong></label></td>
										<td > 	
											<input name="storeId" type="text" id="storeId" class="field" value="${responseJSON.storeId}" readonly >
										</td>	
									</tr>
									<tr class="even">
										<td ><label for="Location"><strong>Location</strong></label></td>
										<td >
											 <s:select cssClass="chosen-select" 
												headerKey="" 
												headerValue="Select"
												list="#respData.LOCATION_LIST" 
												name="location" 
												value="#respData.locationName" 
												id="location" requiredLabel="true" 
												theme="simple"
												data-placeholder="Choose Location..."  
												 />  
											<!-- <input name="LocationInfo" type="text" id="LocationInfo" class="field" value="${responseJSON.locationName}"  > -->
										</td>
										<td ><label for="KRA PIN"><strong>KRA PIN</strong></label></td>
										<td > 
											<input name="kraPin" type="text" id="kraPin" class="field" value="${responseJSON.KRA_PIN}"  >
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
										<table width="950" border="0" cellpadding="5" cellspacing="1" 
												class="table table-striped table-bordered bootstrap-datatable ">
											<tr class="even">	
												<td width="20%"><label for="Manager Name"><strong>Manager Name<font color="red">*</font></strong></label></td>
												<td width="30%"><input name="managerName" type="text" id="managerName" class="field" value="<s:property value='#respData.managerName' />" maxlength="50" required='true' ></td>
												<td width="20%"><label for="Email"><strong>Email<font color="red">*</font></strong></label></td>
												<td width="30%"><input name="email" type="text"  id="email" class="field"  value="<s:property value='#respData.email' />" required='true' > </td>
											</tr>
											<tr class="odd">
												<td ><label for="Address Line 1"><strong>Address Line 1<font color="red">*</font></strong></label></td>
												<td ><input name="addressLine1" id="addressLine1" class="field" type="text"  maxlength="50" value="<s:property value='#respData.addressLine1' />" required='true' ></td>
												<td ><label for="Address Line 2 "><strong>Address Line 2<font color="red">*</font> </strong></label></td>
												<td ><input name="addressLine2" type="text" class="field" id="addressLine2"   value="<s:property value='#respData.addressLine2' />" required='true' /></td>
											</tr>
											<tr class="even">
												<td ><label for="Address Line 3 "><strong>Address Line 3</strong></label></td>
												<td ><input name="addressLine3" type="text" class="field" id="addressLine3" value="<s:property value='#respData.addressLine3' />" /></td>
												<td><label for="City"><strong>City<font color="red">*</font></strong></label></td>
												<td ><input name="city" type="text" class="field" id="city"  value="<s:property value='#respData.city' />" required='true' /></td>
											</tr>
											<tr class="odd">
												<td ><label for="Address Line 3 "><strong>Area<font color="red">*</font></strong></label></td>
												<td ><input name="area" type="text" class="field" id="area" value="<s:property value='#respData.area' />" required='true' /></td> 
												<td ><label for="Postal Code"><strong>Postal Code<font color="red">*</font></strong></label></td>
												<td ><input name="postalCode" type="text" id="postalCode" class="field" value="<s:property value='#respData.postalcode' />"  required='true'/> <span id="postalCode_err" class="errmsg"></span></td>
												
										   </tr>
											 
										   <tr class="odd">
												<td ><label for="Telephone Number 2 "><strong>Telephone Number 2</strong></label>	</td>
												<td ><input name="telephoneNumber2" type="text" class="field" id="telephoneNumber2"  value="<s:property value='#respData.telephoneNumber2' />" /><span id="telephoneNumber2_err" class="errmsg"></span></td>												
												<td ><label for="Mobile Number"><strong>Mobile Number<font color="red">*</font></strong></label></td>
												<td ><input name="mobileNumber" id="mobileNumber" class="field" type="text"  value="<s:property value='#respData.mobileNumber' />" required='true' /><span id="mobileNumber_err" class="errmsg"></span></td>
												
										   </tr>
										<tr class="even">
											<td ><label for="Fax Number"><strong>Fax Number </strong></label></td>
											<td ><input name="faxNumber"  type="text" class="field" id="faxNumber" value="<s:property value='#respData.faxNumber' />" /><span id="faxNumber_err" class="errmsg"></span></td>											 
											<td >&nbsp;</td >
											<td >&nbsp;</td >
										</tr>
										<tr class="odd">  
											<td ><label for="Primary Contact Person"><strong>Primary Contact Person<font color="red">*</font></strong></label></td>
											<td ><input name="prmContactPerson" id="prmContactPerson" class="field" type="text" value="<s:property value='#respData.PRIMARY_CONTACT_NAME' />" required='true' ></td>
											<td><label for="Primary Contact Number"><strong>Primary Contact Number<font color="red">*</font></strong></label></td>
											<td ><input name="prmContactNumber" type="text" class="field" id="prmContactNumber" value="<s:property value='#respData.PRIMARY_CONTACT_NUMBER' />" required='true' /><span id="prmContactNumber_err" class="errmsg"></span></td>
										</tr>
										 
									</table>
								</fieldset> 
							</div>  
					</div> 
				</div> 
	<div class="form-actions">
			<a  class="btn btn-danger" href="#" onClick="getGenerateMerchantScreen()">Back</a> &nbsp;&nbsp;
			<a  class="btn btn-success" href="#" onClick="updateStore()">Modify</a>
	</div>

</div> 
<script >
$(function() {  

    var config = {
      '.chosen-select'           : {},
      '.chosen-select-deselect'  : {allow_single_deselect:true},
      '.chosen-select-no-single' : {disable_search_threshold:10},
      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
      '.chosen-select-width'     : {width:"95%"}
    }
    for (var selector in config) {
      $(selector).chosen(config[selector]);
    } 
	
	
    $('#location').val('<s:property value="#respData.location" />');
	$('#location').trigger("liszt:updated");  
	
});
</script>
</form>
</body>
</html>
