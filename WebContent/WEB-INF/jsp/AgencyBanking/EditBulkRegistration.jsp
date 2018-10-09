
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
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
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

var billerrules = {
		rules : { 
			fullName : { required : true },
			email : { required : true  , email : true },
			mobileNo : { required : true, number : true  },
 			daysToDisburse : { required : true , number : true} 
		},		
		messages : { 
			fullName : { 
					required : "Please Enter Full Name."
				},
			email : { 
					required : "Please Enter Email Address.",
					email : "Invalid Email Address Entered."
				},
			 
			mobileNo : { 
						required : "Please Enter Mobile Number.",
						number : "Invalid Data Entered."
					} , 
			daysToDisburse : { 
				required : "Please Enter Day's To Disburse.",
				number : "Invalid Data Entered."
			}
		},
		errorElement: 'label'
	};

$(document).ready(function(){    
	 
	$("#mobileNo,#phoneNumber1,#phoneNumber2,#phoneNumber3,#daysToDisburse").keypress(function (e) {
		 //if the letter is not digit then display error and don't type anything
		 if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
			//display error message
			$("#"+$(this).attr('id')+"_err").html("Digits Only").show().fadeOut("slow");
				   return false;
			}
	   }); 
	
	$("#form1").validate(billerrules); 
	$('#btn-submit').live('click',function() {  
		if($("#form1").valid()) {
			$('#abbreviation').val($('#abbreviation').val().toUpperCase());
			$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/editBulkOrgDetailsConfirm.action";
			$("#form1").submit();	
			return true;
		} else {
			return false;
		}
						
	}); 
	
	$('#btn-Cancel').live('click',function() {  
		$("#form1").validate().cancelSubmit = true;
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/viewOrganization.action?pid=130";
		$("#form1").submit();					
	}); 
	
});

//For Closing Select Box Error Message_Start
$(document).on('change','select',function(event) {  
	 if($('#'+$(this).attr('id')).next('label').text().length > 0) {
		  $('#'+$(this).attr('id')).next('label').text(''); 
		  $('#'+$(this).attr('id')).next('label').remove();
	 } 
});
//For Closing Select Box Error Message_End

</SCRIPT>  
</head> 
<body>
	<form name="form1" id="form1" method="post" autocomplete="off">
	  <div id="content" class="span10"> 
			 
		    <div> 
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li><a href="viewOrganization.action?pid=130">E-Wallet</a><span class="divider"> &gt;&gt; </span> </li>
				   <li><a href="#">Edit Organization</a></li>
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
							<td width="20%"><label for="Organization Name"><strong>Organization Name<font color="red">*</font></strong></label></td>
							<td width="30%"><input type="text" name="name"  id="name"   value="<s:property value='bulkBean.name' />"  required="true" />  
								 <input type="hidden" name="orgId"  id="orgId" value="<s:property value='bulkBean.orgId' />"   />   </td>
							<td width="20%"><label for="Abbreviation"><strong>Abbreviation<font color="red">*</font></strong></label></td>
							<td width="30%" >  <input type="text" name="abbreviation"  id="abbreviation" value="<s:property value='bulkBean.abbreviation' />"  required="true"  /></td>						 
						</tr> 
						<tr > 
							<td><label for="Full Name"><strong>Full Name<font color="red">*</font></strong></label></td>
							<td>  <input type="text" name="fullName"  id="fullName" value="<s:property value='bulkBean.fullName' />"  required="true"/> </td> 
							<td><label for="Email"><strong>Email<font color="red">*</font></strong></label></td>
							<td> <input type="text" name="email"  id="email" value="<s:property value='bulkBean.email' />"  required="true" />  </td>
						</tr>
						<tr > 
							<td><label for="Mobile No"><strong>Mobile Number<font color="red">*</font></strong></label></td>
							<td> <input type="text" name="mobileNo"  id="mobileNo" value="<s:property value='bulkBean.mobileNo' />"  required="true"/> <span id="mobileNo_err" class="errmsg"></span></td> 
							<td><label for="Phone Number1"><strong>Phone Number1</strong></label></td>
							<td> <input type="text" name="phoneNumber1"  id="phoneNumber1" value="<s:property value='bulkBean.phoneNumber1' />"  /> <span id="phoneNumber1_err" class="errmsg"> </span></td>
						</tr>
						<tr > 
							<td><label for="Phone Number2"><strong>Phone Number2 </strong></label></td>
							<td> <input type="text" name="phoneNumber2"  id="phoneNumber2" value="<s:property value='bulkBean.phoneNumber2' />" /> <span id="phoneNumber2_err" class="errmsg"></span></td> 
							<td><label for="Phone Number3"><strong>Phone Number3</strong></label></td>
							<td> <input type="text" name="phoneNumber3"  id="phoneNumber3" value="<s:property value='bulkBean.phoneNumber3' />"  /> <span id="phoneNumber3_err" class="errmsg"></span> </td>
						</tr> 
						<tr >  
							<td><label for="Days To Disperse"><strong>Days To Disperse<font color="red">*</font></strong></label></td>
							<td colspan=3> <input type="text" name="daysToDisburse"  id="daysToDisburse" value="<s:property value='bulkBean.daysToDisburse' />"  required="true" />  <span id="daysToDisburse_err" class="errmsg"></span></td>
							 					
						</tr>
						<tr >  
							<td><label for="Created By"><strong>Created By</strong></label></td>
							<td><s:property value='bulkBean.createdBy' />  <input type="hidden" name="createdBy"  id="createdBy" value="<s:property value='bulkBean.createdBy' />"   />  </td>
							<td><label for="Created Date"><strong>Created Date</strong></label></td>
							<td><s:property value='bulkBean.createdDate' />  <input type="hidden" name="createdDate"  id="createdDate" value="<s:property value='bulkBean.createdDate' />"  />  </td>							
						</tr> 
				</table>
			</fieldset>  
		</div>	 
	</div>
	</div>   
	<div class="form-actions" >
      <input type="button" class="btn btn-success" name="btn-submit" id="btn-submit" value="Submit" width="100" ></input>&nbsp;
      <input type="button" class="btn btn-info" name="btn-Cancel" id="btn-Cancel" value="Back" width="100" ></input>
   </div>   					 
</div> 
 </form> 
</body>
</html> 