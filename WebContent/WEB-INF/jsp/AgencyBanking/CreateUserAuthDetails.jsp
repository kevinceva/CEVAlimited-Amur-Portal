
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>IMPERIAL</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
 <%@taglib uri="/struts-tags" prefix="s"%>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 
<s:set value="responseJSON" var="respData"/>
<SCRIPT type="text/javascript"> 
var toDisp = '${type}';

$(document).ready(function(){
	 
	var actionLink = "";
	
	 
	var userStatus = '<s:property value="#respData.user_status" />';
	var text = ""; 
	
	
	if( userStatus == 'Active')
		text = "<a href='#' class='label label-success'  >"+userStatus+"</a>";
	else if( userStatus == 'De-Active')
		text = "<a href='#'  class='label label-warning' >"+userStatus+"</a>";
	else if( userStatus == 'InActive')
		text = "<a href='#'  class='label label-info'  >"+userStatus+"</a>";
	else if( userStatus == 'Not Authorized')
		text = "<a href='#'  class='label label-primary' >"+userStatus+"</a>";
	
	$('#spn-user-status').append(text);
  
	$('#btn-submit').live('click',function() {  
		
		var searchIDs="";
		 
 		$("div#merchant-auth-data input:radio:checked").each(function(index) {
 			searchIDs=$(this).val();
 			 $('#DECISION').val(searchIDs);
		});
		
		  if(searchIDs.length == 0) {
				$("#error_dlno").text("Please check atleast one record.");
			} else {
				$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/commonAuthRecordconfirm.action";
				$("#form1").submit();	
			}
	});  
	
  	$('#btn-back').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/commonAuthListAct.action";
		$("#form1").submit();	
		
	});  
  	
  	$('#btn-home').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/AuthorizationAll.action?pid=119";
		$("#form1").submit();	
		
	});  
  	
  	var auth=$('#STATUS').val(); 

	if ( auth == 'C' || auth == 'R'){
		$("#merchant-auth-data").hide();
		$("#btn-submit").hide();
	} 
		
}); 
</SCRIPT> 
</head> 
<body  >
	<form name="form1" id="form1" method="post">
 	 <div id="content" class="span10"> 
			 
		    <div> 
				<ul class="breadcrumb">
				 <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="userGrpCreation.action?pid=7">User Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#"> <s:property value="type" />  User </a></li>
				</ul>
			</div>  
		 	
		 	<div class="row-fluid sortable">
           
	<div class="box span12">
                            
		<div class="box-header well" data-original-title>
			 <i class="icon-edit"></i>User Details 
			<div class="box-icon">
				<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
				<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
			</div>
		</div>
						
		<div class="box-content">
			<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
					 <tr > 
							<td width="25%" ><label for="User Id"><strong>User Id</strong></label></td>
							<td width="25%" ><s:property value="#respData.user_id" />  
										<input type="hidden" name="CV0001"  id="userId" value="<s:property value="#respData.user_id" />" /> </td>  
							<td width="25%" ><label for="Employee No"><strong>Employee No</strong></label></td>
							<td width="25%" ><s:property value="#respData.emp_id" /> 
							 	<input type="hidden" name="CV0002"  id="empNo" value="<s:property value="#respData.emp_id" />" /></td>
						</tr>
						<tr > 
							<td><label for="First Name"><strong>First Name</strong></label></td>
							<td><s:property value="#respData.first_name" /> <input type="hidden" name="CV0003"  id="firstName" value="<s:property value="#respData.first_name" />" /></td> 
							<td><label for="Last Name"><strong>Last Name</strong></label></td>
							<td> <s:property value="#respData.last_name" /><input type="hidden" name="CV0004"  id="lastName" value="<s:property value="#respData.last_name" />" /></td>
						</tr>
						<tr > 
							<td><label for="Telephone Res"><strong>Telephone(Res)</strong></label></td>
							<td><s:property value="#respData.res_no" /> <input type="hidden" name="CV0005"  id="telephoneRes" value="<s:property value="#respData.res_no" />" /></td> 
							<td><label for="Telephone Off"><strong>Telephone(Off)</strong></label></td>
							<td><s:property value="#respData.off_no" /> <input type="hidden" name="CV0006"  id="telephoneOff" value="<s:property value="#respData.off_no" />" /></td>
						</tr>

						 <tr > 
								<td><label for="Mobile"><strong>Mobile</strong></label></td>
								<td><s:property value="#respData.mobile_no" /> <input type="hidden" name="CV0007"  id="mobile" value="<s:property value="#respData.mobile_no" />" /></td> 
								<td><label for="Fax"><strong>Fax</strong></label></td>
								<td><s:property value="#respData.fax" /> <input type="hidden" name="CV0008"  id="fax" value="<s:property value="#respData.fax" />" /></td>
						 </tr>

						<tr > 
						  <td><label for="User Level"><strong>User Level</strong></label></td>
						  <td>
							<s:property value="#respData.user_level" />
							 <input type="hidden" name="CV0009"  id="adminType" value="<s:property value="#respData.user_level" />" />
						 </td> 
						 <td><label for="Office Location"><strong>Office Location</strong></label></td>
						  <td>
							<s:property value="#respData.location" />
							<input type="hidden" name="CV0010"  id="officeLocation" value="<s:property value="#respData.location" />" />
						   </td>
						</tr>
						<tr > 
							<td><label for="Expiry Date"><strong>Expiry Date</strong></label></td>
							<td>
								<s:property value="#respData.expiry_date" />
								<input type="hidden" name="CV0011"  id="expiryDate" value="<s:property value="#respData.expiry_date" />" />
							</td> 
							<td><label for="E-Mail"><strong>E-Mail</strong></label></td>
							<td>
								<s:property value="#respData.email" />  
								<input type="hidden" name="CV0012"  id="email" value="<s:property value="#respData.email" />" />
							</td>
						</tr> 
						<tr > 
							<td><label for="User Status"><strong>User Status</strong></label></td>
							<td>
								<span id="spn-user-status"></span> 
 							</td> 
							<td> &nbsp;</td>
							<td>
								&nbsp;
							</td>
						</tr>
				</table>
			</fieldset>  
		</div>
		</div>
		</div>  
		
		<s:if test="STATUS != 'C'" > 
			 <div id="merchant-auth-data" > 
					<ul class="breadcrumb">
						 <li> <strong>Authorize&nbsp&nbsp&nbsp&nbsp&nbsp</strong> <input  name="authradio" id="authradio"  class='center-chk' type='radio' value='A' />&nbsp&nbsp </li>
						 <li> <strong>Reject&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp</strong> <input  name="authradio" id="authradio"  class='center-chk' type='radio' value='R' /> &nbsp&nbsp&nbsp</li>
					</ul>
			</div>  
		</s:if>
	
		 	
		<div class="form-actions">
			
			<s:if test="STATUS != 'C'" > 
				<input type="button" class="btn btn-success" name="btn-submit"
						id="btn-submit" value="Confirm"  /> 
			</s:if>
			<s:else>
				<input type="button" class="btn btn-success" name="btn-back"
						id="btn-home" value="Home"  /> 
			</s:else>
				
				<input type="button" class="btn btn-info" name="btn-back"
						id="btn-back" value="Back"  />

				<span id ="error_dlno" class="errors"></span>

  			   <input name="STATUS" type="hidden" id="STATUS" value="<s:property value="STATUS" />" />
  				<input name="AUTH_CODE" type="hidden" id="AUTH_CODE" value="<s:property value="AUTH_CODE" />"  />
				<input type="hidden" name="REF_NO" id="REF_NO" value="<s:property value="REF_NO" />"/>
				<input type="hidden" name="DECISION" id="DECISION" />  
			</div> 				 
	</div> 
 </form>
</body>
</html>

