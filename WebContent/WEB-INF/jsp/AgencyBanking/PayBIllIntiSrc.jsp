
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String ctxstr = request.getContextPath();
%>
<%
	String appName = session.getAttribute(
			CevaCommonConstants.ACCESS_APPL_NAME).toString();
%>

<style type="text/css">
.errors {
	font-weight: bold;
	color: red;
	padding: 2px 8px;
	margin-top: 2px;
}

input#abbreviation {
	text-transform: uppercase
}
;
</style>
<SCRIPT type="text/javascript"> 

var billerrules = {
		rules : {
 			abbreviation : { required : true },
			name : { required : true } ,
			agencyType : { required : true },
			address : { required : true  },
			telephone : { required : true , number : true},
			contactPerson : { required : true },
			email : { required : true , email : true },
			commissionType : { required : true },
			narration : {required : true },
			customerName : {required : true },
			amount : { required: true },
			rate : { required: {
					depends: function(element) {
						if ($('select#commissionType option:selected').val() == 'Rate'){
							return true;
						} else {
							return false;
					   }
					}
				 }    
			, number : {
				depends: function(element) {
					if ($('select#commissionType option:selected').val() == 'Rate'){
						return true;
					} else {
						return false;
				   }
				}
				}
			}
		},		
		messages : {
			abbreviation : { 
				required : "Please Enter Abbreviation." 
			  }, 
			name : { 
					required : "Please Enter Name." 
				},
			agencyType : { 
					required : "Please Select Agency Type."
				},			
			 
			address : { 
						required : "Please Enter Address."
					} , 
			telephone : { 
				required : "Please Enter Telephone.",
				number : "Invalid Telephone Number."
			},
			 
			contactPerson : { 
				required : "Please Enter Contact Person."
			},
				 
			email : { 
				required : "Please Enter Email.",
				email : "Invalid Email Address Entered."
			},
						 
			commissionType : { 
				required : "Please Select Commission Type."
			},
			narration : {
				required : "Please enter narration."
			},
			customerName	: {
				required : "Please enter Customer Name."
			},
		 
			amount : { 
				required : "Please Enter Amount."
			},			 
			rate : { 
				required : "Please Enter Rate.",
				number : "Invalid Rate."
			}
		},
		errorElement: 'label'
	};


