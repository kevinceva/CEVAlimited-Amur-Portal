
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
	var mydata ='${responseJSON.ReferenceNoList}';
    	var json = jQuery.parseJSON(mydata);
    	$.each(json, function(i, v) {
    	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
    	    $('#storeCreditRefNo').append(options);
    	});
});
 

function getGenerateLimitScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/creditManagementAct.action';
	$("#form1").submit();
	return true;
}

var storeCreditRules = {
   rules : {
    terminalCreditAmt : { required : true },
	storeCreditRefNo : { required : true }
   },  
   messages : {
    terminalCreditAmt : { 
       required : "Please enter Terminal Credit Amount."
        },
	storeCreditRefNo : { 
       required : "Please select Store Credit Reference No."
        }
   } 
  };
  
  
function createTerminal(){ 
	$("#form1").validate(storeCreditRules);
	if($("#form1").valid()){	
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/getCreateTerminalCreditConfirmAct.action';
		$("#form1").submit();
		return true;
	}else{
		return false;
	}
}	
</script>
</head>
<body>
	<form name="form1" id="form1" method="post" action="">		
		<div id="content" class="span10">
		    <div>
				<ul class="breadcrumb">
				 	<li><a href="#">Home</a> <span class="divider"> > </span></li>
					<li><a href="#">Float Management</a> <span class="divider"> > </span></li>
					<li><a href="#">Credit Management</a> <span class="divider"> > </span></li>
					<li><a href="#">Terminal Credit Create</a></li>
				</ul>
			</div>
			
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
								<table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable ">
										<tr class="even">
											<td width="20%"><strong><label for="Merchant ID">Merchant ID</label></strong></td>
											<td width="30%"> ${responseJSON.merchantID}
												<input name="merchantID" type="hidden" id="merchantID" class="field" value="${responseJSON.merchantID}">
											</td>
											<td width="20%"><strong><label for="Store ID">Store ID</label></strong></td>
											<td width="30%"> ${responseJSON.storeId}
												<input name="storeId"  type="hidden" id="storeId" class="field"  value="${responseJSON.storeId}"  >
											</td>
										</tr>
										<tr class="odd">
											<td><strong><label for="Terminal ID">Terminal ID</label></strong></td>
											<td> ${responseJSON.terminalID}
												<input name="terminalID" type="hidden"  id="terminalID" class="field" value="${responseJSON.terminalID}"  maxlength="8">
											</td>
											<td ><strong><label for="Terminal Make">Terminal Make </label></strong></td>
											<td > ${responseJSON.terminalMake}
												<input name="terminalMake" type="hidden" class="field" id="terminalMake" value="${responseJSON.terminalMake}"  />
											</td>
													
										</tr>
										<tr class="even">
											<td ><strong><label for="Model Number ">Model Number</label></strong></td>
											<td > ${responseJSON.modelNumber}
												<input name="modelNumber"  type="hidden" id="modelNumber" class="field" value="${responseJSON.modelNumber}"  />
											</td>	
											<td><strong><label for="Serial Number">Serial Number</label></strong></td>
											<td > ${responseJSON.serialNumber}
												<input name="serialNumber" id="serialNumber" class="field" value="${responseJSON.serialNumber}" type="hidden"  />
											</td>	
										</tr>
										<tr class="odd">
											<td ><strong><label for="Status">TPK Status</label></strong></td>
											<td> ${responseJSON.status}
												<input name="status" id="status" class="field" value="${responseJSON.status}" type="hidden"  />
												
											</td>
											<td><strong><label for="date">TPK Date</label></strong></td>
											 <td > ${responseJSON.TERMINAL_DATE}
												<input name="terminalDate"  type="hidden" id="terminalDate" class="field"  value="${responseJSON.TERMINAL_DATE}"   > 
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
								<i class="icon-edit"></i>Credit Details
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							</div>
						</div>
						<div id="limitDetails" class="box-content">
							<fieldset>  
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable " >
										<tr class="even">
											<td width="20%">Store Credit Reference No</td>
											<td width="30%">
												<select id="storeCreditRefNo" name="storeCreditRefNo" class="field"  required='true' >
													<option value="">Select</option>
												</select>
											</td>
											<td width="20%"><strong><label for="Store Limit">Terminal Credit Amount<font color="red">*</font></label></strong></td>
											<td width="30%"><input name="terminalCreditAmt" type="text" id="terminalCreditAmt" class="field" value="${responseJSON.terminalCreditAmt}"  required='true' ></td>
										</tr> 
								</table>
							</fieldset>  
						</div> 
					</div>
			</div>
	<div class="form-actions">
		<a  class="btn btn-primary" href="#" onClick="getGenerateLimitScreen()">Back</a> &nbsp;&nbsp;
		<a  class="btn btn-success" href="#" onClick="createTerminal()">Submit</a>
	</div>	
</div> 
 </form>
</body>
</html>
