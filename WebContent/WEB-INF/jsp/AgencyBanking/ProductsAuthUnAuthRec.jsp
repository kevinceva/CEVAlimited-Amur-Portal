<%@ page import="java.util.*"%><!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
 var path = '${pageContext.request.contextPath}';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="http://cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js"></script>
<script src="http://datatables.net/release-datatables/extensions/ColVis/js/dataTables.colVis.js"></script> -->

<script src="${pageContext.request.contextPath}/pagenationjs/jquery-1.12.2.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/dataTables.colVis.js"></script>


<title>Person Form</title>

<script type="text/javascript">

var table;

jQuery(document).ready(function() {
	
 var status="${status}";
 //alert(status);
 	if(status=="UN-AUTH"){
 		 table = $('#personTable').dataTable({
 		    "bPaginate": true,
 		    "iDisplayStart":0,
 		    "bProcessing" : true,
 		    "bServerSide" : true,
 		    "sAjaxSource" : path+"/com/ceva/pagination/ProductsUnAuthRecordsServlet.java"
 		 });	
 	}else if(status=="AUTH"){
 		table = $('#personTable').dataTable({
 		    "bPaginate": true,
 		    "iDisplayStart":0,
 		    "bProcessing" : true,
 		    "bServerSide" : true,
 		    "sAjaxSource" : path+"/com/ceva/pagination/ProductsAuthRecordsServlet.java"
 		 });
 	}else if(status=="REJECT"){
 		table = $('#personTable').dataTable({
 		    "bPaginate": true,
 		    "iDisplayStart":0,
 		    "bProcessing" : true,
 		    "bServerSide" : true,
 		    "sAjaxSource" : path+"/com/ceva/pagination/ProductsRejectedRecordsServlet.java"
 		 });
 	}else if(status=="DEAL-UN-AUTH"){
 		$("#offers").show();
 		$("#product").hide();
 		
 		table = $('#offerTable').dataTable({
 		    "bPaginate": true,
 		    "iDisplayStart":0,
 		    "bProcessing" : true,
 		    "bServerSide" : true,
 		    "sAjaxSource" : path+"/com/ceva/pagination/OfferDealUnAuthRecordsServlet.java"
 		 });
 	}else if(status=="DEAL-AUTH"){
 		$("#offers").show();
 		$("#product").hide();
 		
 		table = $('#offerTable').dataTable({
 		    "bPaginate": true,
 		    "iDisplayStart":0,
 		    "bProcessing" : true,
 		    "bServerSide" : true,
 		    "sAjaxSource" : path+"/com/ceva/pagination/OfferDealAuthRecordsServlet.java"
 		 });
 	}else if(status=="DEAL-REJECT"){
 		$("#offers").show();
 		$("#product").hide();
 		
 		table = $('#offerTable').dataTable({
 		    "bPaginate": true,
 		    "iDisplayStart":0,
 		    "bProcessing" : true,
 		    "bServerSide" : true,
 		    "sAjaxSource" : path+"/com/ceva/pagination/OfferDealRejectRecordsServlet.java"
 		 });
 	}
 });
 
function postData(actionName,paramString) {
	$('#form2').attr("action", actionName)
			.attr("method", "post");
	
	var paramArray = paramString.split("&");
	 
	$(paramArray).each(function(indexTd,val) {
		var input = $("<input />").attr("type", "hidden").attr("name", val.split("=")[0]).val(val.split("=")[1].trim());
		$('#form2').append($(input));	 
	}); 
	$('#form2').submit();	
}

$(document).on('click','a',function(event) {
	var status=$("#status").val();
	//alert(status);
	var productId=$(this).attr('id') ;
	//alert(productId);
	var queryString = 'status='+status; 
	
	if(status=="UN-AUTH"){
		queryString += '&productId='+productId; 
		postData("productsApproveAct.action",queryString);
	}else if(status=="AUTH" || status=="REJECT"){
		queryString += '&productId='+productId; 
		postData("productsInfoViewAct.action",queryString);
		
	}else if(status=="DEAL-UN-AUTH"){
		queryString += '&offerId='+productId; 
		postData("offersApproveAct.action",queryString);
	}else if(status=="DEAL-AUTH" || status=="DEAL-REJECT"){
		queryString += '&offerId='+productId; 
		postData("offersInfoViewAct.action",queryString);
		
	}
	
});


</script>


</head>
<body>


<form>

		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Markets</a></li>
				</ul>
			</div>
 
			<div class="row-fluid sortable" id="product">
				<div class="box span12" id="groupInfo">
					<div class="box-header well" data-original-title>Product Information
						<div class=
						"box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content"> 
						<table  class="table table-striped table-bordered bootstrap-datatable datatable"  	id="personTable" >
							<thead>
								<tr>
									 <th>S No</th>
								     <th>Product Id</th>
								     <th>Product Name</th>
								     <th>Price</th>
								     <th>Category</th>
								     <th>Sub Category</th>
								     <th>Status</th>
								     <th>Maker Id</th>
								     <th>Maker Date</th>
								</tr>
							</thead> 
							<tbody>
							</tbody>
						</table>
					</div>
			  </div>
		</div> 
		
		
		<div class="row-fluid sortable" id="offers" style="display: none;">
				<div class="box span12" id="groupInfo">
					<div class="box-header well" data-original-title>Deal/Offers Information
						<div class=
						"box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div> 
					<div class="box-content"> 
						<table  class="table table-striped table-bordered bootstrap-datatable datatable"  	id="offerTable" >
							<thead>
								<tr>
									 <th>S No</th>
									 <th>Offer Id</th>
								     <th>Product Id</th>
								     <th>Product Name</th>
								     <th>Product Price</th>
								     <th>Deal/Offer</th>
								     <th>Discount/Cash Back</th>
								     <th>Status</th>
								     <th>Maker Id</th>
								     <th>Maker Date</th>
								</tr>
							</thead> 
							<tbody>
							</tbody>
						</table>
					</div>
			  </div>
		</div>
		
		
		<input type="hidden" name="status" id="status" value="${status}" /> 
</form>
<form name="form2" id="form2" method="post">
</form>
</body> 
</html>