$(document).ready(function(){   
	 
	var mydata ='${responseJSON1.BILLER_DATA}';
	var json = jQuery.parseJSON(mydata);
	$.each(json, function(i, v) {
	    var options = $('<option/>', {value: v.val , text:v.val+'~'+v.key}).attr('data-id',i);  
	    $('#billerCode').append(options);
	});
	
	
	$('#billerCode').on('change',function() {
		if($(this).val() != null) {
			var queryString="method=getBillerAccountDetails&billerCode="+$("#billerCode").val(); 
			$.getJSON("postJson.action", queryString,function(data) {  
				var json = data.responseJSON.BILLER_ACCT_DATA;
				$('#accountNo').find('option:not(:first)').remove();
	 	    	$.each(json, function(i, v) {
	 				var options = $('<option/>', {value: v.val, text: v.val+'~'+v.key}).attr('data-id',i);  
	 				$('#accountNo').append(options);
	 			});
	 	    	
	 	    	$('select').trigger("liszt:updated");
	 	    	
	 	    	var billerAbbr = data.responseJSON.BILLER_ABBR;
	 	    	$('#abbreviation').val(billerAbbr);
	 	    	$('#billerAbbr').text(billerAbbr);
	 	    	
	 	    	 var commission= data.responseJSON.COMM_TYPE;
	 	    	var feeAmt= data.responseJSON.COMM_AMT;
	 	    	$('#commission').text(commission);
	 	    	$('#fee').text(feeAmt);
	 	    	
	 	    	$('#commissionType').val(commission);
	 	    	$('#feeAmount').val(feeAmt);
	 	    	
			});
		}
		 
	});
	
	$('#amount').on('change',function() {
		var feeAmount = $("#feeAmount").val();
		var actAmt = $("#amount").val();
		var finalAmt=parseInt(feeAmount)+parseInt(actAmt);
		$("#totAmt").text(finalAmt);
		$("#totalAmount").val(finalAmt);
	});
	
	$('#accountNo').on('change',function() {
		var data =$("#accountNo option:selected").text();
		var sdata = data.split("~");
		$('#accountName').val(sdata[1]);
	    $('#acctName').text(sdata[1]);
	});
	
	
	$("#form1").validate(billerrules); 
	
	$('#btn-submit').live('click',function() { 
 		/* if($("#form1").valid()) { */
 			$('#abbreviation').val($('#abbreviation').val().toUpperCase());
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName%>/payBillSubAct.action";
			$("#form1").submit();	
			/* return true;
		} else {
 			return false;
		} 	 */
	}); 
	
	$('#btn-Cancel').live('click',function() {  
		$("#form1").validate().cancelSubmit = true;
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName%>/newPayBillAct.action";
		$("#form1").submit();
	});

});

	//For Closing Select Box Error Message_Start
	$(document)
			.on(
					'change',
					'select',
					function(event) {

						if ($('#' + $(this).attr('id')).next('label').text().length > 0) {
							$('#' + $(this).attr('id')).next('label').text('');
							$('#' + $(this).attr('id')).next('label').remove();
						}

						if ($(this).attr('id') == 'commissionType') {
							if ($(
									'select#' + $(this).attr('id')
											+ ' option:selected').val() == 'Rate') {
								$('#comm-amt').hide();
								$('#comm-rate').show();
							} else if ($(
									'select#' + $(this).attr('id')
											+ ' option:selected').val() == 'Amount') {
								$('#comm-amt').show();
								$('#comm-rate').hide();
							} else {
								$('#comm-amt').hide();
								$('#comm-rate').hide();
							}
						}

					});

	//For Closing Select Box Error Message_End
