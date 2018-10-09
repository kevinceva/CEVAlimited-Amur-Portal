
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
			amount : { required: {
						depends: function(element) {
							if ($('select#commissionType option:selected').val() == 'Amount'){
								return true;
							} else {
								return false;
						   }
						}
					 }    
					, number : {
						depends: function(element) {
							if ($('select#commissionType option:selected').val() == 'Amount'){
								return true;
							} else {
								return false;
						   }
						}
					}
			},
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
			amount : { 
				required : "Please Enter Amount.",
				number : "Invalid Amount."
			},			 
			rate : { 
				required : "Please Enter Rate.",
				number : "Invalid Rate."
			}
		},
		errorElement: 'label'
	};


$(document).ready(function(){   
	 
	
	$("#form1").validate(billerrules); 
	
	$('#btn-submit').live('click',function() { 
 		/* if($("#form1").valid()) { */
 			$('#abbreviation').val($('#abbreviation').val().toUpperCase());
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName%>/payBillConfAct.action";
			$("#form1").submit();	
			/* return true;
		} else {
 			return false;
		} 	 */
	}); 
	
	$('#btn-Cancel').live('click',function() {  
		$("#form1").validate().cancelSubmit = true;
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName%>/newPayBillAct.action?pid=91";
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
									<td width="30%">${responseJSON1.billerCode}
										<input type="hidden" name="billerCode"	id="billerCode" 	value="${responseJSON1.billerCode}"	required=true />
										
								</tr>
								<tr>	
									<td width="20%"><label for="Code"><strong>Biller Abbreviation</strong></label></td>
									<td width="30%"> ${responseJSON1.abbreviation}
										<input type="hidden" name="abbreviation" id="abbreviation" value="${responseJSON1.abbreviation}"	required=true />
									</td>
								</tr>
								<tr>
									<td><label for="Name"><strong>Account Number</strong></label></td>
									<td > ${responseJSON1.accountNo}
										<input type="hidden" name="accountNo" id="accountNo" value="${responseJSON1.accountNo}"	required=true />
									</td>
								</tr>
								<tr>
									<td><label for="Name"><strong>Account Name</strong></label></td>
									<td > ${responseJSON1.accountName}
										<input type="hidden" name="accountName" id="accountName" value="${responseJSON1.accountName}"	required=true />
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
										<td><label for="Mobile No"><strong>Customer Name</strong></label>  </td>
										<td colspan="3">  ${responseJSON1.customerName}
											<input name="customerName" type="hidden" id="customerName" class="field"  maxlength="50" required=true value="${responseJSON1.customerName}" /> 
										</td> 
									</tr>
									<tr class="odd" id="mobile">
										<td><label for="Mobile No"><strong>Mobile No</strong></label>  </td>
										<td colspan="3"> ${responseJSON1.telephone}
											<input name="telephone" type="hidden" id="telephone" class="field"  maxlength="50" required=true value="${responseJSON1.telephone}" /> 
										</td> 
									</tr>
									<tr>
										<td width="20%"><label for="Merchant Name"><strong>Mode of Payment</strong></label></td>
										<td width="30%" colspan="3"> ${responseJSON1.modeOfPayment}
											 <input name="modeOfPayment" type="hidden" id="modeOfPayment" class="field"  maxlength="50" required=true  value="${responseJSON1.modeOfPayment}"/>
										</td>
									</tr>
									<tr class="odd" id="cash">
										<td><label for="Amount"><strong>Commission Type</strong></label>  </td>
										<td colspan="3"> ${responseJSON1.commissionType} 
											<input name="commissionType" type="hidden" id="commissionType" class="field"  maxlength="50" value="${responseJSON1.commissionType}" required=true  />
										</td> 
									</tr>
									<tr class="odd" id="cash">
										<td><label for="Amount"><strong>Fee Amount</strong></label>  </td>
										<td colspan="3"> ${responseJSON1.feeAmount} 
											<input name="feeAmount" type="hidden" id="feeAmount" class="field"  maxlength="50" value="${responseJSON1.feeAmount}" required=true  /><span id="amount_err" class="errmsg"></span>
										</td> 
									</tr>
									<tr class="odd" id="cash">
										<td><label for="Amount"><strong>Amount</strong></label>  </td>
										<td colspan="3"> ${responseJSON1.amount}
											<input name="amount" type="hidden" id="amount" class="field"  maxlength="50" required=true  value="${responseJSON1.amount}"/>
										</td> 
									</tr>
									<tr class="odd" id="cash">
										<td><label for="Amount"><strong>Total Amount</strong></label>  </td>
										<td colspan="3">
											${responseJSON1.totalAmount}
											<input name="totalAmount" type="hidden" id="totalAmount" class="field"  maxlength="50" value="${responseJSON1.totalAmount}" required=true  /><span id="amount_err" class="errmsg"></span>
										</td> 
									</tr>
									<tr class="odd" id="narration">
										<td><label for="Narration"><strong>Narration</strong></label>  </td>
										<td colspan="3"> ${responseJSON1.narration}
											<input name="narration" type="hidden" id="narration" class="field"  maxlength="50" required=true value="${responseJSON1.narration}" /> 
										</td> 
									</tr>							
							</table>
						</fieldset>
					</div>
				</div>
			</div>

						<div class="form-actions">
				<input type="button" class="btn btn-primary" name="btn-submit" id="btn-submit" value="Confirm" width="100"></input>
					 &nbsp;<input 	type="button" class="btn " name="btn-Cancel" id="btn-Cancel"	value="Back" width="100"></input>
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
