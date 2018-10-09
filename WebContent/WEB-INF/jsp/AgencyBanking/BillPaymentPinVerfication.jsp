
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
		 	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/billPayVerifyPinAct.action';
			$("#form1").submit();	
		});   
	 
	 $('#btn-back').live('click',function() {   
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/billPaymentBack.action";
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
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt;</span> </li>
					   <li><a href="billPaymentBack.action">Pay Bill</a><span class="divider"> &gt;&gt;</span> </li>
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
										
												<input name="customerAccount" type="hidden" id="customerAccount" class="field" value="<s:property value='#respData.customerAccount' />"  />
											    <input name="biller" type="hidden" id="biller" class="field" value="<s:property value='#respData.biller' />"  />
											    
											    <input name="accountName" type="hidden" id="accountName" class="field" value="<s:property value='#respData.accountName' />"  />
											    <input name="abbreviation" type="hidden" id="abbreviation" class="field" value="<s:property value='#respData.abbreviation' />"  />
												<input name="billerCode" type="hidden" id="billerCode" class="field" value="<s:property value='#respData.billerCode' />"  />
												<input name="agencyType" type="hidden"  id="agencyType" class="field"  value="<s:property value='#respData.agencyType' />"  />
												<input name="accountType" type="hidden"  id="accountType" class="field"  value="<s:property value='#respData.accountType' />"  />
												<input name="commissionType" type="hidden"  id="commissionType" class="field"  value="<s:property value='#respData.commissionType' />"  />
												<input name="amount"  type="hidden" id="amount" class="field" value="<s:property value='#respData.amount' />" />
												<input name="accountNo"  type="hidden" id="accountNo" class="field" value="<s:property value='#respData.accountNo' />" />
												
												<input name="modeOfPayment"  type="hidden" id="modeOfPayment" class="field" value="<s:property value='#respData.modeOfPayment' />" /> 
												<input name="mobileNo"  type="hidden" id="mobileNo" class="field" value="<s:property value='#respData.mobileNo' />" />
												<input name="narration"  type="hidden" id="narration" class="field" value="<s:property value='#respData.narration' />" />
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
