
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

function createSubService(){

	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/marketsCheckerAct.action';
	$("#form1").submit();
	return true;
}

function approveReject(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/approveOrRejectOffer.action';
	$("#form1").submit();
	return true;
}

function getReason(){
	//alert("hello");
	var data=$("#approveOrReject").val();
	if(data=="R"){
		$("#res").show();
	}else{
		$("#res").hide();
	}
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
						  <li><a href="#"> Offer/Deal Approve</a></li>
						</ul>
				</div>
				<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
							<div class="box-header well" data-original-title>
								<i class="icon-edit"></i> Offer/Deal Information
								<div class="box-icon">
										<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
										<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div>

							<div id="primaryDetails" class="box-content">
								<fieldset>
									<table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable " >
										<tr class="even">
											<td ><strong><label for="Service ID">Offer ID</label></strong></td>
											<td colspan="3"> ${responseJSON.OFFER_ID}
												<input type="hidden" name="offerId" name="offerId" value="${responseJSON.OFFER_ID}" />
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
											<td colspan="3"> ${responseJSON.PRODUCT_PRICE}
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Service Name">Deal/Offer</label></strong></td>
											<td> ${responseJSON.OFFER_TYPE}
											</td>
											<td ><strong><label for="Service Name">Deal/Offer Status</label></strong></td>
											<td> ${responseJSON.STATUS}
											</td>
										</tr>
										
										<tr class="odd">
											<td ><strong><label for="Service Name">Time Limit</label></strong></td>
											<td> ${responseJSON.TIME_LIMIT}
											</td>
										<td ><strong><label for="Sub Service Code">End Date</label></strong></td>
											<td > ${responseJSON.END_DATE}
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Service Name">Discount/Cash Back</label></strong></td>
											<td> ${responseJSON.DISCOUNT_CASHBACK}
											</td>
										<td ><strong><label for="Sub Service Code">Discount(%) /Cash Back</label></strong></td>
											<td > ${responseJSON.DIS_CASHBACK_PER}
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Service Name">Create By</label></strong></td>
											<td> ${responseJSON.MAKER_ID}
											</td>
										<td ><strong><label for="Sub Service Code">Created Date</label></strong></td>
											<td > ${responseJSON.MAKER_DTTM}
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Service Name">Approve/Rejected By</label></strong></td>
											<td> ${responseJSON.CHECKER_ID}
											</td>
										<td ><strong><label for="Sub Service Code">Approve/Rejected Date</label></strong></td>
											<td > ${responseJSON.CHECKER_DTTM}
											</td>
										</tr>
										<tr class="odd">
											<td ><strong><label for="Service Name">Approved/Reject</label></strong></td>
											<td colspan="3"> 
												<select id="approveOrReject" name="approveOrReject" data-placeholder="Choose User Designation..." 
													class="chosen-select" style="width: 220px;" required=true  onChange="getReason()">
													<option value="">Select</option>
													<option value="A">APPROVE</option>
													<option value="R">REJECT</option>
												</select>
											</td>
										</tr>
										<tr class="odd" id="res" style="display: none">
											<td>Reason</td>
											<td colspan="3">
											<input type="text" name="reason" id="reason" /></td>
										</tr>	
									</table>
								</fieldset>
							</div>
					</div>
				</div> 
		<div class="form-actions">
			<a  class="btn btn-danger" href="#" onClick="createSubService()">Next</a> &nbsp;&nbsp;
			<a  class="btn btn-success" href="#" onClick="approveReject()">Confirm</a>
		</div> 
	</div><!--/#content.span10--> 
</form>
</body>
</html>
