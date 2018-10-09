 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Create Entity</title>
 
 
<script type="text/javascript">

var userrules = {
		rules : {
			userId : { required : true,  regex: /^[A-Z0-9]+$/ } 
		},		
		messages : {
			userId : { 
					required : "Please enter User Id.",
					regex : "User Id, can not contain any special characters."
				  } 
		} 
	};
$(function () {
	$('#userId').live('keyup',function(){
		 var id = $(this).attr('id');
		 var v_val = $(this).val(); 
		 $("#"+id).val(v_val.toUpperCase());
	});
	
	$.validator.addMethod("regex", function(value, element, regexpr) {          
		 return regexpr.test(value);
	   }, "");   
	
    $('#btn-submit').live('click', function () {  
		if($('#form1').validate(userrules)){
			$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/resetID.action';
			$("#form1").submit();
			return true; 
		} else {
			return false;
		} 
    }); 
}); 

</script>

<style type="text/css">
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
label.error {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
.errmsg {
color: red;
}
 
</style>  
</head>
<body>
<form name="form1" id="form1" method="post">  
	<div id="content" class="span10"> 
 		<div> 
			<ul class="breadcrumb">
			  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li> <a href="#">User Pin Generator</a> </li> 
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
				<table width="950" border="0" cellpadding="5" cellspacing="1" 
								class="table table-striped table-bordered bootstrap-datatable " id="user-details">
					<tr class="even">
						<td ><strong><label for="User ID">User ID<font color="red">*</font></label></strong></td>
						<td ><input name="userId" id="userId" class="field" type="text" required="true"/></td>
					</tr> 
				</table>
			</fieldset> 
		</div>
	</div>
	</div>
	 <div class="form-actions">
			<input type="button" name="btn-submit" class="btn btn-success" id="btn-submit" value="Submit" /> 
	</div>  
</div> 
</form> 
</body> 
</html>