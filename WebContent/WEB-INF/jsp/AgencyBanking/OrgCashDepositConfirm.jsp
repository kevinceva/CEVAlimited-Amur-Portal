
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
 
<s:set value="responseJSON" var="respData" />
<script  type="text/javascript"> 
 $(function () {
	 
    $('#Submit').live('click', function () { 
   	    $("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/orgCashDepositAck.action';
		$("#form1").submit();
		return true; 
    });
	
	
    $('#cancel').live('click', function () { 
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/orgCashDeposit.action';
		$("#form1").submit();
		return true; 
    }); 

     
});
 
</script>
</head>
<body >
<form name="form1" id="form1" method="post" action=""> 
	<div id="content" class="span10">  
		<div>
			<ul class="breadcrumb">
				<li> <a href="home.action">Home</a> <span class="divider">&gt;&gt;</span> </li>
 				<li><a href="viewOrganization.action?pid=130">E-Wallet</a><span class="divider">&gt;&gt;</span></li>
				<li><a href="#">Account Credit Confirmation</a></li>
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
							<i class="icon-edit"></i>Account Credit Details Confirmation
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
								<td width="20%"><label for="Service"><strong>Organization Id</strong></label></td>
								<td width="50%" colspan="3">
									<s:property value="#respData.orgId" />  <input name="orgId" id="orgId" class="field" type="hidden"   value="<s:property value="#respData.orgId" />"/> 
 								</td> 
							</tr> 
							<tr class="even">
								<td><label for="Service"><strong>Organization Name</strong></label></td>
								<td colspan="3">
									<s:property value="#respData.orgName" />  <input name="orgName" id="orgName" class="field" type="hidden"   value="<s:property value="#respData.orgName" />"/>
								</td> 
							</tr> 
							<tr class="even">
								<td><label for="Service"><strong>Organization Type</strong></label></td>
								<td colspan="3">
									<s:property value="#respData.organizationType" />  <input name="organizationType" id="organizationType" class="field" type="hidden"   value="<s:property value="#respData.organizationType" />"/>
								</td> 
							</tr> 
							<tr class="even">
								<td><label for="Service"><strong>Sender's Name</strong></label></td>
								<td colspan="3">
									<s:property value="#respData.payeername" />  
									<input name="payeername" id="payeername" class="field" type="hidden" required="true"  maxlength="50" autocomplete="off" value='<s:property value="#respData.payeername" />' />
								</td> 
							</tr> 
							<tr  class="odd">
								<td><label for="Amount"><strong>Amount</strong></label></td>
								<td colspan="3"> <s:property value="#respData.amount" />  
								<input name="amount" id="amount" class="field" type="hidden" required="true"  maxlength="50" autocomplete="off" value='<s:property value="#respData.amount" />'/></td>
							</tr> 
							<tr  class="even">	
								<td><label for="Mobile"><strong>Mobile No</strong></label></td>
								<td colspan="3"> <s:property value="#respData.mobile" />  
								<input name="mobile" type="hidden" class="field" id="mobile" required="true"  maxlength="50" autocomplete="off" value='<s:property value="#respData.mobile" />'/></td>
							</tr>
							<tr class="even"> 
								<td><label for="Mode Of Deposit"><strong>Mode Of Deposit</strong></label></td>
								<td colspan="3"> 
									<s:property value="#respData.paymentmode" />  
									<input name="paymentmode" type="hidden" class="field" id="paymentmode" required="true"  maxlength="50" autocomplete="off" value='<s:property value="#respData.paymentmode" />'/>
								</td> 
							</tr> 
						</table> 
					</fieldset> 
				</div>  
				
			</div>
				</div>
			  <div class="form-actions">
				<input type="button" class="btn btn-primary" type="hidden"  name="Submit" id="Submit" value="Confirm" width="100" ></input> &nbsp;
				<input type="button" class="btn"    name="cancel" id="cancel" value="Back" width="100" ></input>
			  </div>
	</div> 
</form>
 </body> 
</html>