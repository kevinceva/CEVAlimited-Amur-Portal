
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

 function getServiceScreen(){
 	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/serviceMgmtAct.action';
 	$("#form1").submit();
 	return true;
 }


 var createFeeRules = {
    rules : {
    referenceNo : { required : true },
 	merchantId : { required : true },
 	storeId : { required : true },
	terminalId : { required : true },
	amount : { required : true }
    },  
    messages : {
     referenceNo : { 
        required : "Reference No Missing"
         },
 	merchantId : { 
        required : "Please select Merchant Id"
         },
 	storeId : {
 		required : "Please select Store Id"
 	},
	terminalId : {
 		required : "Please select Terminal Id"
 	},
	amount : {
 		required : "Please enter amount"
 	}
    } 
  };

 function createRefund(){
 	$("#form1").validate().cancelSubmit = true;
 	accountFinalData=accountFinalData.slice(1);
 	if(accountFinalData=="") {
 		alert("Please add alteast one record");
 		return false;
 	}else{
 		$("#accountMultiData").val(accountFinalData);
 		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/refundCreateAct.action';
 		$("#form1").submit();
 		return true;
 	}
 			
 }

 function getStores(){
 	var merchantId=$("#merchantId option:selected" ).val();
  	var formInput='merchantId='+merchantId+'&method=getStoreDetails';
 	   $.getJSON('getStoreListAct.action', formInput,function(data) {
 	     	var json = data.responseJSON.STORE_LIST;
			 document.form1.storeId.options.length=1;
 	    	$.each(json, function(i, v) {
				// create option with value as index - makes it easier to access on change
				var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i); 
				$('#storeId').append(options);	
			});
			
 		});
 }	
 
 function getTerminals(){
 	var storeId=$("#storeId option:selected" ).val();
  	var formInput='storeId='+storeId+'&method=getTerminalDetails';
 	   $.getJSON('getTerminalListAct.action', formInput,function(data) {
 	     	var json = data.responseJSON.TERMINAL_LIST;
			  document.form1.terminalId.options.length=1;
 	    	$.each(json, function(i, v) {
				// create option with value as index - makes it easier to access on change
				var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
				// append the options to job selectbox
				//console.log(options);
				$('#terminalId').append(options);	
			});
			$("#StoreRecovery").show();
			$("#storeRecoveryAmt").text("");
			$("#storeRecoveryAmt").text(data.responseJSON.RECOVERY_AMT);
			
 		});
 }	
 function getTerminalAmt(){
 	var terminalId=$("#terminalId option:selected" ).val();
  	var formInput='terminalId='+terminalId+'&method=getTerminalAmt&task=getLimit';
 	   $.getJSON('getTerminalAmtAct.action', formInput,function(data) {
			$("#terminalRecovery").show();
			$("#terminalAmt").text("");
			$("#terminalAmt").text(data.responseJSON.TERMINAL_AMT);
			$("#terminalLimit").val(data.responseJSON.TERMINAL_AMT);
 		});
 }	

 function getSubService(){
 	var data=$("#hudumaSubService option:selected" ).text();
 		$("#hudumaSubServiceName").val(data);
 } 

var val = 1;
var rowindex = 1;
var colindex = 0;
var accountFinalData="";
 	
