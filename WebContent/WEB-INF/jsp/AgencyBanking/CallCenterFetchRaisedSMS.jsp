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


<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>

<title>Person Form</title>

<script type="text/javascript">


var groupId='<%= session.getAttribute("userGroup") %>';

var table;

jQuery(document).ready(function() {
	
	var status = '${status}';
	console.log("sms-status status:"+status);
	$('#status').val(status);
	if(groupId=="CICMAKERS" || groupId=="CICCHECKER" || groupId=="CICADMINS" || groupId=="EXTCCMAKER" ){
		$("#content").hide();
		$("#hide-dash").show();
	}else
	{
		//$("#hidedash").hide();
		$("#content").show();
		$("#hide-dash").hide();
	}	
	
	 table = $('#personTable').dataTable({
	 
	 
    "bPaginate": true,
    "iDisplayStart":0,
    "iDisplayLength":100,
    "bProcessing" : true,
    "bServerSide" : true,
    "sAjaxSource" : path+"/com/ceva/pagination/RaisedSMSTransactionsServlet.java?status="+status+"&searchVal="+$("#searchVal").val()
 });
 
 
 /* $("input[type=search]").on('keyup',function() {
		var rrn=$(this).val();
		$("#searchVal").val(rrn);
		
	});   */
 
 	
 });
 
$(function() {
	$( "#sortable" ).sortable();
	$( "#sortable" ).disableSelection();
 
	$( "#sortable1" ).sortable();
	$( "#sortable1" ).disableSelection();
}); 


function postData(actionName,paramString) {
	$('#form').attr("action", actionName)
			.attr("method", "post");
	
	var paramArray = paramString.split("&");
	 
	$(paramArray).each(function(indexTd,val) {
		var input = $("<input />").attr("type", "hidden").attr("name", val.split("=")[0]).val(val.split("=")[1].trim());
		$('#form').append($(input));	 
	}); 
	$('#form').submit();	
}

$(document).on('click','a',function(event) {
	var v_id=$(this).attr('id') ;
	console.log("v_id 222"+v_id);
	var queryString = 'refno='+v_id; 
	 if (v_id != undefined  )
		{
			postData("callcentersmsdetaprv.action",queryString);
		} 
	//postData("querydetails.action",queryString);
	
});


</script>


</head>
<body>


<form>
		<div id="hide-dash" class="span11" style="display:none;">
			<div>
				 <ul class="breadcrumb">Welcome to Amur...</ul>
				 <img alt="" src="../images/dash.png"  style="width: 75%; margin-top: 5px;">
			</div>
		 </div>
		
			
		
		<div id="content" class="span11" style="display:none;">  
			
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">SMS Details</a> <span class="divider"> &gt;&gt; </span></li>
					
				</ul>
			</div>
<!--  		<div id="sortable1"></div>
 --> 		
			<div id="sortable1" class="row-fluid sortable" >
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>SMS Details
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content" style="overflow-x: scroll;"> 
					<table  class="table table-striped table-bordered bootstrap-datatable datatable"  	id="personTable" >
						<thead>
							<tr>
								<th width="7%">S No</th>
								<th>Requested ID</th>
								<th>Requested Date</th>
								<th>Request From</th>
								<th>Message</th>
								<th>Request To</th>
								<th>Remarks</th>
								<th>Raised By</th>
								<th>Raised Date</th>
							</tr>
						</thead> 
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		

	</div> 
	
 
	<input type="hidden" id="searchVal" name="searchVal" value="" />
	<input type="hidden" name="status" id="status">
</form>
</body>
 
</html>