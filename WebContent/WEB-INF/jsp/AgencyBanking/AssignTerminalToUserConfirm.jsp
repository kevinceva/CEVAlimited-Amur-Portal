<%@taglib uri="/struts-tags" prefix="s"%> 
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

<script type="text/javascript" >
$(document).ready(function() {
     
	var mydata ='${selectedUserText}';
	var dataarray = mydata.split("#");
	for(var i=0;i<dataarray.length;i++){
		var o = new Option(dataarray[i], dataarray[i]);
		$(o).html(dataarray[i]);
		$("#userSelectedList").append(o);
	}
	$.each(mydata.split("#"), function(i,e){
		$("#userSelectedList option[value='" + e + "']").prop("selected", true);
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

 
function getPreviousScreen(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/assignUserterminalBackAct.action';
	$("#form1").submit();
	return true;
}


function assignUserToTerminal(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/assignUserToTerminalAct.action';
	$("#form1").submit();
	return true;
}

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
	<form name="form1" id="form1" method="post" action="">	
	<!-- topbar ends -->
	
		
			<div id="content" class="span10"> 
			
			    <div>
					<ul class="breadcrumb">
					  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">Merchant Management</a> <span class="divider"> &gt;&gt; </span></li>
					  <li><a href="#">Assign Terminal to User</a></li>
					</ul>
				</div>
				<table height="2">
					 <tr>
						<td colspan="3">
							<div class="messages" id="messages"><s:actionmessage /></div>
							<div class="errors" id="errors"><s:actionerror /></div>
						</td>
					</tr>
				 </table>
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
									 <table width="950" border="0" cellpadding="5" cellspacing="1"  
											class="table table-striped table-bordered bootstrap-datatable ">
										<tr class="even">
											<td width="20%"><strong><label for="Merchant ID">Merchant ID</label></strong></td>
											<td width="30%"> ${merchantID}
												<input name="merchantID" type="hidden" id="merchantID" class="field" value=" ${merchantID}">
											</td>
											<td><strong><label for="Store ID">Store ID</label></strong></td>
											<td > ${storeId}
												<input name="storeId"  type="hidden" id="storeId" class="field"  value=" ${storeId}" readonly  > 
											</td>
										</tr>
										<tr class="odd">
											<td width="20%"><strong><label for="Terminal ID">Terminal ID</label></strong></td>
											<td colspan=3  > ${terminalID}
												<input name="terminalID" type="hidden"  id="terminalID" class="field" value="${terminalID}"  maxlength="8">
											</td> 
										</tr>
																
									</table>
							 </fieldset> 
					</div> 
					<div class="box-content" id="terminalUserDetails">
						<fieldset>
								 <table width="950" border="0" cellpadding="5" cellspacing="1"  
									class="table table-striped table-bordered bootstrap-datatable ">
								
								<tr class="even">
									<td ><strong><label for="Terminal ID">Users</label></strong></td>
									<td >
										<select multiple id="userSelectedList" name="userSelectedList" class="chosen-select" style="width: 300px;" disabled>
										</select> 
									</td>								
								</tr>
								<tr class="odd">
									<td width="20%"><strong><label for="Supervisor">Supervisor</label></strong></td>
									<td colspan=3  > ${supervisor}
										<input name="supervisor" type="hidden"  id="supervisor" class="field" value="${supervisor}"  maxlength="8">
									</td>
								</tr>
								<tr class="even">
									<td ><strong><label for="Admin">Admin</label></strong></td>
									<td >${admin} </td>	
									<input name="admin" type="hidden"  id="admin" class="field" value="${admin}"  maxlength="8">
								</tr>							
							</table>
						</fieldset> 
					</div>
					</div>
					</div>
					<input type="hidden" name="selectedUserText" id="selectedUserText" value="${selectedUserText}"/>
					<input type="hidden" name="selectUsers" id="selectUsers" value='${selectUsers}' ></input>
					<input type="hidden" name="token" id="token" value="${responseJSON.token}"/>
					<div align="left">
						<a  class="btn btn-danger" href="#" onClick="getPreviousScreen()">Back</a> &nbsp;&nbsp;
						<a  class="btn btn-success" href="#" onClick="assignUserToTerminal()">Save</a>
					</div>	 
			</div>
       
</form>
 </body>
</html>