$(document).ready(function() {
	
	$("#StoreRecovery").hide();
	$("#terminalRecovery").hide();
	var mydata ='${responseJSON.MERCHANT_LIST}';
	var json = jQuery.parseJSON(mydata);
	$.each(json, function(i, v) {
		// create option with value as index - makes it easier to access on change
		var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);    
		// append the options to job selectbox
		$('#merchantId').append(options);
	});
	
	  $('#addCap').live('click', function () {
		var referenceNo = $('#referenceNo').val() == undefined ? ' ' : $('#referenceNo').val();
		var merchantId = $('#merchantId').val() == undefined ? ' ' : $('#merchantId').val();
		var storeId = $('#storeId').val() == undefined ? ' ' : $('#storeId').val();
		var terminalId = $('#terminalId').val() == undefined ? ' ' : $('#terminalId').val();
		var amount = $('#amount').val() == undefined ? ' ' : $('#amount').val();
		var terminalLimit = $('#terminalLimit').val() == undefined ? ' ' : $('#terminalLimit').val();
		
		$("#form1").validate(createFeeRules);
		if($("#form1").valid()) { 	
			var eachrow=referenceNo+","+merchantId+","+storeId+","+terminalId+","+amount;				
			var addclass = "";
				 
			if(val % 2 == 0 ) {
				addclass = "even";
				val++;
			}
			else {
				addclass = "odd";
				val++;
			}  
			
			if(amount>terminalLimit){
				alert("Amount should not be grater than Max Refund Amount");
				return false;
			}
			var appendTxt = "<tr class="+addclass+" index='"+rowindex+"'> "+
								"<td>"+rowindex+"</td>"+
								"<td>"+merchantId+" </td>"+ 
								"<td>"+storeId+" </td>"+ 
								"<td>"+terminalId+" </td>"+ 
								"<td>"+amount+" </td>"+ 
								"<td class='center '> <a class='btn btn-min btn-info' href='#' id='editDat'> <i class='icon-edit icon-white'></i></a>  <a class='btn btn-min btn-danger' href='#' id='deleteemail'> <i class='icon-trash icon-white'></i> </a></td></tr>";
									
								$("#tbody_data2").append(appendTxt);
								accountFinalData=accountFinalData+"#"+eachrow;
								$("#merchantId").val("");
								$("#storeId").val("");
								$("#terminalId").val("");
								$("#amount").val("");										
									
		
			colindex = ++ colindex; 
			rowindex = ++rowindex;
		}else{
			return false;
		}
	}); 
	
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
<link rel="shortcut icon" href="images/favicon.ico">

</head>

<body>
	<form name="form1" id="form1" method="post" action="">
	<!-- topbar ends --> 
		
		<div id="content" class="span10">  
			 
			    <div>
					<ul class="breadcrumb">
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li><a href="#">Refund Management</a></li>
					</ul>
				</div>
				
		<div class="row-fluid sortable"><!--/span--> 
			<div class="box span12">
				<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>Create Refund
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					</div>
				</div> 
				<div id="primaryDetails" class="box-content">
					<fieldset> 
						 <table width="950" border="0" cellpadding="5" cellspacing="1" 
							class="table table-striped table-bordered bootstrap-datatable" >
								<tr class="odd">
									<td width="20%"><label for="Service ID"><strong>Reference No<font color="red">*</font></strong></label></td>
									<td width="30%">
										${responseJSON.referenceNo}
										<input type="hidden" name="referenceNo" id="referenceNo" value="${responseJSON.referenceNo}"/>
									</td>
									<td width="20%"></td>
									<td></td>
								</tr>
								<tr class="even">
									<td><label for="Merchant Id"><strong>Merchant Id<font color="red">*</font></strong></label></td>
									<td colspan="3">
										<select id="merchantId" name="merchantId" data-placeholder="Choose Merchant Id..." 
												class="chosen-select" style="width: 220px;" onChange="getStores()">
											<option value="">Select</option>
										</select>
									</td> 
								</tr>
								<tr class="odd">
									<td ><label for="Store Id"><strong>Store Id<font color="red">*</font></strong></label></td>
									<td colspan="3">
										<select id="storeId" name="storeId" data-placeholder="Choose Store Id..." 
												class="chosen-select" style="width: 220px;" onChange="getTerminals()">
											<option value="">Select</option>
										</select>
									</td> 
								</tr>
								<tr class="even">
									<td><label for="Terminal Id"><strong>Terminal Id<font color="red">*</font></strong></label></td>
									<td>
										<select id="terminalId" name="terminalId" data-placeholder="Choose Terminal Id..." 
												class="chosen-select" style="width: 220px;" onChange="getTerminalAmt()">
											<option value="">Select</option>
										</select>
									</td>
									<td id="terminalRecovery"><strong>Max Refund Amount at Terminal</strong></td>
									<td><span id="terminalAmt"></span> </td>
								</tr>
								<tr class="odd">
									<td><label for="Amount"><strong>Amount<font color="red">*</font></strong></label></td>
									<td colspan="3">
										<input name="amount"  type="text" id="amount" class="field" value="${responseJSON.amount}" >
										<input name="terminalLimit"  type="hidden" id="terminalLimit" class="field"  >
									</td> 
								</tr>
								<tr class="even">
									<td colspan="4"><input type="button" class="btn btn-success" name="addCap" id="addCap" value="Add" ></input></td>
								</tr>
						</table> 
					</fieldset>  
				</div>   
					<input type="hidden" name="accountMultiData" id="accountMultiData" value=""></input>
					
					<table class="table table-striped table-bordered bootstrap-datatable " 
							id="accountData">
						<thead>
							<tr >
								<th>Sno</th>
								<th>Merchant Id</th>
								<th>Store Id</th>
								<th>Terminal Id</th>
								<th>Recovery Amount</th>
								<th>Action</th>
							</tr>
						</thead>    
						<tbody  id="tbody_data2">
						</tbody>
					</table> 
				 
			</div><!--/#content.span10-->
		</div><!--/fluid-row--> 
		<div class="form-actions"> 
			<a class="btn btn-info" href="#" onClick="getServiceScreen()">Back</a> &nbsp;&nbsp;
			<a class="btn btn-success" href="#" onClick="createRefund()">Submit</a>
		</div> 
</div>  
		 
</form>
</body>
</html>
