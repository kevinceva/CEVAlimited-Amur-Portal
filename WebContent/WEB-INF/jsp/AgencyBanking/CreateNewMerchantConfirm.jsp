<!DOCTYPE html>
<html lang="en">
<%@taglib uri="/struts-tags" prefix="s"%> 
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>

 <s:set value="responseJSON" var="respData"/>
<%-- <script type="text/javascript" > 
$(document).ready(function() {
    
	var val = 1;
	var rowindex = 1;
	var colindex = 0;
	var bankacctfinalData="${bankAcctMultiData}";
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
				"<td> "+bankBranch+"</td>"+
				"<td> "+transferCode+"</td>"+
				"</tr>";
				
				$("#tbody_data1").append(appendTxt);	  
				rowindex = ++rowindex;
				colindex = ++ colindex; 
		}
		
		
	var val = 1;
	var rowindex = 1;
	var colindex = 0;
	var documentfinalData="${documentMultiData}";
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
					"<td  >"+rowindex+"</td>"+
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
	var agentMultiData="${agentMultiData}";
	var agentMultiDatarows=agentMultiData.split("#");
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
					"<td >"+rowindex+"</td>"+
					"<td> "+bankAgenctNo+"</td>"+	
					"<td>"+MPeasAgenctNo+" </td>"+ 
					"<td> "+airtelMoneyAgenetNo+"</td>"+
					"<td> "+orangeMoneyAgentNo+"</td>"+
					"<td> "+mpesaTillNumber+"</td>"+
					"</tr>";
					
					$("#tbody_data3").append(appendTxt);	  
					rowindex = ++rowindex;
					colindex = ++ colindex; 
			}
	}
	

});
 
	
</script> --%>

<script type="text/javascript">

 
function createMerchant(){ 
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/merchantCreateConfirmAct.action';
	$("#form1").submit();
	 
}

function getPreviousScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/merchantCreateBackAct.action';
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
				  <li><a href="#">Add New Merchant Confirmation</a></li>
				</ul>
			</div>
				<div class="row-fluid sortable">
					<div class="box span12">
						 <table height="3">
							 <tr>
								<td colspan="3">
									<div class="messages" id="messages"><s:actionmessage /></div>
									<div class="errors" id="errors"><s:actionerror /></div>
								</td>
							</tr>
						 </table>
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
								<td ><label for="Store ID"><strong>Store ID</strong></label></td>
								<td > <s:property value="storeId"/>
									<input name="storeId"  type="hidden" id="storeId" class="field"  value="<s:property value="storeId"/>" > 
								</td>
								<td><label for="Store Name"><strong>Store Name</strong></label></td>
								<td> <s:property value="storeName"/>
									<input name="storeName" type="hidden" class="field" id="storeName" value="<s:property value="storeName"/>" />
								</td>
										
							</tr>
							<tr class="even">
								<td ><label for="Location"><strong>Location</strong></label></td>
								<td > <s:property value="locationVal"/>
									<input name="location" type="hidden" class="field" id="location" value="<s:property value="location"/>" />
								</td>
								<td ><label for="KRA PIN"><strong>KRA PIN</strong></label></td>
								<td > <s:property value="kraPin"/>
									<input name="kraPin" type="hidden" class="field" id="kraPin" value="<s:property value="kraPin"/>"  />
								</td>	
							</tr> 
							<tr class="odd">
								<td ><label for="Merchant Type"><strong>Merchant Type</strong></label></td>
								<td >
									<s:property value="merchantTypeVal"/>
									<input name="merchantType" type="hidden" class="field" id="merchantType" value="<s:property value="merchantType"/>"  />
								</td>
								<td ><label for="Merchant Type"><strong>Member Type</strong></label></td>
								<td >
									<s:property value="memberType"/>
									<input name="memberType" type="hidden" class="field" id="memberType" value="<s:property value="memberType"/>"  />
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
												<td width="20%"><strong><label for="Manager Name">Manager Name</label></strong></td>
												<td width="30%"><s:property value="managerName"/>
													<input name="managerName" type="hidden" id="managerName" class="field" value="<s:property value="managerName"/>" maxlength="50" >
												</td>
												<td width="20%"><strong><label for="Email">Email</label></strong></td>
							                    <td width="30%"> <s:property value="email"/>
													<input name="email" type="hidden"  id="email" class="field"  value="<s:property value="email"/>" >
												</td>
											</tr>
											<tr class="odd">
												<td ><strong><label for="Address Line 1">Address Line 1</label></strong></td>
												<td > <s:property value="addressLine1"/>
													<input name="addressLine1" id="addressLine1" class="field" type="hidden"  maxlength="50" value="<s:property value="addressLine1"/>"  >
												</td>
												<td ><strong><label for="Address Line 2 ">Address Line 2 </label></strong></td>
												<td > <s:property value="addressLine2"/>
													<input name="addressLine2" type="hidden" class="field" id="addressLine2"   value="<s:property value="addressLine2"/>" />
												</td>
											</tr>
											<tr class="even">
												<td ><strong><label for="Address Line 3 ">Address Line 3</label></strong></td>
												<td > <s:property value="addressLine3"/>
													<input name="addressLine3" type="hidden" class="field" id="addressLine3" value="<s:property value="addressLine3"/>" />
												</td>
												<td><strong><label for="City">City</label></strong></td>
												<td > <s:property value="city"/>
													<input name="city" type="hidden" class="field" id="city"  value="<s:property value="city"/>" />
												</td>
											</tr>
											<tr class="odd">
												<td ><strong><label for="P.O. Box Number ">P.O. Box Number</label></strong></td>
												<td > <s:property value="poBoxNumber"/>
													<input name="poBoxNumber" type="hidden" id="poBoxNumber" class="field" value="<s:property value="poBoxNumber"/>" />
												</td>
												<td ><strong><label for="Telephone Number 1">Telephone Number 1</label></strong></td>
												<td> <s:property value="telephoneNumber1"/>
													<input name="telephoneNumber1" type="hidden" id="telephoneNumber1" class="field" value="<s:property value="telephoneNumber1"/>" />
												</td>
											</tr>
							               <tr class="even">
												<td ><strong><label for="Telephone Number 2 ">Telephone Number 2</label></strong>	</td>
												<td > <s:property value="telephoneNumber2"/>
													<input name="telephoneNumber2" type="hidden" class="field" id="telephoneNumber2"  value="<s:property value="telephoneNumber2"/>" />
												</td>
												<td ><strong><label for="Mobile Number">Mobile Number</label></strong></td>
												<td > <s:property value="mobileNumber"/>
													<input name="mobileNumber" id="mobileNumber" class="field" type="hidden"  value="<s:property value="mobileNumber"/>" />
												</td>
										   </tr>
							               <tr class="odd">
												<td ><strong><label for="Fax Number ">Fax Number </label></strong></td>
												<td > <s:property value="faxNumber"/>
													<input name="faxNumber"  type="hidden" class="field" id="faxNumber" value="<s:property value="faxNumber"/>"  />
												</td>
												<td ><!-- <strong><label for="Contact Name">Contact Name</label></strong> --></td>
												<td ><!-- <input name="contactName" type="text" id="contactName" class="field" value="${contactName}"  > --> </td>
										   </tr>
										   	<tr class="even">
											<td ><strong><label for="Primary Contact Person">Primary Contact Person</label></strong></td>
											<td > <s:property value="prmContactPerson"/>
												<input name="prmContactPerson" id="prmContactPerson" class="field" type="hidden" value="<s:property value="prmContactPerson"/>" >
											</td>
											<td><strong><label for="Primary Contact Number">Primary Contact Number</label></strong></td>
											<td > <s:property value="prmContactNumber"/>
												<input name="prmContactNumber" type="hidden" class="field" id="prmContactNumber" value="<s:property value="prmContactNumber"/>" />
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
						 
							<input type="hidden" name="bankAcctMultiData" id="bankAcctMultiData" value="<s:property value="bankAcctMultiData"/>"></input> 
							<div id="" class="box-content">
								<fieldset> 
									<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
											id="bankAcctData" >
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
										 	<s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
										<s:param name="jsonData" value="%{bankAcctMultiData}"/>  
										<s:param name="searchData" value="%{'#'}"/>  
									</s:bean> 
									<s:iterator value="#jsonToList.data" var="mulData"  status="mulDataStatus" >   
									 
												<s:if test="#mulDataStatus.even == true" > 
													<tr class="even" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
												</s:if>
												<s:elseif test="#mulDataStatus.odd == true">
													<tr class="odd" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
												</s:elseif> 
											<td><s:property value="#mulDataStatus.index+1" /></td>
												<s:generator val="%{#mulData}"
													var="bankDat" separator="," >  
													<s:iterator status="itrStatus"> 
														<s:if test="#itrStatus.index != 5" > 
															<td><s:property /></td>
														</s:if>
													</s:iterator>  
												</s:generator> 
										</tr>
									</s:iterator>
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
								<input type="hidden" name="agentMultiData" id="agentMultiData" value="<s:property value="agentMultiData"/>"></input>
								
								<table width="100%" class="table table-striped table-bordered bootstrap-datatable " 
										id="documentData">
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
										 <s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
												<s:param name="jsonData" value="%{agentMultiData}"/>  
												<s:param name="searchData" value="%{'#'}"/>  
											</s:bean> 
											<s:iterator value="#jsonToList.data" var="mulData"  status="mulDataStatus" >   
														 
														<s:if test="#mulDataStatus.even == true" > 
															<tr class="even" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
														</s:if>
														<s:elseif test="#mulDataStatus.odd == true">
															<tr class="odd" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
														</s:elseif> 
												
													<td><s:property value="#mulDataStatus.index+1" /></td>
														<s:generator val="%{#mulData}"
															var="bankDat" separator="," >  
															<s:iterator status="itrStatus">  
																<td><s:property /></td> 
															</s:iterator>  
														</s:generator> 
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
									<i class="icon-edit"></i>Document Information
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div>  
							<div id="documentInformation" class="box-content">
								<input type="hidden" name="documentMultiData" id="documentMultiData" value="<s:property value="documentMultiData"/>"></input>
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
											 <s:bean name="com.ceva.base.common.bean.JsonDataToObject" var="jsonToList">
												<s:param name="jsonData" value="%{documentMultiData}"/>  
												<s:param name="searchData" value="%{'#'}"/>  
											</s:bean> 
											<s:iterator value="#jsonToList.data" var="mulData"  status="mulDataStatus" >   
														 
														
														<s:if test="#mulDataStatus.even == true" > 
															<tr class="even" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
														</s:if>
														<s:elseif test="#mulDataStatus.odd == true">
															<tr class="odd" align="center" id="tr-<s:property value="#mulDataStatus.index" />" index="<s:property value="#mulDataStatus.index" />">
														</s:elseif> 
												
													<td><s:property value="#mulDataStatus.index+1" /></td>
														<s:generator val="%{#mulData}"
															var="bankDat" separator="," >  
															<s:iterator status="itrStatus">  
																	<td><s:property /></td> 
															</s:iterator>  
														</s:generator> 
												</tr>
											</s:iterator>
											 </tbody>
									</table>
							</div>
						 </div>
					</div> 
		<div class="form-actions">
				<a  class="btn btn-danger" href="#" onClick="getPreviousScreen()">Back</a>
				<a  class="btn btn-success" href="#" onClick="createMerchant()">Save</a>
		</div> 
</div>
</form>
</body>
</html>
