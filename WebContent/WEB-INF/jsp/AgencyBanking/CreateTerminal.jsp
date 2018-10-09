
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
   
    var terminalId="${terminalID}";
	var termType=terminalId.substr(0,2);
	if(termType!="PR" || termType!="PU")
		$("#terminalID").val("");
	if(termType=="PR"){
		$("#terminalType").val("Rural");
		$("#terminalID").val(terminalId);
	}else if(termType=="PU"){
		$("#terminalType").val("Urban");
		$("#terminalID").val(terminalId);
	}
	
	$("#terminalType").change(function(){
			var terminalType=document.getElementById("terminalType").value;
				if(terminalType=="Urban"){
					terminalId=terminalId.slice(2);
					terminalId="PU"+terminalId;
					$("#terminalID").val(terminalId);
				}
				else if(terminalType=="Rural"){
					terminalId=terminalId.slice(2);
					terminalId="PR"+terminalId;
					$("#terminalID").val(terminalId);
					
				}else {
					$("#terminalID").val("");
				}
	});
	
	var tpkIndex="${responseJSON.tmkIndex}";
	if(tpkIndex==""){
		tpkIndex="${tpkIndex}";
	}
	$("#tpkIndex").val(tpkIndex);
	$("#tmkIndexSpan").empty();
	$("#tmkIndexSpan").append(tpkIndex);
	
	var tpkKey="${responseJSON.tpkKey}";
	if(tpkKey==""){
		tpkKey="${tpkKey}";
	}
	$("#tpkKey").val(tpkKey);
	$("#tpkKeySpan").empty();
	$("#tpkKeySpan").append(tpkKey);
	
});



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
	$("#serialNumber").keypress(function (e) {
	 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
		$("#"+$(this).attr('id')+"_err").html("Digits Only").show().fadeOut("slow");
			   return false;
		}
   }); 
 	
});
var createTerminalRules = {
   rules : {
    merchantID : { required : true },
	storeId : { required : true },
	terminalID : { required : true },
	modelNumber : { required : true },
	serialNumber : { required : true },
	terminalMake : { required : true },
	validFrom : { required : true },
	validThru : { required : true },
	terminalDate : { required : true },
	terminalType : { required : true }
   },  
   messages : {
    merchantID : { 
       required : "Merchant Id Missing"
        },
	storeId : { 
       required : "Store Id Missing"
        },
	terminalID : { 
       required : "Please enter Terminal Id"
        },
	modelNumber : { 
       required : "Please enter Model No"
        },
	serialNumber : { 
       required : "Please enter Serial No"
        },
	terminalMake : { 
       required : "Please enter Terminal Make"
        },
	validFrom : { 
       required : "Please select Valid From"
        },
	validThru : { 
       required : "Please select Valid Thru"
        },
	terminalDate : { 
       required : "Please select Date"
        },
	terminalType : { 
       required : "Please Select Terminal Type to generate Terminal Id"
        }
   } 
 };
  
function getGenerateMerchantScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/generateMerchantAct.action';
	$("#form1").submit();
	return true;
}

function createTerminal(){
	$("#form1").validate(createTerminalRules);
	if($("#form1").valid()){
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/getCreateTerminalConfirmAct.action';
		$("#form1").submit();
		return true;
	}else{
		return false;
	}
}	
</script>
</head>

