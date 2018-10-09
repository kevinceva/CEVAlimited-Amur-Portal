
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
  
<link rel="stylesheet" type="text/css" media="screen" href="<%=ctxstr%>/css/chosen.min.css" /> 
<script src="${pageContext.request.contextPath}/js/jquery.validate.min.js"></script> 
<script type="text/javascript" >
$(document).ready(function () {

var data="${selectUsers}";
var arr = data.split(',');


for ( var i = 0, l = arr.length; i < l; i++ ) {
		option=new Option();
		option.text=arr[i];
		option.value=arr[i];
		document.getElementById("userlist1").add(option);
}
});
function home(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/home.action';
	$("#form1").submit();
	return true;
}
function assignDashBoardLinksAck(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/userGrpCreation.action?pid=7';
	$("#form1").submit();
	return true;
}
</script> 
		
</head>

<body>
	<form name="form1" id="form1" method="post" action=""> 
			<div id="content" class="span10"> 
			    <div> 
					<ul class="breadcrumb">
					   <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					   <li> <a href="#">User Management</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">Dashboard Selection Acknowledge</a> </li>  
					</ul>
				</div>  
				
	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
					<i class="icon-edit"></i>Dashboard Acknowledge 
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
				</div>
			</div>  
			<div class="box-content">
				<fieldset> 
					<table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable ">
							<tr class="even">
										<td width="20%" ><label for="Group Id"><strong>Group Id</strong></label></strong></td>
								<td width="30%" > ${groupId}
									<input name="groupId" type="hidden" id="groupId" class="field" value=" ${groupId}" />
								</td>
								<td width="20%" ><label for="Store ID"><strong>Group Name</strong></label></td>
								 <td width="30%" > ${grpname}
									<input name="grpname"  type="hidden" id="groupId" class="field"  value=" ${grpname}" /> 
								</td>
							</tr>
					</table>
				</fieldset>
			</div>
			<div id="userDetails">
				<table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable ">
					<tr class="even">
						<td >
							<div align="center">
								<select multiple style="width:300px;height:150px;" size="10" name="userlist1" id="userlist1" disabled >
								</select>
							</div>
						</td>
						
					</tr>							
				</table> 
			</div>
					<input type="hidden" name="selectUsers" id="selectUsers" value=" ${selectUsers}"></input>
				</div>
			</div>
 		<div align="center">
			<a  class="btn btn-info" href="#" onClick="home()">Home</a> &nbsp;&nbsp;
			<a  class="btn btn-success" href="#" onClick="assignDashBoardLinksAck()">Next</a>
		 </div>	
</div> 
</form>	 
</body>
</html>
