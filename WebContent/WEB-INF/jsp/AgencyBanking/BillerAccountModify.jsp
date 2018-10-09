<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="">
 <%@taglib uri="/struts-tags" prefix="s"%> 
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%String ctxstr = request.getContextPath(); %>
<%String appName = session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>

  
<script type="text/javascript" > 
  
var val = 1;
var rowindex = 0;
var colindex = 0;
var bankAcctFinalData="";
var binstatus = "";
var v_message = "";

$(document).ready(function() {
		
			
		$('#btn-back').live('click', function () { 
			$("form").validate().cancelSubmit = true; 
			var url="${pageContext.request.contextPath}/<%=appName %>/newPayBillAct.action"; 
			$("#form1")[0].action=url;
			$("#form1").submit(); 
		});
		
		$('#btn-submit').live('click', function () { 
			$("form").validate().cancelSubmit = true; 
			var url="${pageContext.request.contextPath}/<%=appName %>/billerAccountModifySubAct.action"; 
			$("#form1")[0].action=url;
			$("#form1").submit(); 
		});
	
		var config = {
		      '.chosen-select'           : {},
		      '.chosen-select-deselect'  : {allow_single_deselect:true},
		      '.chosen-select-no-single' : {disable_search_threshold:10},
		      '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
		      '.chosen-select-width'     : {width:"95%"}
		    }
		    for (var selector in config) {
		      $(selector).chosen(config[selector]);
		    }
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
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#"> Pay Bill</a></li><span class="divider"> &gt;&gt; </span>
					<li><a href="#"> Biller Account Modify</a></li>
				</ul>
			</div>
			<div class="row-fluid sortable"><!--/span--> 
					<div class="box span12"> 
						<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Biller Account Modify
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
										<td width="20%"><strong><label for="Bank Code">Biller Id<font color="red">*</font></label></strong></td>
										<td width="30%"> ${responseJSON1.BILLER_ID}
											<input name="billerId" type="hidden"  id="billerId" class="field" value='${responseJSON1.BILLER_ID}' >
										</td>
									</tr>
									<tr class="odd">
										<td width="20%"><strong><label for="Bank Code">Biller Name<font color="red">*</font></label></strong></td>
										<td width="30%"> ${responseJSON1.NAME}
											<input name="billerName" type="hidden"  id="billerName" class="field" value='${responseJSON1.NAME}' >
										</td>
									</tr>
									<tr class="odd">
										<td width="20%"><strong><label for="Bank Code">Biller Code<font color="red">*</font></label></strong></td>
										<td width="30%"> ${responseJSON1.BILLER_CODE}
											<input name="billerCode1" type="hidden"  id="billerCode1" class="field" value='${responseJSON1.BILLER_CODE}' >
										</td>
									</tr>
									<tr>
										<td width="20%"><strong><label for="Bank Name">Account Number<font color="red">*</font></label></strong></td>
										<td width="30%"> ${responseJSON1.ACCOUNT_NUMBER} 
														<input name="accountNumber" type="hidden"  id="accountNumber" class="field" value='${responseJSON1.ACCOUNT_NUMBER}'  readonly >
										</td>
									</tr>
									<tr class="even">
										<td ><strong><label for="Bin">Account Name<font color="red">*</font></label></strong></td>
										<td><input name="accountName" type="text" id="accountName" class="field"   value='${responseJSON1.ACCOUNT_NAME}' required='true'></td>
									</tr>
									<tr>
										<td ><strong><label for="Bin Desc">Account Type<font color="red">*</font></label></strong></td>
										<td><input name="accountType" type="text" id="accountType" class="field"   value='${responseJSON1.ACCOUNT_TYPE}' required='true'></td>
									</tr>
									 
								</table>
							 </fieldset> 
							</div>
							 
						</div>
					</div>
						 
				 <div class="form-actions"> 
					<input type="button" class="btn btn-info" name="btn-submit" id="btn-submit" value="Submit" />&nbsp;
					<input type="button" class="btn" name="btn-back" id="btn-back" value="Back" />
				</div> 
	</div> 
</form>
</body>
</html>
