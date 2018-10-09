
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
	
	 
<script type="text/javascript" >
 

var list = "validFrom,validThru,terminalDate".split(",");
var datepickerOptions = {
			showTime: false,
			showHour: false,
			showMinute: false,
  		dateFormat:'dd-mm-yy',
  		alwaysSetTime: false,
		changeMonth: true,
		changeYear: true
};


$(function() {
	$(list).each(function(i,val){
		$('#'+val).datepicker(datepickerOptions);
	});
});
 

function createInbox(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/InboxConfirmAct.action';
	$("#form1").submit();
	return true;
}	

function Back(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/inboxSubmitAct.action';
	$("#form1").submit();
	return true;
}	

</script>

	 
</head>

<body>
	<form name="form1" id="form1" method="post">
			
			<div id="content" class="span10">
 			    <div>
						<ul class="breadcrumb">
						  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
						  <li><a href="#">Inbox Confirmation</a></li>
						</ul>
				</div>
				
				<div class="row-fluid sortable">  
					<div class="box span12"> 
							<div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Inbox Information
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
							</div>
					</div>

					<div id="terminalDetails" class="box-content">
						<fieldset> 
							<table width="950" border="0" cellpadding="5" cellspacing="1"  
								class="table table-striped table-bordered bootstrap-datatable ">
								<tr class="even">
									<td ><strong><label for="To E-Mail">To </label></strong></td>
									<td> ${responseJSON.emailId}
										<input name="emailId" type="hidden" id="emailId" class="field" value="${responseJSON.emailId}" >
									</td>
								</tr>
								<tr class="odd">
									<td ><strong><label for="To E-Mail">CC</label></strong></td>
									<td> ${responseJSON.ccEmailId}
										<input name="ccEmailId" type="hidden" id="ccEmailId" class="field" value="${responseJSON.ccEmailId}" >
									</td>
								</tr> 
								<tr class="even">
									<td><strong><label for="Subject">Subject</label></strong></td>
									<td> ${responseJSON.mailSubject}
										<input name="mailSubject" type="hidden"  id="mailSubject" class="field" value="${responseJSON.mailSubject}"  >
									</td>
											
								</tr>
								<tr class="odd">
									<td ><strong><label for="Messages">Messages </label></strong></td>
									<td >
										${responseJSON.emailMessage}
										<input name="emailMessage" type="hidden"  id="emailMessage" class="field" value="${responseJSON.emailMessage}"  >
									</td>	
								</tr>
								<tr class="even">
									<td ><strong><label for="Refreence NO">Reference No </label></strong></td>
									<td >
										${responseJSON.storeCreditRefNo}
										<input name="referenceNo" type="hidden"  id="referenceNO" class="field" value="${responseJSON.storeCreditRefNo}"  >
									</td>	
								</tr>
							</table>
						</fieldset> 	 
					</div>
				 </div>
			</div>
	<div class="form-actions">
		<a  class="btn btn-danger" href="#" onClick="createInbox()">Confirm</a>
		<a  class="btn btn-danger" href="#" onClick="Back()">Back</a>
	</div> 
</div> 
 </form> 				            
</body>
</html>
