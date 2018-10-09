
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

$(function(){
	var status="${responseJSON.STATUS}";
	
	if(status=="Rejected"){
		$("#res").show();
	}else{
		$("#res").hide();
	}
});

function createSubService(){

	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/marketsCheckerAct.action';
	$("#form1").submit();
	return true;
}



</script>
 

</head>

<body>
	<form name="form1" id="form1" method="post" action="">
	 
		<div id="content" class="span10">
            			<!-- content starts -->
			    <div>
						<ul class="breadcrumb">
						  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
						  <li> <a href="#">Markets</a> <span class="divider"> &gt;&gt; </span></li>
						  <li><a href="#">View Online Product</a></li>
						</ul>
				</div>
				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
							<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>View Online Product
								<div class="box-icon">
										<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
										<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div>

							<div id="primaryDetails" class="box-content">
								<fieldset>
									<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
										<tr class="even">
											<td ><strong><label for="Service ID">Product Id</label></strong></td>
											<td> ${responseJSON.PRODUCT_ID}
											</td>
											<td ><strong><label for="Service ID">Product Name</label></strong></td>
											<td> ${responseJSON.PRODUCT_NAME}
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Service Name">Product Price</label></strong></td>
											<td> ${responseJSON.PRODUCT_PRICE}
											</td>
											<td ><strong><label for="Service Name">Product Status</label></strong></td>
											<td> ${responseJSON.STATUS}
											</td>
										</tr>
										<tr class="even">
										<td ><strong><label for="Service Name">Category Id</label></strong></td>
											<td> ${responseJSON.CATEGORY_ID}
											</td>
										<td ><strong><label for="Sub Service Code">Category Desc</label></strong></td>
											<td > ${responseJSON.CATEGORY_DESC}
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Service Name">Sub Category Id</label></strong></td>
											<td> ${responseJSON.SUB_CATEGORY_ID}
											</td>
										<td ><strong><label for="Sub Service Code">Sub Category Desc</label></strong></td>
											<td > ${responseJSON.SUB_CATEGORY_DESC}
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Service Name">Created By</label></strong></td>
											<td> ${responseJSON.MAKER_ID}
											</td>
										<td ><strong><label for="Sub Service Code">Created Date</label></strong></td>
											<td > ${responseJSON.MAKER_DTTM}
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Service Name">Approved/Rejected By</label></strong></td>
											<td> ${responseJSON.CHECKER_ID}
											</td>
										<td ><strong><label for="Sub Service Code">Approved/Rejected Date</label></strong></td>
											<td > ${responseJSON.CHECKER_DTTM}
											</td>
										</tr>
										<tr style="display: none;" id="res">
											<td ><strong><label for="Sub Service Code">Rejected Reason</label></strong></td>
												<td colspan="3" >
													<b><font color="red"><span id="reason">${responseJSON.COMMENTS}</span></font></b>
												 </td>
										</tr>
									</table>
								</fieldset>
							</div>
					</div>
				</div> 
		<div class="form-actions">
			<a  class="btn btn-danger" href="#" onClick="createSubService()">Next</a>
		</div> 
	</div><!--/#content.span10--> 
</form>
</body>
</html>
