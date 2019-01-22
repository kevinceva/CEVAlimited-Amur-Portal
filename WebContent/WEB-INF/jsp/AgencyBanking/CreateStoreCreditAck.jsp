
<!DOCTYPE html>
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
function getGenerateMerchantScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/creditManagementAct.action';
	$("#form1").submit();
	return true;
}

		
</script>
</head>
<body>
	<form name="form1" id="form1" method="post" action="">
	 
			<div id="content" class="span10">
			    <div>
					<ul class="breadcrumb"> 
					   <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					   <li> <a href="#">Float Management</a> <span class="divider">&gt;&gt; </span></li>
					   <li> <a href="#">Credit Management</a> <span class="divider"> &gt;&gt;</span></li>
					   <li><a href="#">Store Credit Acknowledgement</a></li>
					</ul>
				</div>
					<div class="row-fluid sortable">
						<div class="box span12">
							<div class="box-header well" data-original-title>Store Credit
								<div class="box-icon"> 
									<a href="#" class="btn btn-minimize btn-round" data-toggle="tooltip"><i class="icon-chevron-up"></i></a>
									<a href="#" class="btn btn-close btn-round" data-toggle="tooltip"><i class="icon-remove"></i></a> 
								</div>
							</div>  
						<div class="box-content">
							<fieldset> 
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
											class="table table-striped table-bordered bootstrap-datatable "> 
									 <tr>
										<td></td>
										<td >Credit Amount of kshs
												${responseJSON.storeCreditAmt} /= successfully assigned to Store
												${responseJSON.storeId} of Merchant ${responseJSON.merchantID}
												under Reference No ${responseJSON.referenceNo} .</td>
										<td ></td>	
									</tr>
								</table>  
							</fieldset> 
						</div>
					</div>
				</div>
				  
	<div class="form-actions">
		<a class="btn btn-danger" href="#"
			onClick="getGenerateMerchantScreen()">Next</a> &nbsp;&nbsp;
	</div> 
</div> 
</form>
</body>
</html>
