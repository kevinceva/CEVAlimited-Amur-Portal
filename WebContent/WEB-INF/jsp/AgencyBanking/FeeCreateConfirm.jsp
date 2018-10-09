
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 <script type="text/javascript" >
$(document).ready(function() {
	var data="${responseJSON.flatPercent}";
	if(data=="F")
		$("#floatPercentspan").append("Flat");
	if(data=="P")
		$("#floatPercentspan").append("Percent");
	var data="${responseJSON.serviceType}";
	if(data=="Y"){
		$("#serviceTypespan").append("Yes");
		$("#service1").show();
		$("#service2").show();
		$("#service3").show();
		$("#service4").show();
	}
	if(data=="N"){
		$("#serviceTypespan").append("No");
		$("#service1").hide();
		$("#service2").hide();
		$("#service3").hide();
		$("#service4").hide();
	}	
	
	var slabFrom="${responseJSON.slabFrom}";
	var slabTo="${responseJSON.slabTo}";
	//alert(slabFrom.length);
	if(slabFrom.length==0 || slabTo.length==0){
			$("#TransData").hide();
	}
	var val = 1;
	var rowindex = 1;
	var colindex = 0;
	var bankfinalData="${responseJSON.accountMultiData}";
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

	
	for(var i=0;i<bankfinalDatarows.length;i++){
		var eachrow=bankfinalDatarows[i];
		var eachfieldArr=eachrow.split(",");
		var partnerAccount=eachfieldArr[0];
		var amount=eachfieldArr[1];
			var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
			"<td>"+rowindex+"</td>"+
			"<td><input type='hidden' name='frequencies' value='"+partnerAccount+"' />"+partnerAccount+"</td>"+	
			"<td><input type='hidden' name='dateTime' value='"+amount+"' />"+amount+" </td>"+ 
			"</tr>";
			$("#tbody_data").append(appendTxt);	  
		rowindex = ++rowindex;
		colindex = ++ colindex; 
	} 
	
	var feeFor="${responseJSON.feeFor}";
	if(feeFor=="CUS"){
		$("#feeForlab").text("Customer");
	}
	else if(feeFor=="REC"){
		$("#feeForlab").text("Receipent");
	}else if(feeFor=="ONS"){
		$("#feeForlab").text("On Share");
	}else {
		$("#feeForlab").text(" ");
	}
	
}); 

function getServiceScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/serviceMgmtAct.action';
	$("#form1").submit();
	return true;
}
function createSubService(){ 
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/insertFeeAct.action';
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
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt;  </span> </li>
			  <li> <a href="#">Fee Management</a> <span class="divider"> &gt;&gt;  </span></li>
			  <li><a href="#">Create Fee Cofirmation</a></li>
			</ul>
		</div>
		<div class="row-fluid sortable"><!--/span--> 
			<div class="box span12"> 
				 
				<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>Create Fee Confirmation
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
										<td width="20%"><strong><label for="Service ID">Service Code</label></strong></td>
										<td width="30%"> ${responseJSON.serviceCode}
											<input name="serviceCode" type="hidden" id="serviceCode" class="field" value="${responseJSON.serviceCode}"  >
										</td>
										<td width="20%"><strong><label for="Service Name">Service Name</label></strong></td>
										<td width="30%"> ${responseJSON.serviceName}
											<input name="serviceName" type="hidden"  id="serviceName" class="field"  value="${responseJSON.serviceName}" >
										</td>
									</tr>
									<tr class="odd">
										<td ><strong><label for="Sub Service Code">Sub Service Code</label></strong></td>
										<td > ${responseJSON.subServiceCode}
											<input name="subServiceCode"  type="hidden" id="subServiceCode" class="field" value="${responseJSON.subServiceCode}">
										</td>
										<td><strong><label for="Sub Service Name">Sub Service Name</label></strong></td>
										<td> ${responseJSON.subServiceName}
											<input name="subServiceName"  type="hidden" id="subServiceName" class="field" value="${responseJSON.subServiceName}">
										</td>
									</tr>
									<tr class="even">
										<td ><strong><label for="Fee Code">Fee Code</label></strong></td>
										<td> ${responseJSON.feeCode}
											<input name="feeCode" type="hidden" id="feeCode" class="field" value="${responseJSON.feeCode}" >
										</td>
										<td ><strong><label for="Fee Code">Flat/Percent</label></strong></td>
										<td>
											 <span id="floatPercentspan"></span>
											<input name="flatPercent"  type="hidden" id="flatPercent" class="field" value="${responseJSON.flatPercent}" >
										</td>
										
											<input type="hidden" name="serviceType" id="serviceType" value="${responseJSON.serviceType}">
										
									</tr>
									<tr class="odd" id="TransData">
										<td ><strong><label for="Slab From">Slab From</label></strong></td>
										<td > ${responseJSON.slabFrom}
											<input name="slabFrom"  type="hidden" id="slabFrom" class="field" value="${responseJSON.slabFrom}" > 
										</td>
										<td><strong><label for="Slab To">Slab To</label></strong></td>
										<td>  ${responseJSON.slabTo}
											<input name="slabTo"  type="hidden" id="slabTo" class="field" value="${responseJSON.slabTo}" >
										</td>
									</tr> 
									<tr class="odd">
										<td id="service1">
											<strong><label for="Service">Huduma Service</label></strong>
										</td>
										<td id="service2">
											 ${responseJSON.hudumaServiceName}
											<input name="service"  type="hidden" id="service" class="field" value="${responseJSON.service}">
											<input name="hudumaServiceName"  type="hidden" id="hudumaServiceName" class="field" value="${responseJSON.hudumaServiceName}">
										</td>
										<td id="service3">
											<strong><label for="Service">Huduma Sub Service</label></strong>
											<input name="hudumaSubService"  type="hidden" id="hudumaSubService" class="field" value="${responseJSON.hudumaSubService}">
											<input name="hudumaSubServiceName"  type="hidden" id="hudumaSubServiceName" class="field" value="${responseJSON.hudumaSubServiceName}">
										</td>
										<td id="service4">
											${responseJSON.hudumaSubServiceName}
										</td>
									</tr> 
									<tr class="odd">
								<td >
									<label for="Service">Fee For</label>
								</td>
								<td colspan="3">
									 <span id="feeForlab"></span>
											<input name="feeFor"  type="hidden" id="feeFor" class="field" value="${responseJSON.feeFor}">
								</td>
							</tr>
							</table>
						</fieldset>
					</div>
					
					<div id="primaryDetails" class="box-content">
						<fieldset>
						<input type="hidden" name="accountMultiData" id="accountMultiData" value="${responseJSON.accountMultiData}" ></input>							
							<table width="100%" class="table table-striped table-bordered bootstrap-datatable "  id="bankData">
							  <thead>
									<tr>
										<th>Sno</th>
										<th>Partner Account</th>
										<th>Percent/Falt Amount</th>
									</tr>
							  </thead>    
							 <tbody  id="tbody_data">
							 </tbody>
							</table> 
						</fieldset>
					</div>
				</div>
			</div>  
		<div class="form-actions"> 
			<a  class="btn btn-danger" href="#" onClick="createSubService()">Confirm</a>
		</div> 
	</div>
</form>
</body>
</html>
