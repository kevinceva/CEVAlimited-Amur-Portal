
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<%@taglib uri="/struts-tags" prefix="s"%> 
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<s:set value="responseJSON" var="respData"/>
<script type="text/javascript" >
$(document).ready(function() { 
 
	$('#btn-confirm').live('click',function() {  
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/billPaymentAct.action';
		$("#form1").submit();
		return true;
	});
	
	$('#btn-back').live('click',function() {   
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/home.action";
		$("#form1").submit();	
		return true;
	});
}); 
 
</script> 
</head> 
<body>
<form name="form1" id="form1" method="post" action="">
	<div id="content" class="span10">  
		<div>
			<ul class="breadcrumb">
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li><a href="billPaymentAct.action">Pay Bill</a><span class="divider"> &gt;&gt;</span> </li>
			  <li><a href="#">Bill Payment Acknowledgement</a></li>
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
										<td width="20%"><s:property value='#respData.customerAccount' /> <input name="customerAccount" id="customerAccount" class="field" type="hidden"    maxlength="20" value="<s:property value='#respData.customerAccount' />"/> &nbsp;
										</td>
										<td width="20%"><label for="Biller"><strong>Biller</strong></label></td>
										<td width="30%">  
 												 <s:property value='#respData.biller' />  <input name="biller" id="biller" class="field" type="hidden"  value="<s:property value='#respData.biller' />"/> 
										</td> 									
									</tr>
 								</table> 
							 </fieldset>  
						</div> 
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
										<td width="30%" colspan="3"><span id="accountName"><s:property value='#respData.accountName' /></span></td> 
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
									<tr > 
										<td><label for="Commission Type"><strong>Commission Type</strong></label></td>
										<td colspan="3"><span id="commissionType"><s:property value='#respData.commissionType' /></span></td> 
									</tr> 
							</table> 
						</div> 
					</div>
				</div>
				
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
									<td width="20%"><label for="Merchant Name"><strong>Mode of Payment</strong></label></td>
									<td width="30%" colspan="3"> <s:property value='#respData.modeOfPayment' />  <input name="modeOfPayment"  type="hidden" id="modeOfPayment" class="field" value="<s:property value='#respData.modeOfPayment' />" />
									</td>
									 
								</tr>
							 <tr class="odd" id="amount">
									<td><label for="Amount/Rate"><strong>Amount/Rate</strong></label></td>
									<td colspan="3">
										 <s:property value='#respData.amount' /> <input name="amount"  type="hidden" id="amount" class="field" value="<s:property value='#respData.amount' />" />
									</td> 
								</tr>
								<tr class="odd" id="mobile">
									<td><label for="Mobile No"><strong>Mobile No</strong></label>  </td>
									<td colspan="3">
										 <s:property value='#respData.mobileNo' /> <input name="mobileNo"  type="hidden" id="mobileNo" class="field" value="<s:property value='#respData.mobileNo' />" />
									</td> 
								</tr>
								<tr class="odd" id="narration">
									<td><label for="Narration"><strong>Narration</strong></label>  </td>
									<td colspan="3">
										 <s:property value='#respData.narration' /> <input name="narration"  type="hidden" id="narration" class="field" value="<s:property value='#respData.narration' />" />
									</td> 
								</tr>								
							</table> 
						</fieldset>
					</div> 
				</div>
		</div>
	<div class="form-actions">
		<a class="btn btn-success" href="#" id="btn-confirm">Next</a>
		<a class="btn btn-danger" href="#" id="btn-back">Home</a>
		
		 
		<input name="accountName" type="hidden" id="accountName" class="field" value="<s:property value='#respData.accountName' />"  />
	    <input name="abbreviation" type="hidden" id="abbreviation" class="field" value="<s:property value='#respData.abbreviation' />"  />
		<input name="billerCode" type="hidden" id="billerCode" class="field" value="<s:property value='#respData.billerCode' />"  />
		<input name="agencyType" type="hidden"  id="agencyType" class="field"  value="<s:property value='#respData.agencyType' />"  />
		<input name="accountType" type="hidden"  id="accountType" class="field"  value="<s:property value='#respData.accountType' />"  />
		<input name="commissionType" type="hidden"  id="commissionType" class="field"  value="<s:property value='#respData.commissionType' />"  />
 		 
	</div> 
</div>  
</form> 
</body>
</html>
