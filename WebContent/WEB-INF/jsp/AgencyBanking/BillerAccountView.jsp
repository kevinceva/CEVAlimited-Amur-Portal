
<!DOCTYPE html>

<%@taglib uri="/struts-tags" prefix="s"%> 

<html lang="en">
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
$(document).ready(function() {
	 		
}); 

function createSubService(){ 
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/newPayBillAct.action';
	$("#form1").submit();
	 
}


</script>
 
</head>

<body>
	<form name="form1" id="form1" method="post" action="">
	 
			<div id="content" class="span10"> 
			 
			    <div>
					<ul class="breadcrumb">
						<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
						<li><a href="#"> Biller Management</a><span class="divider"> &gt;&gt; </span></li>
						<li><a href="#"> Biller Account View</a></li>
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
								<i class="icon-edit"></i>Biller Account View
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
								</div>
							</div> 
							<div id="bankAccountInformation"  class="box-content"> 
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable " id="bank-details">
									<tr class="odd">
										<td width="20%"><strong><label for="Bank Code">Biller Id</label></strong></td>
										<td width="30%"> ${responseJSON1.BILLER_ID} </td>
									</tr>
									<tr class="odd">
										<td width="20%"><strong><label for="Bank Code">Biller Code</label></strong></td>
										<td width="30%"> ${responseJSON1.BILLER_CODE} </td>
									</tr>
									<tr class="odd">
										<td width="20%"><strong><label for="Bank Code">Biller Name</label></strong></td>
										<td width="30%"> ${responseJSON1.NAME} </td>
									</tr>
									<tr>
										<td width="20%"><strong><label for="Bank Name">Account Number</label></strong></td>
										<td width="30%">${responseJSON1.ACCOUNT_NUMBER}</td>
									</tr>
									<tr class="even">
										<td ><strong><label for="Bin">Account Name</label></strong></td>
										<td>${responseJSON1.ACCOUNT_NAME} </td>
									</tr>
									<tr>
										<td ><strong><label for="Bin Desc">Account Type</label></strong></td>
										<td> ${responseJSON1.ACCOUNT_TYPE} </td>
									</tr>
									 
								</table>
							</div>
										
							</div>
						</div>
					 
					<div class="form-actions">  
						<a  class="btn btn-danger" href="#" onClick="createSubService()">Next</a>
					</div> 
			</div>
	 
</form>
</body>
</html>
