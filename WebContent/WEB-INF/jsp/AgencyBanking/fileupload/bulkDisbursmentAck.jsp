
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
		$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/bulkDisbursment.action';
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
					<li><a href="home.action">Home</a> <span class="divider">
							&gt;&gt; </span></li>
					<li><a href="viewOrganization.action">P-Wallet</a><span
						class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Bulk Disbursment Acknowledgement</a></li>
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
							<i class="icon-edit"></i>Upload Bulk Disbursment File
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
										<td ><strong>Bulk Disbursment File Uploaded Successfully.</strong></td>
									</tr>
 								</table> 
							 </fieldset>  
						</div> 
					</div>
				</div>  
				 
		<div class="form-actions">
		<a class="btn btn-success" href="#" id="btn-confirm">Next</a>
		<a class="btn btn-danger" href="#" id="btn-back">Home</a> 
	</div> 
</div>  
</form> 
</body>
</html>
