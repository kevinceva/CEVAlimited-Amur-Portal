
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
 
  
function clickBack(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/binManagementAct.action';
	$("#form1").submit();
	return true;
}
function clickConfirm(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/binAcitvateBlockConfirm.action';
	$("#form1").submit();
	return true;
}	

</script>
 
</head>

<body>
	<form name="form1" id="form1" method="post" action="">	
	<!-- topbar ends --> 
			<div id="content" class="span10">  
			    <div>
					<ul class="breadcrumb">
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">Bin Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Bin Activate/De-Activate </a></li>
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
	  	
				<div class="row-fluid sortable"><!--/span-->
        
					<div class="box span12">  
							<div class="box-header well" data-original-title>
									<i class="icon-edit"></i>Bin Details
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div>  
								
						<div class="box-content" id="primaryDetails"> 
						 <fieldset>  
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable " >
									<tr class="even">
										<td width="20%"><strong><label for="Merchant Name">Bank Code</label></strong></td>
										<td width="30%"> ${responseJSON.BANK_CODE}
										</td>
										<td width="20%"><strong><label for="Merchant ID">Bank Name</label></strong></td>
										<td width="30%"> ${responseJSON.BANK_NAME}
										</td>
									</tr>
									<tr class="odd">
										<td><strong><label for="Store Name">Bin</label></strong></td>
										<td> ${responseJSON.BIN}
											<input type="hidden"  name="bin" id="bin" value="${responseJSON.BIN}"/>
										</td>
										<td ><strong><label for="Store ID">Bin Description</label></strong></td>
										<td > ${responseJSON.BIN_DESC}										</td>	
									</tr>
									<tr class="even">
										<td ><strong><label for="Location">ZPK INDEX</label></strong></td>
										<td > ${responseJSON.ZPK_INDEX}
										</td>
										<td ></td>
										<td ></td>	
									</tr> 
									<tr class="odd">
										<td ><strong><label for="Location">Current Status</label></strong></td>
										<td > ${responseJSON.CURR_STATUS} 
											<input type="hidden"  name="status" id="status" value="${responseJSON.CURR_STATUS}"/>
										</td>
										<td ><strong><label for="Location">New Status</label></strong></td>
										<td > ${responseJSON.NEW_STATUS} 
											
										</td>
									</tr> 				
								</table>
								</fieldset>  
							</div> 
					</div> 
				</div>			
		<div class="form-actions">
			 <a  class="btn btn-danger" href="#" onClick="clickBack()">Back</a> &nbsp;&nbsp;
			<a  class="btn btn-success" href="#" onClick="clickConfirm()">Confirm</a>
		</div>
	</div> 
</form>
</body>
</html>