</SCRIPT>
</head>
<body>
	<form name="form1" id="form1" method="post" autocomplete="off">
		<div id="content" class="span10">

			<div>
				<ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider">&gt;&gt; </span></li>
					<li><a href="#">Pay Bill</a> <span class="divider">&gt;&gt; </span></li>
					<li><a href="#">Pay Bill</a></li>
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
						<i class="icon-edit"></i>Biller Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i
								class="icon-cog"></i></a> <a href="#"
								class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a>

						</div>
					</div>

					<div class="box-content">
						<fieldset>
							<table width="98%" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
								<tr>
									<td width="20%"><label for="Abbreviation"><strong>Biller Code</strong></label></td>
									<td width="30%">
										<select id="billerCode" name="billerCode" class="chosen-select"
											    required="required">
														<option value="">Select</option>
										</select>
								</tr>
								<tr>	
									<td width="20%"><label for="Code"><strong>Biller Abbreviation</strong></label></td>
									<td width="30%"> <b><span id="billerAbbr"></span> </b>
										<input type="hidden" name="abbreviation" id="abbreviation" value=""	required=true />
									</td>
								</tr>
								<tr>
									<td><label for="Name"><strong>Account Number</strong></label></td>
									<td > 
												<select id="accountNo" name="accountNo" class="chosen-select" >
													<option value="NO_DATA">Select</option>
												</select>
									</td>
								</tr>
								<tr>
									<td><label for="Name"><strong>Account Name</strong></label></td>
									<td > <b><span id="acctName"></span></b>
										<input type="hidden" name="accountName" id="accountName" value=""	required=true />
									</td>
								</tr>
							</table>
						</fieldset>
					</div>
				</div>
			</div>

			<div class="row-fluid sortable">
				<div class="box span12">
					<div class="box-header well" data-original-title>
						<i class="icon-edit"></i>Bill Payment Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i
								class="icon-cog"></i></a> <a href="#"
								class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a>

						</div>
					</div>

					<div class="box-content">
						<fieldset>

							<table width="98%" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
									<tr class="odd" id="mobile">
										<td><label for="Mobile No"><strong>Customer Name<font color="red">*</font></strong></label>  </td>
										<td colspan="3">
											<input name="customerName" type="text" id="customerName" class="field"  maxlength="50" required=true value="<s:property value='#respData.customerName' />" /> 
										</td> 
									</tr>
									<tr class="odd" id="mobile">
										<td><label for="Mobile No"><strong>Mobile No<font color="red">*</font></strong></label>  </td>
										<td colspan="3">
											<input name="telephone" type="text" id="telephone" class="field"  maxlength="50" required=true value="<s:property value='#respData.mobileNo' />" /> <span id="mobileNo_err" class="errmsg"></span>
										</td> 
									</tr>
									<tr>
										<td width="20%"><label for="Merchant Name"><strong>Mode of Payment<font color="red">*</font></strong></label></td>
										<td width="30%" colspan="3">
											<s:select cssClass="chosen-select" 
												headerKey="" 
												headerValue="Select"
												list="#{'CASH':'CASH','POS':'POS','MPESA':'MPESA'}"
												name="modeOfPayment" 
												value="#respData.modeOfPayment" 
												id="modeOfPayment" 
												requiredLabel="true" 
												theme="simple"
												data-placeholder="Choose Mode Of Payment..." 
			 								/>  
										</td>
									</tr>
									<tr class="odd" id="cash">
										<td><label for="Amount"><strong>Commission Type<font color="red">*</font></strong></label>  </td>
										<td colspan="3"> <b><span id="commission"></span> </b>
											<input name="commissionType" type="hidden" id="commissionType" class="field"  maxlength="50" required=true  /><span id="amount_err" class="errmsg"></span>
										</td> 
									</tr>
									<tr class="odd" id="cash">
										<td><label for="Amount"><strong>Fee Amount<font color="red">*</font></strong></label>  </td>
										<td colspan="3"> <b><span id="fee"></span> </b>
											<input name="feeAmount" type="hidden" id="feeAmount" class="field"  maxlength="50" required=true  /><span id="amount_err" class="errmsg"></span>
										</td> 
									</tr>
									<tr class="odd" id="cash">
										<td><label for="Amount"><strong>Amount<font color="red">*</font></strong></label>  </td>
										<td colspan="3">
											<input name="amount" type="text" id="amount" class="field"  maxlength="50" required=true  value="<s:property value='#respData.amount' />"/><span id="amount_err" class="errmsg"></span>
										</td> 
									</tr>
									
									<tr class="odd" id="cash">
										<td><label for="Amount"><strong>Total Amount<font color="red">*</font></strong></label>  </td>
										<td colspan="3">
											<span id="totAmt"></span>
											<input name="totalAmount" type="hidden" id="totalAmount" class="field"  maxlength="50" required=true  /><span id="amount_err" class="errmsg"></span>
										</td> 
									</tr>
									
									
									<tr class="odd" id="narration">
										<td><label for="Narration"><strong>Narration<font color="red">*</font></strong></label>  </td>
										<td colspan="3">
											<input name="narration" type="text" id="narration" class="field"  maxlength="50" required=true value="<s:property value='#respData.narration' />" /> <span id="narration_err" class="errmsg"></span>
										</td> 
									</tr>							
							</table>
						</fieldset>
					</div>
				</div>
			</div>

						<div class="form-actions">
				<input type="button" class="btn btn-primary" name="btn-submit"
					id="btn-submit" value="Submit" width="100"></input> &nbsp;<input
					type="button" class="btn " name="btn-Cancel" id="btn-Cancel"
					value="Back" width="100"></input>
			</div>

		</div>
	</form>
	<script type="text/javascript">
		$(function() {

			var config = {
				'.chosen-select' : {},
				'.chosen-select-deselect' : {
					allow_single_deselect : true
				},
				'.chosen-select-no-single' : {
					disable_search_threshold : 10
				},
				'.chosen-select-no-results' : {
					no_results_text : 'Oops, nothing found!'
				},
				'.chosen-select-width' : {
					width : "95%"
				}
			};

			for ( var selector in config) {
				$(selector).chosen(config[selector]);
			}

		});
	</script>
</body>
</html>
