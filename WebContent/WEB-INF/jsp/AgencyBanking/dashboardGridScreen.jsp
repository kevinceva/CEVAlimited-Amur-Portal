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
    "bProcessing" : true,
    "bServerSide" : true,
    "sAjaxSource" : path+"/com/ceva/pagination/UserDashBoardServelt.java"
 });
 });
 


</script>


</head>
<body>


<form>

		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="#">Home </a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="userGrpCreation.action?pid=7">User Management</a> <span class="divider"> &gt;&gt; </span> </li>
					<li><a href="#">Dashboard</a> </li>
				</ul>
			</div>
 
  <div class="row-fluid sortable"><!--/span-->
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>User Dash Board Information
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content"> 
					<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable"  	id="personTable" >
						<thead>
							<tr>
								<th >S No</th>
								<th>Group ID</th>
								<th>User ID</th>
								<th>User Creator ID </th>
								<th>User Creation Date</th>
								<th>Group Name </th>
								<th>Group Creator ID </th>
								<th>Group Creation Date</th>
							</tr>
						</thead> 
						<tbody  >
						</tbody>
					</table>
				</div>
			</div>
		</div> 
		</div>
		<div align="center">
			<input type="button" id="non-printable" class="btn btn-success" onclick="JavaScript:window.print();" value="print" />
		</div>
		</div>
</form>
</body>
 
</html>