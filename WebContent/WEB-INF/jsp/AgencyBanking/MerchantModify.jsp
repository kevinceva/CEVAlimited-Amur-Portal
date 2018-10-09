
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>IMPERIAL</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@taglib uri="/struts-tags" prefix="s"%> 
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 
<script type="text/javascript" >
var merchantCreateRules= {
	rules : { 
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
		email : {required : true, email : true}, 
		postalCode : {required : true, number : true},
		area : {required : true},
		lrNumber : { required : true, regex: /^[a-zA-Z0-9\/\ ]+$/ }
  	},
	messages : { 
		managerName : { required : "Please Enter Manager Name."},
		addressLine1 : { required : "Please Enter Address1."},
		addressLine2 : { required : "Please Enter Address2."},
		city		: { required : "Please Enter City/Town."},
		poBoxNumber : { required : "Please Enter P.O. Box Number."},
		//telephoneNumber1 : { required : "Please Enter Merchant Name."},
		mobileNumber : {required : "Please Enter Mobile Number."},
		//faxNumber : { required : "Please Enter Merchant Name."},
		prmContactPerson : { required : "Please Enter Primary Contact Number."},
		prmContactNumber : { required : "Please Enter Primary Contact Person."},
		email : { required : "Please Enter Email."}, 
		postalCode : {
				required : "Please Enter Postal Code.",
				number : "Please Enter Numbers Only."
		},
		area : {
				required : "Please Enter Area."
 		},
		lrNumber : {
				required : "Please Enter L/R Number.",
				regex : "Please Enter valid L/R Number."
		} 
	}
};

$(document).ready(function() {  
  $.validator.addMethod("regex", function(value, element, regexpr) {          
		 return regexpr.test(value);
   }, "");  
}); 
 

function modifyMerchant(){ 
	$("#form1").validate(merchantCreateRules);
	if($("#form1").valid()){
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/merchantmodifySubmitAct.action';
		$("#form1").submit();
		return true;
	}else{
		return false;
	}
}

function getGenerateScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/generateMerchantAct.action';
	$("#form1").submit();
	return true;
}
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
</style>	 
</head>
<s:set value="responseJSON" var="respData"/>	 
<body>
	<form name="form1" id="form1" method="post" action="" > 
		
			<div id="content" class="span10"> 
			    <div>
						<ul class="breadcrumb">
						  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
						  <li> <a href="#"> Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
						  <li><a href="#">Merchant Modify</a></li>
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
						
						<div class="box-content" id="primaryDetails"> 
						 <fieldset> 
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable ">
									<tr class="even">
										<td width="20%"><strong><label for="Merchant Name"><strong>Merchant Name</strong></label></td>
										<td width="30%"> <s:property value='#respData.merchantName' /> 
										<input name="merchantName" type="hidden"  id="merchantName" class="field"  value="<s:property value='#respData.merchantName' />" > </td>
										<td width="20%"><strong><label for="Merchant ID"><strong>Merchant ID</strong></label></td>
										<td width="30%"> <s:property value='#respData.merchantID' /> <input name="merchantID" type="hidden" id="merchantID" class="field" value="<s:property value='#respData.merchantID' />" ></td>
									</tr>
									<tr class="odd">
										<td ><label  for="Location"><strong>Location</strong></label></td>
										<td > <s:property value='#respData.locationName' />
											<input name="location" type="hidden" class="field" id="location" value="<s:property value='#respData.locationName' />"  />
										</td>
										<td ><label  for="KRA PIN"><strong>KRA PIN</strong></label></td>
										<td > <s:property value='#respData.KRA_PIN' />
										<input name="kraPin" type="hidden" class="field" id="kraPin" value="<s:property value='#respData.KRA_PIN' />"  />
										</td>	
									</tr> 
									<tr class="even">
										<td ><label  for="Merchant Type"><strong>Merchant Type</strong></label></td>
										<td >
											<s:property value='#respData.MERCHANT_TYPE' /> 
											<input name="merchantType" type="hidden" class="field" id="merchantType" value="<s:property value='#respData.MERCHANT_TYPE' />"  />
										</td>
										<td ><label for="Member Type"><strong>Member Type</strong></label></td>
										<td >
											<s:property value='#respData.MEMBER_TYPE' /> 
											<input name="memberType" type="hidden" class="field" id="memberType" value="<s:property value='#respData.MEMBER_TYPE' />"  />
										</td>	
									</tr>
								</table>
						 </fieldset> 
					</div>
					 
				</div> 
			</div>
			 
			<div class="row-fluid sortable">
						<div class="box span12">
								<div class="box-header well" data-original-title>Store Information
									<div class="box-icon"> 
										<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
										<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
									</div>
								</div> 
								<div class="box-content" > 
									<table style = 'border: 1px solid #d7d7d7; font-family: Arial, Helvetica, sans-serif;font-size: 12px; color: #000000; font-weight: normal;' width='100%'   
										class='table table-striped table-bordered bootstrap-datatable datatable'  >
										<thead>
											<tr>
												<th>S No</th>
												<th>Store ID</th>
												<th class='hidden-phone'>Store Name </th>
												<th >Store Created By</th>
												<th class='hidden-phone'>Store Creation Date</th>
												<th >Store Authorized By</th>
												<th class='hidden-phone'>Store Authorized Date</th>
 												<th>Status </th> 
 											</tr>
										</thead> 
										<tbody id="StoreDetails" > 		
											<s:set value="responseJSON['storeData']" var="storeInfoForMer"/>
											<s:iterator value="#storeInfoForMer" var="userGroups" status="userStatus"> 
												<s:if test="#userStatus.even == true" > 
													<tr class="even" index="<s:property value='#userStatus.index' />"  id="<s:property value='#userStatus.index' />">
												 </s:if>
												 <s:elseif test="#userStatus.odd == true">
													<tr class="odd" index="<s:property value='#userStatus.index' />"  id="<s:property value='#userStatus.index' />">
												 </s:elseif> 
														<td><s:property value="#userStatus.index+1" /></td> 
														<td class='hidden-phone'><s:property value="#userGroups['storeId']" /></td> 
														<td class='hidden-phone'><s:property value="#userGroups['storeName']" /></td> 
														<td class='hidden-phone'><s:property value="#userGroups['makerId']" /></td> 
														<td class='hidden-phone'><s:property value="#userGroups['makerDate']" /></td> 
														<td class='hidden-phone'><s:property value="#userGroups['authid']" /></td> 
														<td class='hidden-phone'><s:property value="#userGroups['authdttm']" /></td> 
															<s:if test="#userGroups['status'] == 'Active'" > 
																 <s:set value="%{'label-success'}" var="merchStatus" /> 
															</s:if>
															<s:elseif test="#userGroups['status'] == 'Inactive'" >
																  <s:set value="%{'label-warning'}" var="merchStatus" /> 
															 </s:elseif>
															 <s:elseif test="#userGroups['status'] == 'Un-Authorize'" >
																  <s:set value="%{'label-primary'}" var="merchStatus" /> 
															 </s:elseif> 
														<td class='hidden-phone'><a href='#' class='label <s:property value="#merchStatus" />' ><s:property value="#userGroups['status']" /> </a></td> 
													</tr>  
											</s:iterator>   
										 </tbody>
									</table>
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
												<td><label for="City"><strong>City/Town<font color="red">*</font></strong></label></td>
												<td ><input name="city" type="text" class="field" id="city"  value="<s:property value='#respData.city' />" required='true' /></td>
											</tr>
											<tr class="odd">
												<td ><label for="Address Line 3 "><strong>Area<font color="red">*</font></strong></label></td>
												<td ><input name="area" type="text" class="field" id="area" value="<s:property value='#respData.AREA' />" required='true' /></td> 
												<td ><label for="Postal Code"><strong>Postal Code<font color="red">*</font></strong></label></td>
												<td ><input name="postalCode" type="text" id="postalCode" class="field" value="<s:property value='#respData.POSTALCODE' />"  required='true'/> <span id="postalCode_err" class="errmsg"></span></td>
												
										   </tr>
											<tr class="even">
												<td ><label for="P.O. Box Number "><strong>P.O. Box Number<font color="red">*</font></strong></label></td>
												<td ><input name="poBoxNumber" type="text" id="poBoxNumber" class="field" value="<s:property value='#respData.poBoxNumber' />" required='true' /></td>
												<td ><label for="Telephone Number 1"><strong>Telephone Number 1</strong></label></td>
												<td><input name="telephoneNumber1" type="text" id="telephoneNumber1" class="field" value="<s:property value='#respData.telephoneNumber1' />" /><span id="telephoneNumber1_err" class="errmsg"></span></td>											 
												
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
											<td><label for="L/R Number"><strong>L/R Number<font color="red">*</font></strong></label></td>
											<td ><input name="lrNumber" type="text" class="field" id="lrNumber" value="<s:property value='#respData.LRNUMBER' />" required='true' /></td>
										</tr>
										<tr class="odd">  
											<td ><label for="Primary Contact Person"><strong>Primary Contact Person<font color="red">*</font></strong></label></td>
											<td ><input name="prmContactPerson" id="prmContactPerson" class="field" type="text" value="<s:property value='#respData.PRIMARY_CONTACT_NAME' />" required='true' ></td>
											<td><label for="Primary Contact Number"><strong>Primary Contact Number<font color="red">*</font></strong></label></td>
											<td ><input name="prmContactNumber" type="text" class="field" id="prmContactNumber" value="<s:property value='#respData.PRIMARY_CONTACT_NUMBER' />" required='true' /><span id="prmContactNumber_err" class="errmsg"></span></td>
										</tr>
										<tr class="even">  
											<td ><label for="Country"><strong>Country<font color="red">*</font></strong></label></td>
											<td >
												<select id="country" name="country"   required='true' data-placeholder="Choose country..." 
												class="chosen-select" style="width: 220px;">
												<option value="">Select</option>
												<option value="Kenya">Kenya</option>
												<option value="Uganda">Uganda</option>
											</select> 
											</td>
											<td >&nbsp;</td >
											<td >&nbsp;</td >
										</tr>
									</table>
								</fieldset> 
							</div> 
						</div> 
				</div> 
		<div class="form-actions">
			<a  class="btn btn-danger" href="#" onClick="getGenerateScreen()">Back</a>&nbsp;
			<a  class="btn btn-success" href="#" onClick="modifyMerchant()">Submit</a>  
		</div> 
</div> 
</form>
<script type="text/javascript">
$(function(){
	
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
	
	 
	$('#country').val('<s:property value="#respData.COUNTRY" />');
	$('#country').trigger("liszt:updated");  
	
});
</script>
</body>
</html>
