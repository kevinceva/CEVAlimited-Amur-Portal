
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Banking</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@taglib uri="/struts-tags" prefix="s"%>  
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 
      
<script type="text/javascript" >  
$(document).ready(function () {

 	var userGroupData ='${responseJSON.USER_DETAILS}'; 
	
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
	 
		var user_status="";
		var status_class ="";
		var text = "";
		if(v.user_status == 'Active') {
			user_status = "<a href='#' class='label label-success' >"+v.user_status+"</a>";
			status_class = "btn btn-danger";
			text = "Deactivate";
		} else if(v.user_status == 'De-Active') {
			user_status = "<a href='#'  class='label label-warning'  >"+v.user_status+"</a>";
			status_class = "btn btn-success";
			text = "Activate";
		} else if(v.user_status == 'InActive') {
			user_status = "<a href='#'  class='label label-info'  >"+v.user_status+"</a>";
		} else if(v.user_status == 'Un-Authorize') {
			user_status = "<a href='#'  class='label label-primary'  >"+v.user_status+"</a>";
		}
		
		colindex = ++ colindex;
		var appendTxt = "<tr class="+addclass+" id='"+rowindex+"' index='"+rowindex+"' > "+
					"<td>"+colindex+"</td>"+ 
					"<td>"+v.user_id+"</span></td>"+ 
					"<td>"+v.username+"</span></td>"+ 
					"<td>"+v.empid+"</span></td>"+	
					"<td>"+v.expirydate+"</span></td>"+  
 					"<td>"+v.email+"</span> </td>"+ 
 					"<td>"+user_status+"</span></td>"+
					"  </tr>";
		$("#userGroupTBody").append(appendTxt);	
		rowindex = ++rowindex; 
		
	}); 
	
	$('#btn-submit').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/dashboardUsers.action";
		$("#form1").submit();					
	});  
	
});  
</script>
 
		
</head>

<body>
<form name="form1" id="form1" method="post" >	
	 
	 <div id="content" class="span10">

	 
		<!-- content starts -->
		  <div>
				 <ul class="breadcrumb">
					<li><a href="#">Home </a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="dashboardUsers.action">User Dashboard</a> <span class="divider"> &gt;&gt; </span> </li>
					<li><a href="#">Dashboard</a> </li>
				</ul>
		</div> 
		
	   <div class="row-fluid sortable"><!--/span--> 
			<div class="box span12" id="groupInfo">
					<div class="box-header well" data-original-title>Group Details
						<div class="box-icon"> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
						</div>
					</div>
			 
			<div class="box-content">
				<fieldset> 
					<table width="950"  border="0" cellpadding="5" cellspacing="1" 
						class="table table-striped table-bordered bootstrap-datatable " >
						 <tr > 
								<td width="25%" ><strong><label for="User Id"> Group Id</label></strong></td>
								<td width="25%" >${responseJSON.GROUP_ID} </td>  
								<td width="25%" ><strong><label for="Employee No">Group Name</label></strong></td>
								<td width="25%" > ${responseJSON.GROUP_NAME}</td>
							</tr>
							<tr > 
								<td><strong><label for="First Name">Authorized Id Creator</label></strong></td>
								<td>${responseJSON.MAKER_ID} </td> 
								<td><strong><label for="Last Name">Creation Date</label></strong></td>
								<td> ${responseJSON.MAKER_DTTM} </td>
							</tr> 
					</table>
				</fieldset>  
			</div>
		</div>
	 </div>  
     
	 <div class="row-fluid sortable"> 
		<div class="box span12" id="groupInfo">
                  <div class="box-header well" data-original-title>User Details
					<div class="box-icon"> <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> </div>
				  </div> 
					<div class="box-content">
					 <fieldset>
 						<table width="100%" class="table table-striped table-bordered bootstrap-datatable" 
								id="DataTables_Table_0">
							<thead>
								<tr>
									<th>S No</th>
									<th>User ID</th>
									<th>User Name</th>
									<th>Employee No</th>
									<th>Expiry Date</th>
									<th>Email</th>
									<th>Status</th>
								</tr>
							</thead>  
							<tbody  id="userGroupTBody">
							</tbody>
						</table>
					</fieldset>
				</div>
				
          </div>
       </div> 
	<div class="form-actions">
		<input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Next" width="100" ></input>
	</div> 
</div> 
</form>  
</body>
</html>
