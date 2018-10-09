<!DOCTYPE html>
<html lang="en">
<%@taglib uri="/struts-tags" prefix="s"%> 
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Brian Kiptoo">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 <s:set value="responseJSON" var="respData"/> 
<script type="text/javascript"> 
function createMerchant(){ 
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/storeUpdateConfirmAct.action';
	$("#form1").submit();
	return true; 
}

function getPreviousScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/storeModifySubmitBackAct.action';
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
</style > 
</head>

<body>
	<form name="form1" id="form1" method="post" action="">
		<div id="content" class="span10"> 
			 
			<div>
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="generateMerchantAct.action?pid=9">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">New Store Confirmation</a></li>
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
						<i class="icon-edit"></i>Merchant Primary Details
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

					</div>
				</div>  
				<div class="box-content" id="primaryDetails">  
				  <fieldset> 
						<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
							<tr class="even">
								<td width="20%"><label for="Merchant Name"><strong>Merchant Name</strong></label></td>
								<td width="30%">	<s:property value="merchantName"/> 
									<input name="merchantName" type="hidden"  id="merchantName" class="field"  value="<s:property value="merchantName"/>" maxlength="50">
								</td>
								<td width="20%"><label for="Merchant ID"><strong>Merchant ID</strong></label></td>
								<td width="30%"> <s:property value="merchantID"/>
									<input name="merchantID" type="hidden" id="merchantID" class="field" value="<s:property value="merchantID"/>" maxlength="15" >
								</td>
							</tr>
							<tr class="odd"> 
								<td><label for="Store Name"><strong>Store Name</strong></label></td>
								<td> <s:property value="storeName"/>
									<input name="storeName" type="hidden" class="field" id="storeName" value="<s:property value="storeName"/>" />
								</td>
								<td ><label for="Store ID"><strong>Store ID</strong></label></td>
								<td ><s:property value="storeId"/> 
									<input name="storeId"  type="hidden" id="storeId" class="field"  value="<s:property value="storeId"/>" > 
								</td>		
							</tr>
							<tr class="even">
								<td ><label for="Location"><strong>Location</strong></label></td>
								<td > <s:property value="locationVal"/>
									<input name="location" type="hidden" class="field" id="location" value="<s:property value="location"/>" />
									<input name="LocationInfo" type="hidden" class="field" id="LocationInfo" value="<s:property value="location"/>" />
								</td>
								<td ><label for="KRA PIN"><strong>KRA PIN</strong></label></td>
								<td ><s:property value="kraPin"/> 
									<input name="KRA_PIN" type="hidden" class="field" id="KRA_PIN" value="<s:property value="kraPin"/>"  />
								</td>	
							</tr> 
							<tr class="odd">
								<td ><label for="Till Id"><strong>Till Id</strong></label></td>
								<td >
									<s:property value="tillId"/>
									 <input name="tillId" type="hidden" class="field" id="tillId" value="<s:property value="tillId"/>"   />
								</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td> 
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
					<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
							<tr class="even">
								<td width="20%"><label for="Manager Name"><strong>Manager Name</strong></label></td>
								<td width="30%"> <s:property value="managerName"/>
									<input name="managerName" type="hidden" id="managerName" class="field" value="<s:property value="managerName"/>" maxlength="50" >
								</td>
								<td width="20%"><label for="Email"><strong>Email</strong></label></td>
								<td width="30%"> <s:property value="email"/>
									<input name="email" type="hidden"  id="email" class="field"  value="<s:property value="email"/>" >
								</td>
							</tr>
							<tr class="odd">
								<td ><label for="Address Line 1"><strong>Address Line 1</strong></label></td>
								<td > <s:property value="addressLine1"/>
									<input name="addressLine1" id="addressLine1" class="field" type="hidden"  maxlength="50" value="<s:property value="addressLine1"/>"  >
								</td>
								<td ><label for="Address Line 2 "><strong>Address Line 2 </strong></label></td>
								<td > <s:property value="addressLine2"/>
									<input name="addressLine2" type="hidden" class="field" id="addressLine2"   value="<s:property value="addressLine2"/>" />
								</td>
							</tr>
							<tr class="even">
								<td ><label for="Address Line 3 "><strong>Address Line 3</strong></label></td>
								<td > <s:property value="addressLine3"/>
									<input name="addressLine3" type="hidden" class="field" id="addressLine3" value="<s:property value="addressLine3"/>" />
								</td>
								<td><label for="City"><strong>City</strong></label></td>
								<td > <s:property value="city"/>
									<input name="city" type="hidden" class="field" id="city"  value="<s:property value="city"/>" />
								</td>
							</tr>
							 
							<tr class="even">
								<td ><label for="P.O. Box Number "><strong>P.O. Box Number</strong></label></td>
								<td > <s:property value="poBoxNumber"/>
									<input name="poBoxNumber" type="hidden" id="poBoxNumber" class="field" value="<s:property value="poBoxNumber"/>" />
								</td>
								<td ><label for="Telephone Number 1"><strong>Telephone Number 1</strong></label></td>
								<td> <s:property value="telephoneNumber1"/>
									<input name="telephoneNumber1" type="hidden" id="telephoneNumber1" class="field" value="<s:property value="telephoneNumber1"/>" />
								</td>
							</tr>
						   <tr class="even">
								<td ><label for="Telephone Number 2 "><strong>Telephone Number 2</strong></label>	</td>
								<td > <s:property value="telephoneNumber2"/>
									<input name="telephoneNumber2" type="hidden" class="field" id="telephoneNumber2"  value="<s:property value="telephoneNumber2"/>" />
								</td>
								<td ><label for="Mobile Number"><strong>Mobile Number</strong></label></td>
								<td > <s:property value="mobileNumber"/>
									<input name="mobileNumber" id="mobileNumber" class="field" type="hidden"  value="<s:property value="mobileNumber"/>" />
								</td>
						   </tr>
						   <tr class="odd">
								<td ><label for="Fax Number "><strong>Fax Number </strong></label></td>
								<td > <s:property value="faxNumber"/>
									<input name="faxNumber"  type="hidden" class="field" id="faxNumber" value="<s:property value="faxNumber"/>"  />
								</td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
					
						   </tr>
							<tr class="even">
								<td ><label for="Primary Contact Person"><strong>Primary Contact Person</strong></label></td>
								<td > <s:property value="prmContactPerson"/>
									<input name="PRIMARY_CONTACT_NAME" id="PRIMARY_CONTACT_NAME" class="field" type="hidden" value="<s:property value="prmContactPerson"/>" >
								</td>
								<td><label for="Primary Contact Number"><strong>Primary Contact Number</strong></label></td>
								<td > <s:property value="prmContactNumber"/>
									<input name="PRIMARY_CONTACT_NUMBER" type="hidden" class="field" id="PRIMARY_CONTACT_NUMBER" value="<s:property value="prmContactNumber"/>" />
								</td>
							</tr>
							 
					</table>
				</fieldset> 
			</div> 
		</div> 
		</div>
		  
		 
		<div class="form-actions">
				<a  class="btn btn-danger" href="#" onClick="getPreviousScreen()">Back</a>
				<a  class="btn btn-success" href="#" onClick="createMerchant()">Confirm</a>
		</div> 
	</div> 
</form>
</body>
</html>
