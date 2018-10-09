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
<%String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>

  
<script type="text/javascript" > 

var registerBinRules = {
		   rules : {
				searchBy : { required : true} 
		   },  
		   messages : {
			  searchBy : { 
			   required : "Please Input Terminal Id / User Id / Branch Id."
				}
			} 
		 };
		 
$(document).ready(function() { 
		 
	$('#btn-submit').live('click', function () {   
		var url="${pageContext.request.contextPath}/<%=appName %>/registerBinSubmitAct.action"; 
		$("#form1")[0].action=url;
		$("#form1").submit();
	});  
		  
});    

 

</script>
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
</style> 
</head> 
<body>
	<form name="form1" id="form1" method="post" action=""> 
		
			<div id="content" class="span10"> 
			<div>
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="#">Float View</a>  </li>
 				</ul>
			</div>
			<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
						<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Register Bin
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

							</div>
						</div>

						<div id="primaryDetails" class="box-content">
							<fieldset>   
								<table width="950" border="0" cellpadding="5" cellspacing="1" 
									class="table table-striped table-bordered bootstrap-datatable " id="bank-details">
									<tr class="odd"> 
										<td width="20%"><label for="Bank Name"><strong>TERMINAL ID / USER ID / BRANCH ID<font color="red">*</font></strong></label></td>
										<td width="30%"><input name="bankName" type="text"  id="bankName" class="field" value=''  maxlength="50" 
										required='true'  ></td>
										<td width="20%"></td>
										<td width="20%"></td>
									</tr> 	 
								</table>
							 </fieldset> 
							</div> 
						</div>
					</div> 
				 
		 
			<div class="form-actions"> 
 				<input type="button" class="btn btn-info" name="btn-submit" id="btn-submit" value="Submit" />&nbsp;
				<input type="button" class="btn" name="btn-back" id="btn-back" value="Cancel" /> 
				&nbsp;<span id ="error_dlno" class="errors"></span>
			</div> 
	</div> 
</form>
</body>
</html>
