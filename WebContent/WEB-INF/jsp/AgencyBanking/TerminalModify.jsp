<!DOCTYPE html>
<html lang="en">
<%@taglib uri="/struts-tags" prefix="s"%> 
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
	
	/* var mydata ='${responseJSON.MERCHANT_LIST}';
    	//alert(mydata);
    	var json = jQuery.parseJSON(mydata);
    	//alert(json);
    	$.each(json, function(i, v) {
    	    // create option with value as index - makes it easier to access on change
    	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
    	    // append the options to job selectbox
    	    $('#merchantID').append(options);
    	}); */
    	
    	/*  $('#merchantID').trigger("chosen:updated");
	var mydata ='${responseJSON.STORE_LIST}';
    	//alert(mydata);
    	var json = jQuery.parseJSON(mydata);
    	//alert(json);
    	$.each(json, function(i, v) {
    	    // create option with value as index - makes it easier to access on change
    	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
    	    // append the options to job selectbox
    	    $('#storeId').append(options);
    	});
		
    	 $('#storeId').trigger("chosen:updated"); */
    	 
	/* var merchantId = "${responseJSON.merchantID}";
	$('#merchantID option[value="' + merchantId + '"]').prop('selected', true);
	
	var storeId = "${responseJSON.storeId}";
	$('#storeId option[value="' + storeId + '"]').prop('selected', true);  */
	
});
 

function getGenerateMerchantScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/generateMerchantAct.action';
	$("#form1").submit();
	return true;
}

function modifyTerminal(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/modifyTerminalAct.action';
	$("#form1").submit();
	return true;
}

var list = "validFrom,validThru,terminalDate".split(",");
var datepickerOptions = {
			showTime: false,
			showHour: false,
			showMinute: false,
  		dateFormat:'dd-mm-yy',
  		alwaysSetTime: false,
		changeMonth: true,
		changeYear: true
};

