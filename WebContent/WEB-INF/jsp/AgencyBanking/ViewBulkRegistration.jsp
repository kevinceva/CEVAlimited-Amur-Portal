
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<%String ctxstr = request.getContextPath(); %>
<%String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<%@taglib uri="/struts-tags" prefix="s"%> 
<style type="text/css">
.errors {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
input#abbreviation{text-transform:uppercase};
</style>
<SCRIPT type="text/javascript">  

$(document).ready(function(){     
	 
	$('#btn-submit').live('click',function() {   
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/home.action";
		$("#form1").submit();	 
	}); 
	
	$('#btn-Cancel').live('click',function() {  
 		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/viewOrganization.action?pid=130";
		$("#form1").submit();					
	}); 
	
});
 
</SCRIPT>  
</head> 
<body>
	<form name="form1" id="form1" method="post" autocomplete="off">
	  <div id="content" class="span10"> 
			 
		    <div> 
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li><a href="viewOrganization.action?pid=130">E-Wallet</a><span class="divider"> &gt;&gt; </span> </li>
				   <li><a href="#">View Organization</a></li>
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
				 <i class="icon-edit"></i>Basic Details  
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					
				</div>
			</div>
						
			<div class="box-content">
				<fieldset> 
				<table width="98%"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " > 
						 <tr> 
							<td width="20%"><label for="Organization Name"><strong>Organization Name</strong></label></td>
							<td width="30%"><s:property value='bulkBean.name' /> <input type="hidden" name="name"  id="name"   value="<s:property value='bulkBean.name' />"   />   </td>
							<td width="20%" ><label for="Abbreviation"><strong>Abbreviation</strong></label></td>
							<td width="30%" ><s:property value='bulkBean.abbreviation' /> <input type="hidden" name="abbreviation"  id="abbreviation" value="<s:property value='bulkBean.abbreviation' />" /></td>						 
						</tr> 
						<tr > 
							<td><label for="Full Name"><strong>Full Name</strong></label></td>
							<td> <s:property value='bulkBean.fullName' /> <input type="hidden" name="fullName"  id="fullName" value="<s:property value='bulkBean.fullName' />"  /> </td> 
							<td><label for="Email"><strong>Email</strong></label></td>
							<td><s:property value='bulkBean.email' />  <input type="hidden" name="email"  id="email" value="<s:property value='bulkBean.email' />"   />  </td>
						</tr>
						<tr > 
							<td><label for="Mobile No"><strong>Mobile Number</strong></label></td>
							<td><s:property value='bulkBean.mobileNo' />  <input type="hidden" name="mobileNo"  id="mobileNo" value="<s:property value='bulkBean.mobileNo' />"  /> </td> 
							<td><label for="Phone Number1"><strong>Phone Number1 </strong></label></td>
							<td><s:property value='bulkBean.phoneNumber1' />  <input type="hidden" name="phoneNumber1"  id="phoneNumber1" value="<s:property value='bulkBean.phoneNumber1' />"  /> </td>
						</tr>
						<tr > 
							<td><label for="Phone Number2"><strong>Phone Number2 </strong></label></td>
							<td><s:property value='bulkBean.phoneNumber2' />  <input type="hidden" name="phoneNumber2"  id="phoneNumber2" value="<s:property value='bulkBean.phoneNumber2' />" /> </td> 
							<td><label for="Phone Number3"><strong>Phone Number3</strong></label></td>
							<td><s:property value='bulkBean.phoneNumber3' />  <input type="hidden" name="phoneNumber3"  id="phoneNumber3" value="<s:property value='bulkBean.phoneNumber3' />"  />  </td>
						</tr> 
						<tr >  
							<td><label for="Days To Disperse"><strong>Days To Disperse</strong></label></td>
							<td > <s:property value='bulkBean.daysToDisburse' />  <input type="hidden" name="daysToDisburse"  id="daysToDisburse" value="<s:property value='bulkBean.daysToDisburse' />"   />  </td>
							<td><label for="Email"><strong>Email</strong></label></td>
							<td><s:property value='bulkBean.email' />  <input type="hidden" name="email"  id="email" value="<s:property value='bulkBean.email' />"  />  </td>							
						</tr>
						<tr >  
							<td><label for="Created By"><strong>Created By</strong></label></td>
							<td > <s:property value='bulkBean.createdBy' />  <input type="hidden" name="createdBy"  id="createdBy" value="<s:property value='bulkBean.createdBy' />"   />  </td>
							<td><label for="Created Date"><strong>Created Date</strong></label></td>
							<td><s:property value='bulkBean.createdDate' />  <input type="hidden" name="createdDate"  id="createdDate" value="<s:property value='bulkBean.createdDate' />"  />  </td>							
						</tr> 
				</table>
			</fieldset>  
		</div>
	</div>
	</div>   
	<div class="form-actions" >
       <input type="button" class="btn btn-success" name="btn-submit" id="btn-submit" value="Home" width="100" ></input>&nbsp;
       <input type="button" class="btn btn-info" name="btn-Cancel" id="btn-Cancel" value="Back" width="100" ></input>
   </div>   					 
</div> 
 </form> 
</body>
</html> 