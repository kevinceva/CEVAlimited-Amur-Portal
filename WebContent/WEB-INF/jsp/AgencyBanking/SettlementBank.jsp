<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Banking</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<% String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<%@taglib uri="/struts-tags" prefix="s"%> 

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

<s:set value="responseJSON" var="respData"/>
 
<script type="text/javascript" >
 
 
var settlementrules = {
		rules : { 
			bankid : { required : true  } 
		},		
		messages : { 
			bankid : { 
						required : "Please Select Bank Id." 
					}  
		} 
	}; 
$(document).ready(function () {    
	$('#btn-submit').live('click', function () {
		$("#error_dlno").text('');	 
		$("#form1").validate(settlementrules); 
		if($("#form1").valid()) {
			$('#bankname').val($('#bankid option:selected').text().split("~")[1]);
			$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/confirmSettlementBank.action';
			$("#form1").submit();
			return true;
		} 
	});  
});  
</script>  
</head> 
<body>
<form name="form1" id="form1" method="post" >	
	<div id="content" class="span10"> 
	  <div>
		 <ul class="breadcrumb">
			<li><a href="home.action">Home </a> <span class="divider"> &gt;&gt; </span></li>
			<li><a href="#">Add Bank To Settlement</a></li>
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
                  <div class="box-header well" data-original-title>Select Bank
					<div class="box-icon"> <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					 <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> </div>
				</div>
           		<div class="box-content">
				<fieldset> 
					<table width="950" border="0" cellpadding="5" cellspacing="1" 
							class="table table-striped table-bordered bootstrap-datatable " id="user-details">  
						<tr class="odd">
							<td width="10%"><label for="Bank Id"><strong>Select Bank<font color="red">*</font></strong></label></td>
							<td width="50%" colspan=3>
								 <s:select cssClass="chosen-select" 
									headerKey="" 
									headerValue="Select"
									list="#respData.bank_data"  
									name="bankid" 
									id="bankid" 
									value="bankid"
									theme="simple"
									data-placeholder="Choose Bank..."
									style="width: 420px;"   
									 />
									 
									 <input type="hidden" name="bankname" id="bankname" /> 
							</td>
							
						</tr>
						 
					</table>
				</fieldset> 
			</div>	 
		</div>
	</div> 
	<div class="form-actions"> 
		<input type="button" class="btn btn-success" type="text"  name="btn-submit" id="btn-submit" value="Submit" width="100" />
		&nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel" width="100" />
		<span id ="error_dlno" class="errors"></span> 
	</div>  
</div>  
</form>
<script type="text/javascript">
$(function(){ 
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
</body>
</html>
