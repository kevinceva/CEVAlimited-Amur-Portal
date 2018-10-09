
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
 
 $(function(){
	
	 $('#btn-submit').live('click',function() {  
		 	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/payBillPinVerifyAct.action';
			$("#form1").submit();	
		});   
	 
	 $('#btn-back').live('click',function() {   
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/payBillSubAct.action";
			$("#form1").submit();					
		});   
	 
 });
</script>
</head>
<s:set value="responseJSON" var="respData"/>
<body>
	<form name="form1" id="form1" method="post" action="" autocomplete=off> 
			<div id="content" class="span10">  
			    <div>
			    	<ul class="breadcrumb">
				    	<li><a href="home.action">Home</a> <span class="divider">&gt;&gt; </span></li>
						<li><a href="#">Pay Bill</a> <span class="divider">&gt;&gt; </span></li>
						<li><a href="#">Pay Bill</a></li>
						<li> <a href="#">Pay Bill Pin Verification</a>  </li> 
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
								<i class="icon-edit"></i>Bill Payment Details
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
								</div>
							</div> 
							<div class="box-content"> 
								 <fieldset>  
									<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
										<tr class="even">
											<td >
												<label for="Service ID"><strong>Transaction Pin<font color="red">*</font></strong></label>
											</td>
											<td> 
												<input type="password" name="pin" id="pin"  autocomplete=off /> 
												
												<input type="hidden" name="billerCode"	id="billerCode" 	value="${responseJSON1.billerCode}"	required=true />
												<input type="hidden" name="abbreviation" id="abbreviation" value="${responseJSON1.abbreviation}"	required=true />
												<input type="hidden" name="accountNo" id="accountNo" value="${responseJSON1.accountNo}"	required=true />
												<input type="hidden" name="accountName" id="accountName" value="${responseJSON1.accountName}"	required=true />
												<input name="customerName" type="hidden" id="customerName" class="field"  maxlength="50" required=true value="${responseJSON1.customerName}" />
												<input name="telephone" type="hidden" id="telephone" class="field"  maxlength="50" required=true value="${responseJSON1.telephone}" />
												<input name="modeOfPayment" type="hidden" id="modeOfPayment" class="field"  maxlength="50" required=true  value="${responseJSON1.modeOfPayment}"/>
												<input name="amount" type="hidden" id="amount" class="field"  maxlength="50" required=true  value="${responseJSON1.amount}"/>
												<input name="narration" type="hidden" id="narration" class="field"  maxlength="50" required=true value="${responseJSON1.narration}" />
												<input name="commissionType" type="hidden" id="commissionType" class="field"  maxlength="50" required=true value="${responseJSON1.commissionType}" />
												<input name="feeAmount" type="hidden" id="feeAmount" class="field"  maxlength="50" required=true value="${responseJSON1.feeAmount}" />
												<input name="totalAmount" type="hidden" id="totalAmount" class="field"  maxlength="50" required=true value="${responseJSON1.totalAmount}" />
												
											</td> 
										</tr>
									</table>
								</fieldset>  		
							</div>
					</div>
				</div>
				
			<div class="form-actions"> 
				<a  class="btn btn-success" href="#" id="btn-submit">Submit</a> &nbsp;
				<a  class="btn btn-info" href="#" id="btn-back">Back</a>
			</div> 
		</div> 
</form>
</body>
</html>