$(function() {
	$(list).each(function(i,val){
		$('#'+val).datepicker(datepickerOptions);
	}); 
	  
	  $("#merchantID").change(function(){
	   //var formInput=$("#form1").serialize()+'&merchantId='+$('#merchantID').val();
	   var formInput='merchantId='+$('#merchantID').val();
	   $.getJSON('retriveStoresAct.action', formInput,function(data) {
	     	var json = data.responseJSON.STORE_LIST;
	     	document.form1.storeId.options.length=1;
	    	$.each(json, function(i, v) {
				var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);  
				$('#storeId').append(options);
			});
	   });
	  
	   return false;
	  
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
<s:set value="responseJSON" var="respData"/>
<body>
	<form name="form1" id="form1" method="post" action=""> 
		<div id="content" class="span10">  
			 
			    <div>
					<ul class="breadcrumb">
					  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Modify Terminal</a></li>
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
									<i class="icon-edit"></i>Terminal Information
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
								</div>
							</div> 
							
					<div id="terminalDetails" class="box-content">
						<fieldset>
							<table width="950" border="0" cellpadding="5" cellspacing="1"  
											class="table table-striped table-bordered bootstrap-datatable ">
								<tr class="even">
									<td width="20%"><strong><label for="Merchant ID">Merchant ID</label></strong></td>
									<td width="30%"> 
										<%-- <select  id="merchantID" name="merchantID" data-placeholder="Choose Services to Assign Terminals..." 
												class="chosen-select" style="width: 220px;">
										</select> --%>
										
										<%-- <s:select cssClass="chosen-select"
								              headerKey=""
								              headerValue="Select"
								              list="#respData.MERCHANT_LIST"
								              name="merchantID"
								              value="#respData.MERCHANT_LIST"
								              id="localGovernment" requiredLabel="true"
								              theme="simple"
								              data-placeholder="Choose Government ..."
								               /> --%>
								               ${responseJSON.merchantID}
								               <input name="merchantID" type="hidden"  id="merchantID" class="field" value="${responseJSON.merchantID}"  maxlength="8" readonly>
									</td>
									<td width="20%"><strong><label for="Store ID">Store ID</label></strong></td>
									<td width="30%"> 
										<%-- <select  id="storeId" name="storeId" data-placeholder="Choose Services to Assign Terminals..." 
												class="chosen-select" style="width: 220px;">
										</select> --%>
										
										<%-- <s:select cssClass="chosen-select"
								              headerKey=""
								              headerValue="Select"
								              list="#respData.STORE_LIST"
								              name="storeId"
								              value="#respData.STORE_LIST"
								              id="storeId" requiredLabel="true"
								              theme="simple"
								              data-placeholder="Choose Store ..."
								               /> --%>
								               ${responseJSON.storeId}
								             <input name="storeId" type="hidden"  id="storeId" class="field" value="${responseJSON.storeId}"  maxlength="8" readonly>  
									</td>
								</tr>
								<tr class="odd">
									<td><strong><label for="Terminal ID">Terminal ID</label></strong></td>
									<td> 
										<input name="terminalID" type="text"  id="terminalID" class="field" value="${responseJSON.terminalID}"  maxlength="8" readonly>
									</td>
									<td><strong><label for="Terminal Usage">Terminal Usage</label></strong></td>
									<td >
										<input name="terminalUsage" type="text" id="terminalUsage" class="field" value=" ${responseJSON.terminalUsage}" >
									</td>
											
								</tr>
								<tr class="even">
									<td ><strong><label for="Terminal Make">Terminal Make </label></strong></td>
									<td > 
										<input name="terminalMake" type="text" id="terminalMake" class="field" value=" ${responseJSON.terminalMake}" >
									</td>
									<td ><strong><label for="Model Number ">Model Number</label></strong></td>
									<td > 
											<input name="modelNumber" type="text" id="modelNumber" class="field" value=" ${responseJSON.modelNumber}" >
									</td>		
								</tr>
										
								 <tr  class="odd">
									<td><strong><label for="Serial Number">Serial Number</label></strong></td>
									<td >
										 <input name="serialNumber" type="text" id="serialNumber" class="field" value=" ${responseJSON.serialNumber}" >
									</td>
									<td ><strong><label for="PIN Entry">PIN Entry</label></strong></td>
									<td >
										<input name="pinEntry" type="text" id="pinEntry" class="field" value=" ${responseJSON.pINEntry}" >
									</td>
								</tr>
								
								 <tr  class="even">
									<td><strong><label for="Valid From">Valid From (dd-mon-yyyy)</label></strong></td>
									<td > 
										<input name="validFrom" type="text" id="validFrom" class="field" value=" ${responseJSON.validFrom}" >
									</td>
									<td ><strong><label for="Valid Thru">Valid Thru (dd-mon-yyyy)</label></strong></td>
									<td >
										<input name="validThru" type="text" id="validThru" class="field" value=" ${responseJSON.validThru}" >
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
						<i class="icon-edit"></i>TMK Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

						</div>
					</div> 
					<div id="feeDetails" class="box-content">
						<fieldset>
							 <table width="950" border="0" cellpadding="5" cellspacing="1"  
									class="table table-striped table-bordered bootstrap-datatable " >
								<tr class="even">
									<td width="20%"><strong><label for="Status">Status</label></strong></td>
									<td colspan=3>
										<select  id="status" name="status" data-placeholder="Choose Services to Assign Terminals..." class="chosen-select" style="width: 220px;">
											<option value="">Select</option>
											<option value="Active" selected>Active</option>
											<option value="Close">Close</option>
										</select>
									</td>
								</tr>
								<tr class="odd">
									<td><strong><label for="date">Date</label></strong></td>
									 <td > 
										<input name="terminalDate" type="text" id="terminalDate" class="field" value=" ${responseJSON.TERMINAL_DATE}" >
									</td>
								</tr>
							</table>
						</fieldset>
					</div>  
				</div>
			</div>
		<div class="form-actions">
			<a  class="btn btn-danger" href="#" onClick="getGenerateMerchantScreen()">Back</a> &nbsp;&nbsp;
			<a  class="btn btn-success" href="#" onClick="modifyTerminal()">Modify</a>
		</div>	
	</div> 
</form> 
<script src="${pageContext.request.contextPath}/js/jquery.chosen.min.js"></script>
 
</body>
</html>
