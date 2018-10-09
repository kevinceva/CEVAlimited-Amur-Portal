
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
   
<style type="text/css">
.messages {
  font-weight: bold;
  color: green;
  padding: 2px 8px;
  margin-top: 2px;
}

.errors{
	font-weight: bold;
	color: red;
	padding: 2px 8px;
	margin-top: 2px;
}
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
span.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
.errmsg {
color: red;
}
</style>

<script type="text/javascript">

$(document).ready(function () {
    $('#save').live('click', function () { 
     
	      var pin = $('#pin').val() == undefined ? ' ' : $('#pin').val(); 
			if(pin == '') {
				alert('please Enter Transaction PIN');
				return false;
	 		} 

    	    $("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/CashDepositPin.action';
			$("#form1").submit();
			return true;

    });

    $('#cancel').live('click', function () { 

    	    $("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/cashDepositAct.action';
			$("#form1").submit();
			return true;

    });


}); 
</script> 
</head>
<body>
	<form name="form1" id="form1" method="post"> 
		<div id="content" class="span10">
		 		<div> 
					<ul class="breadcrumb">
						<li>
							<a href="home.action">Home</a> <span class="divider">&gt;&gt;</span>
						</li>
						<li><a href="#">Cash Deposit</a> <span class="divider">&gt;&gt;</span>
						</li>
						<li>
							<a href="#">PIN Verification </a>
						</li> 
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
				<div class="box-header well" data-original-title >
						<i class="icon-edit"></i>Transaction PIN Entry
					<div class="box-icon">
						<a href="#" class="btn btn-setting btn-round"><i
							class="icon-cog"></i></a> <a href="#"
							class="btn btn-minimize btn-round"><i
							class="icon-chevron-up"></i></a>
	
					</div>
				</div> 

	 
				<div class="box-content">
					<fieldset> 
						 <table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable" id="user-details">  

							<tr class="odd">
								<td >&nbsp;</td>
								<td> <label for="TransactionPIN"><strong>Transaction PIN<font color="red">*</font></strong></label></td>
								<td><input name="pin" type="password" class="field" id="pin"  maxlength="30" size="30" autocomplete="off"/></td>
									<td >&nbsp;</td> 
								<td></td>
								<td></td>
							 </tr> 
						 </table>
				  </fieldset> 
				</div>
					</div>
				</div> 
			<div class="form-actions">
				<input type="button" class="btn btn-primary" type="text"  name="save" id="save" value="PIN Verify" width="100" ></input>
				<input type="button" class="btn" type="text"  name="cancel" id="cancel" value="Cancel" width="100" ></input>							
			</div> 
		</div>

<input type="hidden" name="multiData" id="multiData" value=""></input>
<input name="bankid" type="hidden" id="bankid" value="${bankid}" />
<input name="bankName" type="hidden" id="bankName" value="${bankName}" />
<input name="accountno" type="hidden" id="accountno" value="${accountno}" />
<input name="mobile" type="hidden" id="mobile" value="${mobile}" />
<input name="amount" type="hidden" id="amount" value="${amount}" />
<input name="payeername" type="hidden" id="payeername" value="${payeername}" />
<input name="cash" type="hidden" id="cash" value="${cash}" />
<input type="hidden" name="customerName" id="customerName" value="${customerName}"></input>
<input type="hidden" name="location" id="location" value="${location}"></input>
<input type="hidden" name="loginId" id="loginId" value="${loginId}"></input>
<input type="hidden" name="User_Name" id="User_Name" value="${User_Name}"></input>	
<input type="hidden" name="narration" id="narration" value="${narration}"></input>	
<input type="hidden" name="feeAmount" id="feeAmount" value="${feeAmount}"></input>	
<input type="hidden" name="totalAmount" id="totalAmount" value="${totalAmount}"></input>		
 </form>
   
</body> 
</html>