<body>
	<form name="form1" id="form1" method="post">
		<div id="content" class="span10"> 
		    <div>
					<ul class="breadcrumb">
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt;</span> </li>
					  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Create Terminal</a></li>
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
							
					<div class="box-content" id="terminalDetails"> 
					 <fieldset>   
						<table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable ">
							<tr class="even">
								<td width="20%"><strong><label for="Merchant ID">Merchant ID<font color="red">*</font></label></strong></td>
								<td width="30%"><input name="merchantID" type="text" id="merchantID" class="field" value="${merchantID}" readonly></td>
								<td width="20%"><strong><label for="Store ID">Store ID<font color="red">*</font></label></strong></td>
								<td width="30%"><input name="storeId"  type="text" id="storeId" class="field"  value="${storeId}" readonly  > </td>
							</tr>
							<tr class="odd">
								<td><strong><label for="Terminal Type">Terminal Type<font color="red">*</font></label></strong></td>
								<td>
									<div>
									 <!-- <input name="TType" type="radio" value="Rural" id="TType">Rural
									 <input name="TType" type="radio" value="Urban" id="TType">Urban -->
										<select id="terminalType" name="terminalType" data-placeholder="Choose terminal type..." 
											class="chosen-select" style="width: 220px;">
												<option value="">Select</option>
												<option value="Rural">Rural</option>
												<option value="Urban">Urban</option>
										</select>
									</div>
								</td>
								<td><strong><label for="Terminal ID">Terminal ID<font color="red">*</font></label></strong></td>
								<td><input name="terminalID" type="text"  id="terminalID" class="field" value="${terminalID}"  maxlength="8" readonly></td> 
							</tr> 		
							 <tr  class="even">
								<td ><strong><label for="Model Number ">Model Number<font color="red">*</font></label></strong></td>
								<td ><input name="modelNumber"  type="text" id="modelNumber" class="field" value="${modelNumber}"  /> </td>		
								<td><strong><label for="Serial Number">Serial Number<font color="red">*</font></label></strong></td>
								<td ><input name="serialNumber" id="serialNumber" class="field" value="${serialNumber}" type="text" maxlength="8" /> <span id="serialNumber_err" class="errmsg"> </span></td>
								
							</tr>
							
							<tr class="odd">
								<!-- <td><strong><label for="Terminal Usage">Terminal Usage<font color="red">*</font></label></strong></td>
								<td ><input name="terminalUsage" type="text" class="field" id="terminalUsage" value="${terminalUsage}" /></td> -->
								<td ><strong><label for="Terminal Make">Terminal Make <font color="red">*</font></label></strong></td>
								<td ><input name="terminalMake" type="text" class="field" id="terminalMake" value="${terminalMake}"  />
									<input name="terminalUsage" type="hidden" class="field" id="terminalUsage" value="Yes" /> </td>
								<td></td>
								<td></td>
							</tr>
							
							 <tr  class="even">
								<td><strong><label for="Valid From">Valid From (dd-mon-yyyy)<font color="red">*</font></label></strong></td>
								<td ><input name="validFrom" id="validFrom" class="field" value="${validFrom}" type="text"  /></td>
								<td ><strong><label for="Valid Thru">Valid Thru (dd-mon-yyyy)<font color="red">*</font></label></strong></td>
								<td ><input name="validThru" type="text" class="field" id="validThru"   value="${validThru}" /></td>	
							</tr>
							<!-- <tr  class="odd">
								<td ><strong><label for="PIN Entry">PIN Entry<font color="red">*</font></label></strong></td>
								<td ><input name="pinEntry" type="text" class="field" id="pinEntry"  value="${pinEntry}"  /></td>
								<td></td>
								<td></td>
							</tr> -->
						</table>
						<input name="pinEntry" type="hidden" class="field" id="pinEntry"  value="NO"  />
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
						 <table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable " >
							<tr class="even">
								<td width="20%"><strong><label for="Status">TMK Index<font color="red">*</font></label></strong></td>
								<td width="30%"> <span id="tmkIndexSpan"></span> 
									<input name="tpkIndex"  type="hidden" id="tpkIndex" class="field"  value="${tmkIndex}" readonly  >
								</td>
								<td width="20%"><strong><label for="date">TMK Key<font color="red">*</font></label></strong></td>
								<td width="30%"> <span id="tpkKeySpan"></span>
									<input name="tpkKey"  type="hidden" id="tpkKey" class="field"  value="${tpkKey}" readonly  >
								</td>
							</tr>
							<tr class="odd">
								<td ><strong><label for="Status">Status<font color="red">*</font></label></strong></td>
								<td>
									<select id="status" name="status" data-placeholder="Choose status..." 
												class="chosen-select" style="width: 220px;" required="true" >
												<option value="">Select</option>
												<option value="Active" selected>Active</option>
												<option value="Close">Close</option>
									</select>
								</td>
								<td><strong><label for="date">Date<font color="red">*</font></label></strong></td>
								 <td ><input name="terminalDate"  type="text" id="terminalDate" class="field"  value="${terminalDate}"   > </td>
							</tr>
						</table>
						</fieldset>  
					</div>
				</div>
			</div> 
			<div class="form-actions">
				<a  class="btn btn-danger" href="#" onClick="getGenerateMerchantScreen()">Back</a> &nbsp;&nbsp;
				<a  class="btn btn-success" href="#" onClick="createTerminal()">Submit</a>
			</div>	 
	</div>
 </form>
</body>
</html>
