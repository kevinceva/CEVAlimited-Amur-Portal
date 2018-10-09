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
	var SubServiceCode='${responseJSON.serviceCode}'+'-'+'${responseJSON.subServiceCode}';
	$("#subServiceCode").val(SubServiceCode);
	var mydata ='${responseJSON.SubServiceList}';
    	var json = jQuery.parseJSON(mydata);
    	$.each(json, function(i, v) {
    	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
    	    $('#subServiceName').append(options);
    	});
});
function getServiceScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/serviceMgmtAct.action';
	$("#form1").submit();
	return true;
}
var enterserviceCode = "Please enter Service Code";
var enterserviceName = "Please enter Service Name";
var entersubserviceCode = "Please enter Sub Service Code";
var entersubserviceName = "Please select Sub Service Name";

var serviceCoderules = {
	required : true
};
var serviceNamerules = {
	required : true
};
var subServiceCoderules = {
	required : true
};
var subServiceNamerules = {
	required : true
};

var serviceCodemessages = {
	required : enterserviceCode
};
var serviceNamemessgaes = {
	required : enterserviceName
};
var subServiceCodemessages = {
	required : entersubserviceCode
};
var subServiceNamemessages = {
	required : entersubserviceName
};

var subServiceCreateRules= {
	rules : {
		serviceCode : serviceCoderules,
		serviceName : serviceNamerules,
		subServiceCode : subServiceCoderules,
		subServiceName : subServiceNamerules
	},
	messages : {
		serviceCode : serviceCodemessages,
		serviceName : serviceNamemessgaes,
		subServiceCode : subServiceCodemessages,
		subServiceName : subServiceNamemessages
	}
};
function createSubService(){
	
	$("#form1").validate(subServiceCreateRules);
	if($("#form1").valid()){	
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/subServiceCreateSubmitAct.action';
		$("#form1").submit();
		return true;
	}else{
			return false;
	}

}

function getData(){
	var data=$("#subServiceName option:selected").text();
	$("#subServiceText").val(data);
}

</script>
 
</head>

<body>
	<form name="form1" id="form1" method="post" action=""> 
		<div id="content" class="span10"> 
		    <div>
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="#">Fee Management</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">Create Sub Service</a></li>
				</ul>
			</div>
		<div class="row-fluid sortable"><!--/span-->
			<div class="box span12"> 
					<div class="box-header well" data-original-title>
						<i class="icon-edit"></i>Create Sub Service
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
									<td ><strong><label for="Service ID">Service Code<font color="red">*</font></label></strong></td>
									<td><input name="serviceCode" type="text" id="serviceCode" class="field" value="${responseJSON.serviceCode}" readonly ></td>
								</tr>
								<tr class="odd">
									<td ><strong><label for="Service Name">Service Name<font color="red">*</font></label></strong></td>
									<td><input name="serviceName" type="text"  id="serviceName" class="field"  value="${responseJSON.serviceName}" readonly></td>

								</tr>
								<tr class="even">
									<td ><strong><label for="Sub Service Code">Sub Service Code<font color="red">*</font></label></strong></td>
									<td ><input name="subServiceCode"  type="text" id="subServiceCode" class="field" value="${subServiceCode}"   readonly > </td>
								</tr>
								<tr class="odd">
									<td><strong><label for="Sub Service Name">Sub Service Name<font color="red">*</font></label></strong></td>
									<td>
										<select id="subServiceName" name="subServiceName" class="field" onChange="getData()">
											<option value="">Select</option>
										</select>
									</td>
								</tr>
							</table>
							</fieldset>
					</div>
						<input name="subServiceText" type="hidden" id="subServiceText" class="field"  />
			</div>
		</div>
	<div class="form-actions">
		<a  class="btn btn-danger" href="#" onClick="getServiceScreen()">Back</a> &nbsp;&nbsp;
		<a  class="btn btn-danger" href="#" onClick="createSubService()">Submit</a>
	</div> 
</div>	
</form>
</body>
</html>
