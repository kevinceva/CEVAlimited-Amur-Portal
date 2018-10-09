<%@ page import="java.util.*"%><!DOCTYPE html>
<html>
<head>
<script type="text/javascript">
 var path = '${pageContext.request.contextPath}';
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
<link href="http://cdn.datatables.net/1.10.3/css/jquery.dataTables.css" rel="stylesheet" type="text/css">
<link href="http://datatables.net/release-datatables/extensions/ColVis/css/dataTables.colVis.css"  rel="stylesheet" type="text/css">
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="http://cdn.datatables.net/1.10.3/js/jquery.dataTables.min.js"></script>
<script src="http://datatables.net/release-datatables/extensions/ColVis/js/dataTables.colVis.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.dataTables.columnFilter.js"></script>
<title>Person Form</title>

<script type="text/javascript">

var table;

jQuery(document).ready(function() {
 table = $('#personTable').dataTable({
    "bPaginate": true,
    "iDisplayStart":0,
    "bProcessing" : true,
    "bServerSide" : true,
    "sAjaxSource" : path+"/com/pagination/JqueryDatatablePluginDemo.java"
 });
 });
 


</script>


</head>
<body>


<form>

		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Pagination Example</a></li>
				</ul>
			</div>
			
      		
			<div class="row-fluid sortable"><!--/span--> 
				<div class="box span12">
					  <div class="box-header well" data-original-title>Transaction Details
						  <div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					  </div>  
						<div class="box-content"> 
							<table width='100%' class="table table-striped table-bordered bootstrap-datatable "  
 								id="personTable" >
								<thead>
									<tr>
										<th>TXN ID</th>
									     <th>TXN DATE</th>
									     <th>MTI</th>
									     <th>PROCESSING CODE</th>
									     <th>TXN TYPE</th>
									     <th>AMOUNT</th>
									     <th>Action</th>
									</tr>
								</thead> 
								<tbody  >
								</tbody>
							</table>
						</div> 
					</div> 
 			</div> 
 </div>
 
</form>
</body>
 
</html>