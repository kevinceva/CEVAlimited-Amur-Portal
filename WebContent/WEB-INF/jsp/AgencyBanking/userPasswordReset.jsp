
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 
    
<style type="text/css">
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
.errmsg {
	color: red;
}
.errors {
	color: red;
}
 
.messages {
  font-weight: bold;
  color: green;
  padding: 2px 8px;
  margin-top: 2px;
}

.errors{
  font-weight: bold;
  color: red;
  padding: 2px 8px;
  margin-top: 2px;
}
</style>
<SCRIPT type="text/javascript">  
	
var toDisp = '${type}';

$(document).ready(function(){
	 
	var userprofilerules = {
			rules : { 
				reason : { required : true }
			},		
			messages : { 
				reason : { 
							required : "Please Input Remarks." 
						}
			} 
		}; 
	 
	var userStatus = '${responseJSON.CV0013}';
	var text = "";
	
	if( userStatus == 'Active')
		text = "<a href='#' class='label label-success'  >"+userStatus+"</a>";
	else if( userStatus == 'De-Active')
		text = "<a href='#'  class='label label-warning'  >"+userStatus+"</a>";
	else if( userStatus == 'InActive')
		text = "<a href='#'  class='label label-info' >"+userStatus+"</a>";
	else if( userStatus == 'Un-Authorize')  
		text = "<a href='#'  class='label label-primary'   >"+userStatus+"</a>";
		
	$('#spn-user-status').append(text);
  
	$('#btn-submit').live('click',function() {   
		$("#form1").validate(userprofilerules); 
		if($("#form1").valid()) { 
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/resetPassword.action";
			$("#form1").submit();	
		} else {
			return false;
		}
	});  
	
});
 
</SCRIPT> 
</head>

<body>
	<form name="form1" id="form1" method="post">
	<!-- topbar ends -->
	
	 <div id="content" class="span10"> 
		 
		    <div> 
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="userGrpCreation.action?pid=7">User Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">User ${type}</a></li>
				</ul>
			</div>  
			
			<table height="3">
				 <tr>
					<td colspan="3">
						<div class="messages" id="messages"><s:actionmessage /></div>
						<div class="errors" id="errors"><s:actionerror /></div>
					</td>
				</tr>
			 </table>
		 	
		 	<div class="row-fluid sortable"> 
				<div class="box span12"> 
					<div class="box-header well" data-original-title>
						 <i class="icon-edit"></i>User Password Reset Confirm 
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
							
						</div>
					</div> 		
					<div class="box-content">
						<fieldset> 
							<table width="950"  border="0" cellpadding="5" cellspacing="1" 
								class="table table-striped table-bordered bootstrap-datatable">
								  <tr > 
										<td width="25%" ><label  for="User Id"><strong>User Id</strong></label></td>
										<td width="25%" >${responseJSON.CV0001}<input type="hidden" name="userId"  id="userId" value="${responseJSON.CV0001}" /> </td>  
										<td width="25%" ><label  for="Employee No"><strong>Employee No</strong></label></td>
										<td width="25%" >${responseJSON.CV0002}<input type="hidden" name="CV0002"  id="empNo" value="${responseJSON.CV0002}" /></td>
									</tr>
									<tr > 
										<td><label  for="First Name"><strong>First Name</strong></label></td>
										<td>${responseJSON.CV0003} <input type="hidden" name="CV0003"  id="firstName" value="${responseJSON.CV0003}" /></td> 
										<td><label  for="Last Name"><strong>Last Name</strong></label></td>
										<td> ${responseJSON.CV0004}<input type="hidden" name="CV0004"  id="lastName" value="${responseJSON.CV0004}" /></td>
									</tr>
									<tr > 
										<td><label  for="Telephone Res"><strong>Telephone(Res)</strong></label></td>
										<td>${responseJSON.CV0005}<input type="hidden" name="CV0005"  id="telephoneRes" value="${responseJSON.CV0005}" /></td> 
										<td><label  for="Telephone Off"><strong>Telephone(Off)</strong></label></td>
										<td>${responseJSON.CV0006}<input type="hidden" name="CV0006"  id="telephoneOff" value="${responseJSON.CV0006}" /></td>
									</tr>

									 <tr > 
											<td><label  for="Mobile"><strong>Mobile</strong></label></td>
											<td>${responseJSON.CV0007}<input type="hidden" name="CV0007"  id="mobile" value="${responseJSON.CV0007}" /></td> 
											<td><label  for="Fax"><strong>Fax</strong></label></td>
											<td>${responseJSON.CV0008}<input type="hidden" name="CV0008"  id="fax" value="${responseJSON.CV0008}" /></td>
									 </tr>

									<tr > 
									  <td><label  for="User Level"><strong>User Level</strong></label></td>
									  <td>
										${responseJSON.CV0009} <input type="hidden" name="CV0009"  id="adminType" value="${responseJSON.CV0009}" />
									 </td> 
									 <td><label  for="Office Location"><strong>Office Location</strong></label></td>
									  <td>
										${responseJSON.CV0010} <input type="hidden" name="CV0010"  id="officeLocation" value="${responseJSON.CV0010}" />
									   </td>
									</tr>
									<tr > 
										<td><label  for="Expiry Date"><strong>Expiry Date</strong></label></td>
										<td>
											${responseJSON.CV0011} <input type="hidden" name="CV0011"  id="expiryDate" value="${responseJSON.CV0011}" />
										</td> 
										<td><label  for="E-Mail"><strong>E-Mail</strong></label></td>
										<td>
											${responseJSON.CV0012} <input type="hidden" name="CV0012"  id="email" value="${responseJSON.CV0012}" />
										</td>
									</tr> 
									<tr > 
										<td><label  for="User Status"><strong>User Status</strong></label></td>
										<td>
											<span id="spn-user-status"></span> <input type="hidden" name="CV0013"  id="user_status" value="${responseJSON.CV0013}" />
										</td> 
										<td> &nbsp;</td>
										<td>
											 &nbsp;
										</td>
									</tr>
							</table> 
							
							<table width="950"  border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable"> 
										<tr > 
											<td colspan="2" width="25%"> <label for="Reason"><strong>Reason</strong></label></td>
											<td colspan="2" width="75%">
												 <input name="reason" id="reason" type="text" class="field" required="true"  />
											</td>  
										</tr>
										<tr class="odd"> 
											<td colspan="4">
												<span > <strong> Please click on confirm to reset the user password.</strong></span>
											</td>
										</tr>
								</table>
						</fieldset> 
						</div> 
				</div>  
			</div>
			<div class="form-actions">
		         <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Confirm" width="100" ></input>
				 &nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel" width="100" ></input>
 		    </div>  			 
	</div><!--/#content.span10-->
	 
 </form>
</body>
</html>

