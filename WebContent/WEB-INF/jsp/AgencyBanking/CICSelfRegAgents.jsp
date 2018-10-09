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
	
		
	 table = $('#personTable').dataTable({
	 
	 
    "bPaginate": true,
    "iDisplayStart":0,
    "iDisplayLength":100,
    "bProcessing" : true,
    "bServerSide" : true,
    "sAjaxSource" : path+"/com/ceva/pagination/CICSelfRegAgentsServlet.java?searchVal="+$("#searchVal").val()
    		
		
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
	console.log("v_idddddd:"+v_id);
	//alert("v_idddddd:"+v_id);
 	var datastr = v_id;
	var arr = datastr.split('$');
	var acn = arr[0];
	var mob = arr[1];
	var queryString = 'mob='+mob+'&acn='+acn; 
	console.log("valueof vid:"+v_id+"-"+queryString);
	//alert("valueof vid:"+v_id+"-"+queryString);
	if (acn != undefined  )
	{	
		postData("selfregAgentDetails.action",queryString);
	}	
	 
	//postData("callcentercompact.action",queryString);
	
});
</script>


</head>
<body>


<form>
		
		
			
		
		<div id="content" class="span10" >  
			
			<!-- <div>
				 <ul class="breadcrumb">
					<li><a href="home.action">SMS Details</a> <span class="divider"> &gt;&gt; </span></li>
					
				</ul>
			</div> -->
 		<div id="sortable1"></div>
 		
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Champions
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
								<th>Created Date</th>
								<th>Champion Mobile No</th>
								<th>Champion Name</th>
								<th>Email ID</th>
								<th>Action</th>
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
</form>
</body>
 
</html>