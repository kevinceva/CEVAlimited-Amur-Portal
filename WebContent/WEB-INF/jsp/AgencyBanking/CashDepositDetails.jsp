
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
 
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
 
 
<script  type="text/javascript"> 

var depositrules= {
		rules : {
			bankid : { required : true },
			accountno : { required : true, digits: true },
			payeername : { 	required : true },
			mobile : { required : true, digits: true  },
			amount : { 	required : true, digits : true },
			paymentmode : { required : true }
		},
		messages : {
			bankid : { required : "Please Select Service." },
			accountno : { required : "Please Enter Account Number." },
			payeername : { required : "Please Enter Senders Name." },
			mobile : { required : "Please Enter Mobile Number." },
			amount : { required : "Please Input Amount." },
			paymentmode : { required : "Please choose Mode Of Payment." } 
		}
	};

var mydata;
var json;

$(function () {
	mydata ='${responseJSON.BANK_LIST}'; 
	json = jQuery.parseJSON(mydata); 
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);
	    $('#bankid').append(options);
	});  
    $('#save').live('click', function () {
 
    		$("#form1").validate(depositrules);
    		if($("#form1").valid()){
				$('#save').attr('disabled','disabled');
    			$('#cash').val($('#paymentmode').val());
	    	    $("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/CashDepositSecurity.action';
				$("#form1").submit();
			return true;
    	}else{
			$('#save').removeAttr('disabled');
			return false;
    	} 

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
    $('#cancel').live('click', function () { 
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/home.action';
		$("#form1").submit();
		return true;

    }); 
    
    $('#amount').on('change', function () {
    	var amount = $(this).val();
    	var bankId = $("#bankid").val();
    	//alert(amount+"---"+bankId);
    	
    	if(bankId=="KCOOKEN"){
    		var queryString = "amount="+amount+"&bankId="+bankId;	
			$.getJSON("fetchBarclaysFeeAct.action", queryString,function(data) { 
				var feeAmount =data.responseJSON.FEE_AMT;
				//alert(feeAmount);
				$("#feeAmount").val("");
				$("#totalAmount").val("");
				$("#feeAmount").val(feeAmount);
	    		$("#totalAmount").val(parseInt(amount)+parseInt(feeAmount));
			});
    	}else{
    		$("#feeAmount").val("0");
    		$("#totalAmount").val(amount);
    	}
    	
    });

});
function changevalue(name) { 
	 var index = $("#bankid")[0].selectedIndex;
	 var key = json[index-1].key; 
	 $("#bankName").val(key);
} 
</script>
</head>
<body >
<form name="form1" id="form1" method="post" action=""> 
	<div id="content" class="span10">  
		<div>
			<ul class="breadcrumb">
				<li>
					<a href="home.action">Home</a> <span class="divider">&gt;&gt;</span>
				</li>
				<li><a href="#">Cash Deposit</a> 	</li>

			</ul>
		</div>

		<table height="3">
			<tr>
				<td colspan="3">
					<div class="messages" id="messages">
						<s:actionmessage />
					</div>
					<div class="errors" id="errors">
						<s:actionerror />
					</div>
				</td>
			</tr>
		</table>  
	 
			  <div class="row-fluid sortable">

				<div class="box span12">

					<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Cash Deposit Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i
								class="icon-cog"></i></a> <a href="#"
								class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a> 
						</div>
					</div> 
				<div class="box-content">
					<fieldset>  
						<table width="950"  border="0" cellpadding="5" cellspacing="1" 
							class="table table-striped table-bordered bootstrap-datatable " >
							<tr class="even">
								<td width="20%"><label for="Service"><strong>Service<font color="red">*</font></strong></label></td>
								<td width="30%">
									<select id="bankid" name="bankid" onchange="changevalue()"  
										data-placeholder="Choose Bank..." 
									class="chosen-select" style="width: 220px;" required=true>
										<option value="">Select</option> 
									</select>
								</td>
								<td width="20%"> <label for="Account Number"><strong>Account Number<font color="red">*</font></strong></label> </td>
								<td width="30%"><input name="accountno" id="accountno" class="field"  type="text" autocomplete="off" /> </td>
							</tr>

							<tr  class="odd">
								<td><label for="Sender Name"><strong>Sender's Name<font color="red">*</font></strong></label></td>
								<td><input name="payeername" id="payeername" class="field" type="text"  maxlength="50" autocomplete="off"  /></td>
								<td><label for="Mobile"><strong>Mobile<font color="red">*</font></strong></label></td>
								<td><input name="mobile" type="text" class="field" id="mobile"  maxlength="50" autocomplete="off" /></td>
							</tr>
							<tr class="even">
								<td><label for="Amount"><strong>Amount<font color="red">*</font></strong></label></td>
								<td><input name="amount" id="amount" class="field" type="text"  maxlength="50" autocomplete="off" /></td>
								<td><label for="Mode Of Deposit"><strong>Mode Of Deposit<font color="red">*</font></strong></label></td>
								<td>
									<select id="paymentmode" name="paymentmode" data-placeholder="Choose Paymentmode..." 
									class="chosen-select" style="width: 220px;" required=true>
										<option value="">Select</option>
										<option value="WEB">WEB</option>
										<option value="POS">POS</option>
										<option value="MPESA">MPESA</option> 
									</select>
								</td> 
							</tr> 
							<tr class="even">
								<td><label for="Amount"><strong>Narration<font color="red">*</font></strong></label></td>
								<td><input name="narration" id="narration" class="field" type="text"  maxlength="50" autocomplete="off" /></td>
								<td></td>
								<td></td> 
							</tr> 
							<tr class="even">
								<td><label for="Amount"><strong>Fee Amount<font color="red">*</font></strong></label></td>
								<td><input name="feeAmount" id="feeAmount" class="field" type="text"  maxlength="50" readonly /></td>
								<td><label for="Amount"><strong>Total Amount<font color="red">*</font></strong></label></td>
								<td><input name="totalAmount" id="totalAmount" class="field" type="text"  maxlength="50" readonly /></td> 
							</tr> 
						</table> 
					</fieldset> 
				</div> 

				<input name="bankName" id="bankName" class="field" type="hidden"  maxlength="50" />
				<input name="cash" id="cash" class="field" type="hidden"  maxlength="50" /> 

			</div>
				</div>
			  <div class="form-actions">
				<input type="button" class="btn btn-primary" type="text"  name="save" id="save" value="Validate Account" width="100" ></input>
				<input type="button" class="btn" type="text"  name="cancel" id="cancel" value="Cancel" width="100" ></input>
			  </div>
	</div> 
</form>
 </body> 
</html>