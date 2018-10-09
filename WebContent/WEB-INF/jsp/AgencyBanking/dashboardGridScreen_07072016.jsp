
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Banking</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<%@taglib uri="/struts-tags" prefix="s"%> 	
 
<script type="text/javascript" > 
var usersList = new Array();
var groupsList = new Array(); 
function postData(actionName,paramString){
		$('form').attr("action", actionName)
				.attr("method", "post");
		
		var paramArray = paramString.split("&");
		
		$(paramArray).each(function(indexTd,val) { 
			var input = $("<input />").attr("type", "hidden").attr("name", val.split("=")[0]).val(val.split("=")[1]);
			$('form').append($(input));	 
		});	

		$('form').submit();	
}

$(document).ready(function () {

	$(document).on('click','a',function(event) {  
 		var queryString = "";
		var v_id=$(this).attr('id');
		var index1 = $(this).attr('index');  
		var parentId =$(this).parent().closest('tbody').attr('id'); 
		var searchTrRows = parentId+" tr"; 
		var searchTdRow = '#'+searchTrRows+"#"+index1 +' > td';
		
		var groupId = "";  
		var userId = "";  
		var action = "";
		
		$(searchTdRow).each(function(indexTd) {  
			if(indexTd == 1) {
				groupId = $(this).text(); 
			}
			if(indexTd == 2) {
				userId = $(this).text(); 
			} 
		}); 
			
		queryString += 'groupID='+groupId+'&userId='+userId;
		
		if(v_id == 'group-view') {
			queryString+='&type=GroupView';
		} else {
			queryString+='&type=View';
		}
		
		if(v_id == 'group-view' 
			|| v_id == 'row-view' ) {
			postData("userDashInformation.action",queryString);
		}
		//$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/userDashInformation.action"+;
	}); 
	var userGroupData ='${responseJSON.USER_GROUPS}'; 
	
	var json = jQuery.parseJSON(userGroupData);
	 
	var val = 1;
	var rowindex = 0;
	var colindex = 0;
	var addclass = "";
	
	$.each(json, function(index, v) {
	
		if(val % 2 == 0 ) {
			addclass = "even";
			val++;
		}
		else {
			addclass = "odd";
			val++;
		}  
		var rowCount = $('#userGroupTBody > tr').length;
		
		colindex = ++ colindex; 
		
		var appendTxt = "<tr class="+addclass+" id='"+rowindex+"' index='"+rowindex+"'> "+
		"<td  >"+colindex+"</td>"+
		"<td><a href='#' index='"+rowindex+"' id='group-view' >"+v.roleGroupId+"</a></span></td>"+
		"<td><a href='#' index='"+rowindex+"' id='row-view' >"+v.userId+"</a></span></td>"+		
		"<td> "+v.USER_MAKER_ID+" </span></td>"+
		"<td> "+v.USER_MAKER_DATE+" </span></td>"+
		"<td>"+v.roleGroupName+"</span> </td>"+ 
		"<td> "+v.GROUP_MAKER_ID+" </span></td>"+
		"<td> "+v.GROUP_MAKER_DATE+" </span></td></tr>";
	 
		$("#userGroupTBody").append(appendTxt);	
		rowindex = ++rowindex;  
		 
	});  
});  
</script> 
 
 
		
</head>

<body>
<form name="form1" id="form1" method="post" >	
	<div id="content" class="span10"> 
	  <div>
		 <ul class="breadcrumb">
			<li><a href="#">Home </a> <span class="divider"> &gt;&gt; </span></li>
			<li><a href="userGrpCreation.action?pid=7">User Management</a> <span class="divider"> &gt;&gt; </span> </li>
			<li><a href="#">Dashboard</a> </li>
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
		<div class="box span12" id="groupInfo">
                  <div class="box-header well" data-original-title>User Dash Board Information
					<div class="box-icon"> <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					 <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> </div>
				</div>
           		<div class="box-content">
 					<table width="100%" class="table table-striped table-bordered bootstrap-datatable datatable" 
						id="DataTables_Table_0"  >
								<thead>
									<tr >
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
								<tbody  id="userGroupTBody">
								</tbody>
							</table>
                    </div>
          </div>
		</div>  
	 
</div><!--/#content.span10--> 
</form> 
<script type="text/javascript" src='${pageContext.request.contextPath}/js/jquery.dataTables.min.js'></script>
<script type="text/javascript" src='${pageContext.request.contextPath}/js/datatable-add-scr.min.js'></script> 
</body>
</html>
