
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
<script type="text/javascript" >
$(document).ready(function() {
    
	var serviceCode='${responseJSON.serviceCode}';
	if(serviceCode.length==1)
		serviceCode="S00000"+serviceCode;
	if(serviceCode.length==2)
		serviceCode="S0000"+serviceCode;
	if(serviceCode.length==3)
		serviceCode="S000"+serviceCode;
	if(serviceCode.length==4)
		serviceCode="S00"+serviceCode;
		
	$("#serviceCode").val(serviceCode);
	
	var mydata ='${responseJSON.SERVICE_LIST}';
    	var json = jQuery.parseJSON(mydata);
    	$.each(json, function(i, v) {
    	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
    	    $('#serviceName').append(options);
    	});
		
		var mydata ='${responseJSON.SettlementAcctList}';
    	var json = jQuery.parseJSON(mydata);
    	$.each(json, function(i, v) {
    	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
    	    $('#settlementAccount').append(options);
    	});
		
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
});
 

function getServiceScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/serviceMgmtAct.action';
	$("#form1").submit();
	return true;
}

var enterserviceCode = "Please enter Service Code";
var enterserviceName = "Please select Service Name";
var selectSettlementAccount= "Please select Settlement Account";
var serviceCoderules = {
	required : true
};
var serviceNamerules = {
	required : true
};
var settlementAccountrules = {
	required : true
};

var serviceCodemessages = {
	required : enterserviceCode
};
var serviceNamemessgaes = {
	required : enterserviceName
};

var settlementAccountmessages = {
	required : selectSettlementAccount
};

var serviceCreateRules= {
	rules : {
		serviceCode : serviceCoderules,
		serviceName : serviceNamerules,
		settlementAccount : settlementAccountrules
	},
	messages : {
		serviceCode : serviceCodemessages,
		serviceName : serviceNamemessgaes,
		settlementAccount : settlementAccountmessages
	}
};

function createNewService(){
	$("#form1").validate(serviceCreateRules);
	if($("#form1").valid()){	
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/serviceCreateSubmitAct.action';
		$("#form1").submit();
		return true;
	}else{
			return false;
	}
}
function getServiceName(){
	 $("#serviceNameDesc").val( $( "#serviceName option:selected" ).text()); 
}

function getSettlementName(){
	  $("#settlementAccountDesc").val( $( "#settlementAccount option:selected" ).text());
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
			  <li><a href="#">Create New Service</a></li>
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
					<i class="icon-edit"></i>New  Service
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					</div>
				</div>
				<div id="primaryDetails" class="box-content">
					<fieldset> 	
						<table width="950" border="0" cellpadding="5" cellspacing="1" 
							class="table table-striped table-bordered bootstrap-datatable ">
							<tr class="odd">
								<td ><strong><label for="Service ID">Service Code<font color="red">*</font></label></strong></td>
								<td ><input name="serviceCode" type="text" id="serviceCode" class="field"  maxlength="15"  value='${responseJSON.serviceCode}' readonly></td>
							</tr>
							<tr class="even">
								<td ><strong><label for="Service Name">Service Name<font color="red">*</font></label></strong></td>
								<td>
									<select id="serviceName" name="serviceName" data-placeholder="Choose User Designation..." 
										class="chosen-select" style="width: 420px;" required=true  onChange="getServiceName()">
												<option value="">Select</option>
									</select>
								</td>
							</tr>
							<!-- <tr class="odd">
								<td ><strong><label for="Service Name">Settlement Account<font color="red">*</font></label></strong></td>
								<td>
									<select id="settlementAccount" name="settlementAccount"  data-placeholder="Choose User Designation..." 
								class="chosen-select" style="width: 220px;" required=true onChange="getSettlementName()">
												<option value="">Select</option>
									</select>
								</td>
							</tr> -->
							<input type="hidden" name="serviceNameDesc" id="serviceNameDesc" value="">
							<input type="hidden" name="settlementAccountDesc" id="settlementAccountDesc" value="">
						</table>
					</fieldset> 
				</div> 
			 </div>
		 </div> 
		<div class="form-actions">
			<a  class="btn btn-info" onClick="getServiceScreen()">Back</a> &nbsp;&nbsp;
			<a  class="btn btn-success" onClick="createNewService()">Submit</a>
		</div> 
	</div>
</form> 
 </body>
</html>
