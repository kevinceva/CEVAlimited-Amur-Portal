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


<%-- var groupId='<%= session.getAttribute("userGroup") %>'; --%>

var table;

jQuery(document).ready(function() {
	
/* 	if(groupId=="CICMAKERS" || groupId=="CICCHECKER" || groupId=="EXTCCMAKER" ){
		$("#content").hide();
		$("#hide-dash").show();
	}else
	{
		//$("#hidedash").hide();
		$("#content").show();
		$("#hide-dash").hide();
	}	
	 */
	 
	 console.log(" welcome to manufacturer details dashboard ");
	  table = $('#personTable').dataTable({
	 
	 
    "bPaginate": true,
    "iDisplayStart":0,
    "iDisplayLength":100,
    "bProcessing" : true,
    "bServerSide" : true,
    "sAjaxSource" : path+"/com/ceva/pagination/ManufacturerDetailsServlet.java?searchVal="+$("#searchVal").val()
    		
 });
 
 
 
 	
 });
 
$(function() {
	$( "#sortable" ).sortable();
	$( "#sortable" ).disableSelection();
 
	$( "#sortable1" ).sortable();
	$( "#sortable1" ).disableSelection();
}); 
</script>


</head>
<body>


<form>
		
<div class="card mb-3">
    <div class="page-header">
        <div>
            <img class="header-icon" alt="Dashboard Icon" src="${pageContext.request.contextPath}/images/manufacturer.png">
            <label>All Manufacturers</label>
        </div>
    </div>
    <div class="breadcrumb">
        <li class="breadcrumb-item">
            <a href="home.action">Dashboard</a>
        </li>
        <li class="breadcrumb-item active"> 
            <a href="#">Manufacturer</a>
        </li>
    </div>
    <div class="body-content"> 
        <div class="content-panel">     
        <table  class="table table-striped table-bordered bootstrap-datatable datatable" id="personTable" >
            <thead>
                <tr>
                    <th>S No</th>
                    <th>Manufacturer Name</th>
                    <th>Contacts</th>
                    <th>Secondary Contacts</th>
                </tr>
            </thead> 
        <tbody>
        </tbody>
        </table>
        </div>
    </div>
 </div> 
 <input type="hidden" id="searchVal" name="searchVal" value="" />   
        		
			
		
		<!--div id="content" class="span12" >  
 
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>All Customers
					<div class="box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content" style="overflow-x: scroll;"> 
					<table  class="table table-striped table-bordered bootstrap-datatable datatable" style="width:100%;" id="personTable" >
						<thead>
							<tr>
								<th  style="font-size: 0.8em; font-family:Tahoma; ">S No</th>
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Manufacturer Name</th>
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Contacts</th>
								<th  style="font-size: 0.8em; font-family:Tahoma; ">Secondary Contacts</th>
							</tr>
						</thead> 
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		

	</div> 
	
 
	<input type="hidden" id="searchVal" name="searchVal" value="" /-->
</form>
</body>
 
</html>