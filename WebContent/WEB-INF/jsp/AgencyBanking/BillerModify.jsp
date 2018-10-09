
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
	String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString();
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
	
	$("#commissionType").val('${responseJSON1.COMM_TYPE}');
	
	
	$("#abbreviation")
	  .focusout(function() {  
		  if($('#abbreviation').val().length > 0){
			  $('#billerCode').val('');
			  $('#billerCode').val($('#abbreviation').val().toUpperCase()+"-"+(Math.random() * 100000).toString().substring(0,6).replace(".",""));
		  } else {
			  $('#billerCode').val('');
		  }
				
	  }) 
	   .blur(function() {
		   if($('#abbreviation').val().length > 0){
				  $('#billerCode').val('');
				  $('#billerCode').val($('#abbreviation').val().toUpperCase()+"-"+(Math.random() * 100000).toString().substring(0,6).replace(".",""));
			  } else {
				  $('#billerCode').val('');
			  }
	  }).keypress(function() {
		   if($('#abbreviation').val().length == 0){  
				  $('#billerCode').val('');
			  }
	  });  
	
	 
	
	$("#form1").validate(billerrules); 
	
	$('#btn-submit').live('click',function() { 
 		if($("#form1").valid()) {
 			$('#abbreviation').val($('#abbreviation').val().toUpperCase());
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName%>/billerRegModifySubmitAct.action";
			$("#form1").submit();	
			return true;
		} else {
 			return false;
		} 	
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
					<li><a href="#">Biller Registration</a></li>
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
						<i class="icon-edit"></i>Basic Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i
								class="icon-cog"></i></a> <a href="#"
								class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a>

						</div>
					</div>

					<div class="box-content">
						<fieldset>

							<table width="98%" border="0" cellpadding="5" cellspacing="1"
								class="table table-striped table-bordered bootstrap-datatable ">
								<tr>
									<td width="20%"><label for="Abbreviation"><strong>Abbreviation</strong></label></td>
									<td width="30%"> ${responseJSON1.ABBREVATION}
										<input type="hidden" name="abbreviation"	id="abbreviation"	value="${responseJSON1.ABBREVATION}"	required=true />
									</td>
									<td width="20%"><label for="Code"><strong>Code</strong></label></td>
									<td width="30%"> ${responseJSON1.BILLER_ID} <input type="hidden" name="billerCode"
										id="billerCode"
										value="${responseJSON1.BILLER_ID}"	required=true /></td>
								</tr>
								<tr>
									<td><label for="Name"><strong>Name</strong></label></td>
									<td > ${responseJSON1.NAME} <input type="hidden" name="name" id="name"	required=true value="${responseJSON1.NAME}"	width="30%" /></td>
		  							<td><label for="Agency Type"><strong>Agency Type</strong></label></td>
									<td>${responseJSON1.AGENCY_TYPE}
										<input type="hidden" name="agencyType"  id="agencyType"   value="${responseJSON1.AGENCY_TYPE}"    />
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
						<i class="icon-edit"></i>Contact Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i
								class="icon-cog"></i></a> <a href="#"
								class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a>

						</div>
					</div>

					<div class="box-content">
						<fieldset>

							<table width="98%" border="0" cellpadding="5" cellspacing="1"
								class="table table-striped table-bordered bootstrap-datatable ">
								<tr>
									<td width="20%"><label for="Address"><strong>Address<font
												color="red">*</font></strong></label></td>
									<td width="30%"><input type="text" name="address"
										id="address" required=true
										value="${responseJSON1.ADDRESS}" /></td>
									<td width="20%">&nbsp;</td>
									<td width="30%">&nbsp;</td>
								</tr>
								<tr>
									<td><label for="Telephone"><strong>Telephone<font
												color="red">*</font></strong></label></td>
									<td colspan=3><input type="text" name="telephone"
										id="telephone" required=true
										value="${responseJSON1.TELEPHONE}" /></td>

								</tr>
								<tr>
									<td><label for="Contact Person"><strong>Contact
												Person<font color="red">*</font>
										</strong></label></td>
									<td colspan=3><input type="text" name="contactPerson"
										id="contactPerson" required=true
										value="${responseJSON1.CONTACT_PERSON}" />
									</td>
								</tr>
								<tr>
									<td><label for="Email"><strong>Email<font
												color="red">*</font></strong></label></td>
									<td colspan=3><input type="text" name="email" id="email"
										required=true value="${responseJSON1.EMAIL}" />
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
						<i class="icon-edit"></i>Commissions
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i
								class="icon-cog"></i></a> <a href="#"
								class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a>
						</div>
					</div>

					<div class="box-content">
						<fieldset>
							<table width="98%" border="0" cellpadding="5" cellspacing="1"
								class="table table-striped table-bordered bootstrap-datatable ">
								<tr>
									<td width="20%"><label for="Commission Type"><strong>Commission
												Type<font color="red">*</font>
										</strong></label></td>
									<td width="30%"><s:select cssClass="chosen-select"
											headerKey="" headerValue="Select"
											list="#{'Rate':'Rate','Amount':'Amount'}"
											name="commissionType" value="billerBean.commissionType"
											id="commissionType" requiredLabel="true" theme="simple"
											data-placeholder="Choose Commission Type..." />
										&nbsp; <label id="commission-id" class="errors"></label></td>
									<td width="20%">&nbsp;</td>
									<td width="30%">&nbsp;</td>
								</tr>
								<tr id="comm-amt">
									<td><label for="Amount"><strong>Amount<font
												color="red">*</font></strong></label></td>
									<td colspan="3"><input type="text" name="amount"
										id="amount" required=true
										value="${responseJSON1.AMOUNT}" /></td>
								</tr>
								<tr id="comm-rate">
									<td><label for="Name"><strong>Rate<font
												color="red">*</font></strong></label></td>
									<td colspan="3"><input type="text" name="rate" id="rate"
										required=true value="${responseJSON1.AMOUNT}" />
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

			if ($('select#commissionType option:selected').val() == 'Rate') {
				$('#comm-amt').hide();
				$('#comm-rate').show();
			} else if ($('select#commissionType option:selected').val() == 'Amount') {
				$('#comm-amt').show();
				$('#comm-rate').hide();
			} else {
				$('#comm-amt').hide();
				$('#comm-rate').hide();
			}

		});
	</script>
</body>
</html>
