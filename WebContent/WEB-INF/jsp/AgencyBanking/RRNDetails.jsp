
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
 
 <style type="text/css">
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
</style> 

<script type="text/javascript"> 
 
function cancel() {
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/CheckSwitchStatus.action?pid=12';
	$("#form1").submit();
	return true;
}
 
</script>


</head>
<body> 

<form name="form1" id="form1" method="post">  
	<div id="content" class="span10"> 
		<div>
			<ul class="breadcrumb">
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			   <li> <a href="#"> Transactions</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li> <a href="#">Customer Information</a></li>

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
		<div class="row-fluid sortable"> 
			<div class="box span12"> 
					<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Customer Information
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

						</div>
					</div>
				<div class="box-content">
					<fieldset> 
						<table width="950" border="0" cellpadding="5" cellspacing="1"
										class="table table-striped table-bordered bootstrap-datatable "> 
							<tr class="odd">
								<td><label for="RRN"><strong>RRN</strong></label></td>
								<td>${responseJSON1.RRN} </td> 
							</tr>
							<tr class="even">
								<td ><label for="Response Code"><strong>Merchant Id</strong></label></td>
								<td > ${responseJSON1.mid} </td>
							</tr>
							<tr class="odd">
								<td ><label for="Response Code"><strong>Terminal Id</strong></label></td>
								<td > ${responseJSON1.tid} </td>
							</tr>
							<tr class="even">
								<td ><label for="Response Code"><strong>Transaction Amount</strong></label></td>
								<td > ${responseJSON1.amt} </td>
							</tr> 
							<tr class="odd">
								<td ><label for="Response Code"><strong>Card Number</strong></label></td>
								<td > ${responseJSON1.maskedcard} </td>
							</tr>
							<tr class="even">
								<td ><label for="Response Code"><strong>Authorized By</strong></label></td>
								<td > ${responseJSON1.authid} </td>
							</tr>
							<tr class="odd">
								<td ><label for="Response Code"><strong>Service Name</strong></label></td>
								<td > ${responseJSON1.bankname} </td>
							</tr>
							<tr class="even">
								<td ><label for="Response Code"><strong>Branch Location</strong></label></td>
								<td > ${responseJSON1.location} </td>
							</tr>
							<tr class="odd">
								<td ><label for="Response Code"><strong>Transaction Type</strong></label></td>
								<td > ${responseJSON1.txntype} </td>
							</tr>
							<tr class="even">
								<td ><label for="Response Code"><strong>Response Code</strong></label></td>
								<td > ${responseJSON1.responseCode} </td>
							</tr>
						</table> 
					</fieldset>  
				
				</div>
			</div>
		</div> 
	 	<div class="form-actions"> 
			 &nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" 
			 id="btn-cancel" value="Back" width="100" onclick="cancel()"></input>
		</div> 
	</div> 
</form> 
</body>
</html>