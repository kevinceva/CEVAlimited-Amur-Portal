
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<style type="text/css">
form#form1{margin:0 0 0px}
</style> 
<script type="text/javascript" > 



var payBillRules = {
   rules : {
		biller : { required : true }
   },  
   messages : {
	   
	
    biller : { 
			required : "Please Select Biller."
        } 
   } 
  }; 

var payBillRules1 = {
		   rules : {
			   modeOfPayment : { required : true },
			   mobileNo : { required : true },
				amount :{required : true, number : true} 
		   },  
		   messages : {
			   
			   modeOfPayment : { 
					required : "Please Select Mode Of Payment."
		       },
		       mobileNo : { 
					required : "Please Enter Mobile No."
		        } 
		   },
		   amount : { 
				required : "Please Enter Amount/Rate."
	        } 
		  };
		  
var submitFlag = true;

$(document).ready(function() { 
	
	$("#alert-msg").hide();
	$(".form-actions").hide(); 
	$("#customer-info-details").hide(); 
	$("#bill-payment-details").hide();
	 
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
	
	$('#billerType').live('change',function() {
		if($(this).val() != null) {
			var queryString="method=getBillerDetails&billerType="+$("#billerType").val(); 
			$.getJSON("postJson.action", queryString,function(data) {  
				var json = data.responseJSON.BILLER_DATA;
				$('#biller').find('option:not(:first)').remove();
	 	    	$.each(json, function(i, v) {
	 				var options = $('<option/>', {value: v.val, text: v.key}).attr('data-id',i);  
	 				$('#biller').append(options);
	 			});
			});
		}
		 
	});
	


	//$('#biller').live('change',function(){ 
	 $('#btn-search').live('click',function(){  
		$(".form-actions").hide(); 
		$("#customer-info-details").hide(); 
		$("#bill-payment-details").hide();
		$('.alert').hide();
		
		$("#form1").validate(payBillRules);
		if($("#form1").valid()) { 
	  		var queryString = "entity=${loginEntity}&method=searchBiller&billerId="+ $("#biller option:selected").val()+"&customerAccount="+ $("#customerAccount").val();	
			$.getJSON("postJson.action", queryString,function(data) {   
 				
				var msg = data.responseJSON.message;
				var name= data.responseJSON.name
				
				$("#accountName").val(name);
				if(msg.indexOf("~NO_ERROR") != -1) { 
					$("#customerDataDiv").find('tr > td > span').each(function(){  
						
						if($(this).attr('id') == 'abbreviation' ) {
							$(this).text(data.billerBean.abbreviation);
						}
						if($(this).attr('id') == 'billerCode' ) {
							$(this).text(data.billerBean.billerCode);
						}
						if($(this).attr('id') == 'agencyType' ) {
							$(this).text(data.billerBean.agencyType);
						}
						if($(this).attr('id') == 'accountType' ) {
							$(this).text(data.billerBean.accountType);
						}  
						if($(this).attr('id') == 'accountNo' ) {
							$(this).text(data.billerBean.accountNo);
						}						
						if($(this).attr('id') == 'commissionType' ) {
							$(this).text(data.billerBean.commissionType);
						}
				 	 	//if($(this).attr('id') == 'amount' ) {
							//$(this).text(data.billerBean.amount);/
						//}  
					});
					//$('#amount').val(data.billerBean.amount);
					$("#customer-info-details").show(200);
					$("#bill-payment-details").show(200);
					$(".form-actions").show(); 
					$("#alert-msg").hide(); 
					submitFlag = true;
				} else {
 					$("#alert-msg").show();
 					$('.alert').show();
 					$(".form-actions").hide(); 
					submitFlag = false;
 				}
				
				/* $("#customer-contact-details").show(200);
				$("#customerContactDiv").find('tr > td > span').each(function(){   
					if($(this).attr('id') == 'Address' ) {
						$(this).text(data.billerBean.address);
					}
					if($(this).attr('id') == 'Telephone' ) {
						$(this).text(data.billerBean.telephone);
					}
					if($(this).attr('id') == 'ContactPerson' ) {
						$(this).text(data.billerBean.contactPerson);
					}
					if($(this).attr('id') == 'Email' ) {
						$(this).text(data.billerBean.email);
					}  
				});  */ 
				
 			}); 
			
			return true;
		} else { 
	 
			return false;		
		}
	 });	 

 
	$("#mobileNo,#amount").keypress(function (e) {
		 //if the letter is not digit then display error and don't type anything
		 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
			//display error message
			$("#"+$(this).attr('id')+"_err").html("Digits Only").show().fadeOut("slow");
				   return false;
			}
	   }); 
	
	
	$('#btn-submit').live('click',function() {  
	 
		
		$("#form2").validate(payBillRules1); 
		
		
		if($("#form2").valid() && submitFlag){ 
			
			$("#form1").find('input[type=text],select').each(function() {
				var nameInput = "";
				var valToSpn =  "";
				try{
					  nameInput = $(this).attr('name') ;
					  
					  if(nameInput == 'biller') 
						  valToSpn = (  $('select#'+nameInput+' option:selected').text() =='' ? ' ' :  $('select#'+nameInput+' option:selected').text());
					  else 
					 	  valToSpn = ($(this).attr('value') =='' ? ' ' :$(this).attr('value'));
					  
					//  console.log("nameInput ===> "+nameInput +" valToSpn ===> "+ valToSpn);
					  
					  if(valToSpn != "") {
							input = $("<input />").attr("type", "hidden").attr("name",nameInput).val(valToSpn.trim());
							$('#form2').append($(input));	 
						}
					  
				} catch(e1) {
					//console.log('The Exception Stack is :: '+ e1);
				} 
			});
			
			$("#form1").find('span').each(function() {
				var nameInput = "";
				var valToSpn =  "";
				try{
					  nameInput = $(this).attr('id') ;
					  valToSpn = ($(this).text() =='' ? ' ' :$(this).text());
					  
					  ///console.log("nameInput ===> "+nameInput +" valToSpn ===> "+ valToSpn);
					  
					  if(nameInput != undefined ) {
							input = $("<input />").attr("type", "hidden").attr("name",nameInput).val(valToSpn.trim());
							$('#form2').append($(input));	 
						}
					  
				} catch(e1) {
					//console.log('The Exception Stack is :: '+ e1);
				}
				
			});
			
			
		 
			$("#form2")[0].action='<%=request.getContextPath()%>/<%=appName %>/billPaymentSubmit.action';
			$("#form2").submit();
			return true; 
		} else if(!submitFlag) {
			$('#alert-msg').clone().insertAfter('#alert-msg');
			$('#submit_err').text('');  
			return false;
		} else {
			return false;
		}
	}); 
	
	$('#btn-Cancel').live('click',function() {  
		$("#form1").validate().cancelSubmit = true;
		$("#form2").validate().cancelSubmit = true;
		
		$("#form2")[0].action="<%=request.getContextPath()%>/<%=appName %>/home.action";
		$("#form2").submit();					
	});   
	
	$('#msg-close').live('click',function() { 
		 $(this).parents("div#alert-msg").remove();
	}); 
	
}); 
</script>
</head>
<s:set value="responseJSON" var="respData"/>
<body>

			<div id="content" class="span10">  
			    <div>
					<ul class="breadcrumb">
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt;</span> </li>
 					  <li><a href="#">Pay Bill</a></li>
					</ul>
				</div> 
				
				<form name="form1" id="form1" method="post" action="">

				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
						<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Select Biller Type
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

							</div>
						</div> 
						<div id="BillerType" class="box-content">
							<fieldset>  
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable " >
									<tr class="even"> 
									<td width="30%"><label for="Biller Type"><strong>Biller Type<font color="red">*</font> </strong></label></td>
									<td colspan=3>
											<s:select cssClass="chosen-select" 
												headerKey="" 
												headerValue="Select"
												list="#respData.BillerList" 
												name="billerType" 
 												id="billerType" 
 												value="#respData.billerCode"
 												requiredLabel="true" 
												theme="simple"
												data-placeholder="Choose Biller..." 
												style="width: 220px;"
 												 />  
									
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
							<i class="icon-edit"></i>Select Biller
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

							</div>
						</div> 
						<div id="Biller" class="box-content">
							<fieldset>  
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable " >
									<tr class="even"> 
										<td width="30%"><label for="Customer Account"><strong>Customer Account</strong></label></td>
										<td width="20%"><input name="customerAccount" id="customerAccount" class="field" type="text"    maxlength="20" value="<s:property value='#respData.customerAccount' />"/> &nbsp;
										</td>
										<td width="20%"><label for="Biller"><strong>Select Biller<font color="red">*</font></strong></label></td>
										<td width="30%"> 
												<select id="biller" name="biller" cssClass="chosen-select" >
													<option value="NO_DATA">Select</option>
												</select>
											 &nbsp;<a  class="btn btn-success" href="#" id="btn-search" >Search</a>
										</td> 									
									</tr>
 								</table>
 								 
							 </fieldset>  
						</div> 
					</div>
				</div> 
				
 				
				
				<div class="box-content alerts" id="alert-msg" >
					<div class="alert alert-error">
						<a href="#" class="close" data-dismiss="alert" id="msg-close">×</a>
						<strong>Oh snap!</strong> Invalid Account Number, try submitting again.
					</div> 
				</div>	 
				
				<div class="row-fluid sortable" id="customer-info-details">
					<div class="box span12">
						<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Details
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
							</div>
						</div>
						<div id="customerDataDiv"  class="box-content"> 
							<table width="98%"  border="0" cellpadding="5" cellspacing="1" 
								class="table table-striped table-bordered bootstrap-datatable " > 
									<tr>
										<td width="20%" ><label for="Account Name"><strong>Account Name</strong></label></td>
										<td width="30%" colspan="3">
										<input name="accountName" type="text" id="accountName" class="field"  maxlength="50"  /> 
										</td> 
									</tr>
									<tr>
										<td ><label for="Abbreviation"><strong>Abbreviation</strong></label></td>
										<td colspan="3"><span id="abbreviation"><s:property value='#respData.abbreviation' /></span></td> 
									</tr>
									<tr>									
										<td ><label for="Code"><strong>Code</strong></label></td>
										<td colspan="3"><span id="billerCode"><s:property value='#respData.billerCode' /></span></td>							
									</tr>
								  	<!-- <tr> 
										<td><label for="Name"><strong>Name</strong></label></td>
										<td colspan=3></td>						 
									</tr> -->
									<tr > 
										<td><label for="Agency Type"><strong>Agency Type</strong></label></td>
										<td colspan="3"><span id="agencyType"><s:property value='#respData.agencyType' /></span></td> 
									</tr>
									<tr>
										<td><label for="Account Type"><strong>Account Type</strong></label></td>
										<td colspan="3"><span id="accountType"><s:property value='#respData.accountType' /></span></td>
									</tr>  
									<tr>
										<td><label for="Account No"><strong>Account No</strong></label></td>
										<td colspan="3"><span id="accountNo"><s:property value='#respData.accountNo' /></span></td>
									</tr>									
									<tr > 
										<td><label for="Commission Type"><strong>Commission Type</strong></label></td>
										<td colspan="3"><span id="commissionType"><s:property value='#respData.commissionType' /></span></td> 
									</tr>
									<%-- <tr>
										<td><label for="Amount/Rate"><strong>Amount/Rate</strong></label></td>
										<td colspan="3"><span id="amount"><s:property value='#respData.amount' /></span></td> 
									</tr>  --%>
							</table> 
						</div> 
					</div>
				</div>  
	</form> 
	<form name="form2" id="form2" method="post" action="">
		<div class="row-fluid sortable" id="bill-payment-details">
				<div class="box span12">
					<div class="box-header well" data-original-title>
						<i class="icon-edit"></i>Bill Payment Details
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
						</div>
					</div>
					<div class="box-content" id="payment-details"> 
						<fieldset>
							<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
								<tr class="even">
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
									<td><label for="Amount"><strong>Amount/Rate<font color="red">*</font></strong></label>  </td>
									<td colspan="3">
										<input name="amount" type="text" id="amount" class="field"  maxlength="50" required=true  value="<s:property value='#respData.amount' />"/><span id="amount_err" class="errmsg"></span>
									</td> 
								</tr>
								<tr class="odd" id="mobile">
									<td><label for="Mobile No"><strong>Mobile No<font color="red">*</font></strong></label>  </td>
									<td colspan="3">
										<input name="mobileNo" type="text" id="mobileNo" class="field"  maxlength="50" required=true value="<s:property value='#respData.mobileNo' />" /> <span id="mobileNo_err" class="errmsg"></span>
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
			<a  class="btn btn-danger" href="#" id="btn-Cancel" >Home</a> &nbsp;&nbsp;
			<a  class="btn btn-success" href="#" id="btn-submit" >Submit</a> &nbsp; <span id="submit_err" class="errmsg"></span>
		</div> 
	</form> 
	 </div>
	<input type="hidden" name="token" id="token" value="${responseJSON.token}"/>
<script type="text/javascript" >  

$(document).ready(function() { 
	var chk = '<s:property value='#respData.method' />';
	if(chk == 'back' ) {
		$(".form-actions").show(); 
		$("#customer-info-details").show(); 
		$("#bill-payment-details").show();
	} 
	
});

</script>
 </body>
</html>
