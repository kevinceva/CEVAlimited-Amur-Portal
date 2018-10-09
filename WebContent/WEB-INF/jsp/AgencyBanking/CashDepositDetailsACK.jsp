
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Create Entity</title>
 
<script type="text/javascript">
$(document).ready(function () {
	var checkVal = '${cash}';
	if(checkVal != 'WEB') {
		$('#cust-det').hide();
	}
    $('#save').live('click', function () { 
    	    $("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/CashDepositAck.action';
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
				<a href="#">Home</a> <span class="divider">&gt;&gt;</span>
			</li>
			<li><a href="/jsp/">Cash Deposit</a> 
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
		 <table width="950" border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable" id="user-details">  
	 		<tr class="even">
				<td width="20%"><label for="Service"><strong>Service </strong></label></td>
				<td width="30%">${bankName}</td> 
				<td width="20%"><label for="Account Number"><strong>Account Number</strong> </label> </td>
                <td width="30%">${accountno}</td> 
			</tr>

			<tr  class="odd">
				<td><label for="Payeer Name"><strong>Sender's Name</strong> </label> </td>
				<td>${payeername}</td>

				<td><label for="Mobile"><strong>Mobile </strong></label> </td>
				<td>${mobile}</td>

			</tr>
			<tr class="even">
				<td><label for="Amount"><strong>Amount </strong></label> </td>
				<td>${amount} </td>
				<td><label for="Mode Of Deposit"><strong>Mode Of Deposit </strong></label></td>
				<td> ${cash}</td> 
			</tr> 
			<tr class="even">
				<td><label for="Amount"><strong>Narration </strong></label> </td>
				<td>${narration} </td>
				<td></td>
				<td></td> 
			</tr>
			<tr class="even">
				<td><label for="Amount"><strong>Fee Amount</strong></label></td>
				<td> ${feeAmount} <input name="feeAmount" id="feeAmount" class="field" type="hidden" value="${feeAmount}"  maxlength="50" readonly /></td>
				<td><label for="Amount"><strong>Total Amount</strong></label></td>
				<td> ${totalAmount} <input name="totalAmount" id="totalAmount" class="field" type="hidden" value="${totalAmount}"  maxlength="50" readonly /></td> 
			</tr>  
		 	<tr class="even" id="cust-det">
				<td><label for="Customer Name"><strong>Customer Name </strong></label></td>
				<td><strong>${clientrequestJSON.ACCOUTNAME}</strong></td> 
				<td></td>
				<td>
					<input name="bankid" type="hidden" id="bankid" class="field"   value="${bankid}"    />
					<input name="bankName" type="hidden" id="bankName" class="field"  value="${bankName}"    />
					<input name="accountno" type="hidden" id="accountno" class="field" value="${accountno}"   />
					<input name="payeername" type="hidden" id="payeername" class="field" value="${payeername}"  />
					<input name="mobile" type="hidden" class="field" id="mobile" value="${mobile}"       />
					<input name="amount" type="hidden" class="field" id="amount" value="${amount}"     />
					<input name="cash" type="hidden" class="field" id="cash" value="${cash}"    />
					<input type="hidden" name="narration" id="narration" value="${narration}"></input>
				</td>
			</tr> 
		</table>
	</fieldset>
	</div> 
				
	</div>
</div>
	<div class="form-actions">
	   <input type="button" class="btn btn-primary" type="text"  name="save" id="save" value="Confirm" width="100" ></input>
	   <input type="button" class="btn"  name="cancel" id="cancel" value="Cancel" width="100" ></input>
 	 </div>
</div>
 
</form>
</body>  
</html>