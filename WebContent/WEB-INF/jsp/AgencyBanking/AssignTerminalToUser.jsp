
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
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
  
<s:set value="responseJSON" var="respData"/> 
 <style type="text/css">
input.button_add {
    background-image: url(<%=ctxstr%>/images/Left.png); /* 16px x 16px */
    background-color: transparent; /* make the button transparent */
    background-repeat: no-repeat;  /* make the background image appear only once */
    background-position: 0px 0px;  /* equivalent to 'top left' */
    border: none;           /* assuming we don't want any borders */
    cursor: pointer;        /* make the cursor like hovering over an <a> element */
    height: 30px;            /*make this the size of your image */
    padding-left: 30px;      /*make text start to the right of the image */
    vertical-align: middle; /* align the text vertically centered */
}
input.button_add2 {
    background-image: url(<%=ctxstr%>/images/Right.png); /* 16px x 16px */
    background-color: transparent; /* make the button transparent */
    background-repeat: no-repeat;  /* make the background image appear only once */
    background-position: 0px 0px;  /* equivalent to 'top left' */
    border: none;           /* assuming we don't want any borders */
    cursor: pointer;        /* make the cursor like hovering over an <a> element */
    height: 30px;           /* make this the size of your image */
    padding-left: 30px;     /* make text start to the right of the image */
    vertical-align: middle; /* align the text vertically centered */
}
</style>

<script type="text/javascript" > 

function getGenerateMerchantScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/generateMerchantAct.action';
	$("#form1").submit();
	return true;
}

var assignUserToTerminalRules = {
   rules : {
		supervisor : { required : true },
		admin : { required : true }
   },  
   messages : {
	supervisor : { 
			required : "Please select Supervisor."
        },
	admin : { 
			required : "Please select Admin."
        }
   } 
};
  
function assignUserToTerminal(){
	$("#form1").validate(assignUserToTerminalRules);
	var selectedUserText="";
	if($("#form1").valid()){
		var selectdata=$('#selectUsers').val();
		if(selectdata.length==0){
			alert("Please select atleast one user.");
			return false;
		}else{
				$("#selectUsers option:selected").each(function () {
				   var $this = $(this);
				   if ($this.length) {
					var selText = $this.text();
					selectedUserText += "#"+selText;
				   }
				});
				
				selectedUserText=selectedUserText.slice(1);
				$("#selectedUserText").val(selectedUserText);
				$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/assignUserTerminalCfmScreenAct.action';
				$("#form1").submit();
				return true;
		}
	}else{
			return false;
	}
}
</script>
<style type="text/css">
	label.error {
	   font-weight: bold;
	   color: red;
	   padding: 2px 8px;
	   margin-top: 2px;
	}
	div.errors {
	   font-weight: bold;
	   color: red;
	   padding: 2px 8px;
	   margin-top: 2px;
	}
</style>
	 
		
</head>

<body>
	<form name="form1" id="form1" method="post" action="">	
			<div id="content" class="span10">  
			    <div>
						<ul class="breadcrumb">
						  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
						  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
						  <li><a href="#">Assign Terminal to User</a></li>
						</ul>
				</div>
				<div class="row-fluid sortable"><!--/span-->
					<div class="box span12"> 
							<div class="box-header well" data-original-title>
									<i class="icon-edit"></i>Terminal Information
								<div class="box-icon">
									<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
									<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

								</div>
							</div>  
							<div class="box-content" id="terminalDetails">
								 <fieldset>
									 <table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable ">
										<tr class="even">
											<td width="20%"><strong><label for="Merchant ID">Merchant ID</label></strong></td>
											<td width="30%"> <s:property value="#respData.merchantID" /> 
												<input name="merchantID" type="hidden" id="merchantID" class="field" value="<s:property value="#respData.merchantID" />">
											</td>
											<td width="20%"><strong><label for="Store ID">Store ID</label></strong></td>
											<td width="30%"><s:property value="#respData.storeId" />
												<input name="storeId"  type="hidden" id="storeId" class="field"  value="<s:property value="#respData.storeId" />"  /> 
											</td>
										</tr>
										<tr class="odd">
											<td><strong><label for="Terminal ID">Terminal ID</label></strong></td>
											<td colspan=3> <s:property value="#respData.terminalID" />
												<input name="terminalID" type="hidden"  id="terminalID" class="field" value="<s:property value="#respData.terminalID" />" />
											</td> 
										</tr>
																		
									</table>
								 </fieldset>
							</div>
						 
							<div class="box-content"  id="userDetails">
								 <fieldset>
									 <table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable ">
										<tr class="even">
											<td width="20%"><strong><label for="Select Users">Select Users<font color="red">*</font></label></strong></td>
											<td colspan=3  >
												<div align="left">
													 <s:select cssClass="chosen-select" 
													    multiple="true"
  														headerValue="Select"
														list="#respData.USERS_LIST" 
														name="selectUsers" 
														value="%{#respData.exist_users}"
 														id="selectUsers" 
														requiredLabel="true" 
														theme="simple"
														data-placeholder="Select Users..." 
														style="width: 300px;"
														 />  
												</div>
											</td>
										</tr>	
									   </table>
								</fieldset> 
							</div>	   
							<div class="box-content"  id="userDetails">
								 <fieldset>
								   <table width="950" border="0" cellpadding="5" cellspacing="1"  class="table table-striped table-bordered bootstrap-datatable ">
										<tr class="even">
											<td width="20%"><strong><label for="Supervisor">Supervisor<font color="red">*</font></label></strong></td>
											<td colspan=3> 
												 <s:select cssClass="chosen-select" 
													    headerKey="" 
 														headerValue="Select"
														list="#respData.ADMIN_LIST" 
														name="supervisor" 
 														id="supervisor" 
														requiredLabel="true" 
														theme="simple"
														data-placeholder="Choose Supervisor..." 
														style="width: 300px;"
														 />  
											</td>
										</tr>
										<tr class="odd">
											<td width="20%"><strong><label for="Admin">Admin<font color="red">*</font></label></strong></td>
											<td colspan=3> 
												 <s:select cssClass="chosen-select" 
														headerKey=""
 														headerValue="Select"
														list="#respData.ADMIN_LIST" 
														name="admin" 
 														id="admin" 
														requiredLabel="true" 
														theme="simple"
														data-placeholder="Choose Admin..." 
														style="width: 300px;"
														 />  
											</td>
										</tr>
									</table>
								</fieldset> 
							</div>
						<input type="hidden" name="token" id="token" value="<s:property value="#respData.token" />"/>
						<input type="hidden" name="selectedUserText" id="selectedUserText" value='<s:property value="#respData.selectedUserText" />'/> 
					</div>
                </div>
				<div align="left">
						<a  class="btn btn-danger" href="#" onClick="getGenerateMerchantScreen()">Back</a> &nbsp;&nbsp;
						<a  class="btn btn-success" href="#" onClick="assignUserToTerminal()">Submit</a>
				</div>	 
		</div><!--/#content.span10--> 
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
