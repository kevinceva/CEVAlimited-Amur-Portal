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
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<% String userGroup= session.getAttribute("userGroup").toString(); %>
<%@taglib uri="/struts-tags" prefix="s"%> 	
 
<script src="${pageContext.request.contextPath}/pagenationjs/jquery-1.12.2.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/jquery.dataTables.min.js"></script>
<script src="${pageContext.request.contextPath}/pagenationjs/dataTables.colVis.js"></script>


<title>Person Form</title>

<script type="text/javascript">

var usersList = new Array();
var groupsList = new Array(); 
var userLinkData ='${USER_LINKS}';
var jsonLinks = jQuery.parseJSON(userLinkData);
var linkIndex = new Array();
var linkName = new Array();
var linkStatus = new Array();

function postData(actionName,paramString){
	
	$('#form2').attr("action", actionName)
			.attr("method", "post");
	
	var paramArray = paramString.split("&");
	var input = "" ;
	$(paramArray).each(function(indexTd,val) {
		if(val != "") {
			input = $("<input />").attr("type", "hidden").attr("name", val.split("=")[0]).val(val.split("=")[1].trim());
			$('form').append($(input));	 
		}
	});	
	$('form').submit();
	
} 

$(document).ready(function () {
	var userGrp='<%=userGroup%>';
	
	//console.log(userGroupData);
	$.each(jsonLinks, function(index, v) {
		linkIndex[index] = index;
		linkName[index] = v.name;
		linkStatus[index] = v.status;
	});  
	
	 
	// Search For Top Layer
	$('#top-layer-anchor').find('a').each(function(index) {
		var anchor = $(this);   
		var flagToDo = false;
		 
		$.each(linkIndex, function(indexLink, v) {	 
 			if(linkName[indexLink] == anchor.attr('id'))  {
				flagToDo = true;
			} 
		}); 
		if(!flagToDo) {
			anchor.attr("disabled","disabled");
		} else {
			anchor.removeAttr("disabled");
		} 
	});
	
	//Search For The Links That Are Assigned To TABLE Level
	 $('table > tbody').find('a').each(function(index) {
		var anchor = $(this);   
		var flagToDo = false;
		 
		$.each(linkIndex, function(indexLink, v) {	 
 			if(linkName[indexLink] == anchor.attr('id'))  {
				flagToDo = true;
			} 
		}); 
		if(!flagToDo) {
			anchor.attr("disabled","disabled");
		} else {
			anchor.removeAttr("disabled");
		} 
		 
	});  
	 
});  


$(document).on('click','a',function(event) {
	var v_id=$(this).attr('id');
	var v_class=$(this).attr('class');
	//alert(v_id);
	
	if(v_id != 'SEARCH_NO') {
		var disabled_status= $(this).attr('disabled'); 
		var queryString = ''; 
		var v_action = "NO";
		var bin = "";
		var status = "";
		
		 var index1 = $(this).attr('index'); 
		/* var index1 = $(this).parent().closest('tr').attr('index'); */
		//alert("index1:::"+index1);
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		//alert("parentId::"+parentId);
		var searchTrRows = parentId+" tr"; 
		var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
				 
		if(disabled_status == undefined) {  
			
			//console.log(v_id);
			// Anchor Tag ID Should Be Equal To TR OF Index
			
			
			$(searchTdRow).each(function(indexTd) {  
				if (indexTd == 3) { 
					bin=$(this).text().trim();
				 }
				if (indexTd == 5) { 
					status=$(this).text().trim();
				 }
				//alert(bin+"----"+status);
			}); 
			
			queryString += 'bin='+bin+'&status='+status;  
			
			
			//alert(queryString);
			//alert(v_id);
			if (v_id ==  "bin-active-deactive" ) {  
 				v_action = "binInfoAct"; 
			}
			
		} else { 
			// No Rights To Access The Link 
		}
		
		
		var userGrp='<%=userGroup%>';
		//alert(userGrp);
		
		if(v_class =="paginate_button current" || v_class =="paginate_button " 
			|| v_class =="paginate_button" || v_class == "paginate_button previous disabled"
			|| v_class =="paginate_button previous" || v_class =="paginate_button next disabled" || v_class=="paginate_button next"){
		//alert("true");
		}else{
			//alert("not pagination"+v_action);
			if(v_action != "NO") {
				postData(v_action+".action",queryString);
			}
		}
		
	} else {
		// The below code is for quick searching.
		var txt_sr = $(this).attr('value');
		var parentId =$(this).parent().closest('table').attr('id');  
		
		$('div input[type=text]').each(function(){
			if($(this).attr("aria-controls") == parentId) { 
				$(this).val(txt_sr);
				$(this).trigger("keyup");
			}  
		});
	}
}); 


var table;

jQuery(document).ready(function() {
 table = $('#personTable').dataTable({
    "bPaginate": true,
    "iDisplayStart":0,
    "bProcessing" : true,
    "bServerSide" : true,
    "sAjaxSource" : path+"/com/ceva/pagination/BinManagementDashBoardServlet.java",
    "fnCreatedRow": function( nRow, aData, iDataIndex ) {
        $(nRow).attr('id', aData[0]);
    }
 });
 });
 


</script>


</head>
<body>


<form name="form1" id="form1" method="post">

		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Bin Management</a></li>
				</ul>
			</div>
			
			<!-- <div class="box-content" id="top-layer-anchor">
				 <div>
					<a href="#" class="btn btn-success" id="add-organization" title='Add New Organization' data-rel='popover' data-content='Creating a new E-Wallet, for organaization.'><i class='icon icon-plus icon-white'></i>&nbsp;Add New Organization</a>
					&nbsp;&nbsp;<a href="#" class="btn btn-info" id="profile-management" title='Profile Management' data-rel='popover' data-content='Profile Management.'><i class='icon icon-plus icon-white'></i>&nbsp;Profile Management</a>		 			
				</div>	
			</div> -->
			
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
	  
			<div class="row-fluid sortable">
			<div class="box span12" id="groupInfo">
				<div class="box-header well" data-original-title>Bin DashBoard
					<div class=
					"box-icon"> 
						<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
						<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
					</div>
				</div> 
				<div class="box-content"> 
					<table width='100%' class="table table-striped table-bordered bootstrap-datatable datatable"  	id="personTable" >
						<thead>
							<tr>
								<th>S No</th>
								<th>Bank Code</th>
								<th>Bank Name</th>
								<th>BIN</th>
								<th>BIN Description</th>
								<th>Status</th>
								<th>Created By</th>										
								<th>Created Date</th>
								<th>Actions</th> 
							</tr>
						</thead> 
						<tbody id="tData"  >
						</tbody>
					</table>
				</div>
			</div>
		</div> 
	</div>
	
</form>
<form name="form2" id="form2" method="post">
</form>
</body>
 
</html>