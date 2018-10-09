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

var status = true;
var status1 = true;

$(function () { 

    $('#btn-submit').live('click', function () { 
	      var vuserId = $('#userId').val() == undefined ? ' ' : $('#userId').val(); 
	      var vmacAddress = $('#macAddress').val() == undefined ? ' ' : $('#macAddress').val(); 
	      var vlanIp =  $('#lanIp').val() == undefined ? ' ' : $('#lanIp').val(); 
			if(vuserId == '')
			{
				alert('please Enter UserId!');
				return false;
	 		}
			 
			
			if(status && status1) {
				var url="${pageContext.request.contextPath}/<%=appName %>/insertWebloginAction.action"; 
				$("#form1")[0].action=url;
				$("#form1").submit(); 
				//postData("insertWebloginAction.action","");
			} else {
				alert("Invalid User Id.");
			}
    }); 
});  

function postData(actionName,paramString){
	$('form').attr("action", actionName)
			.attr("method", "post"); 
	$('form').submit();	
}
function checkUserID()
{
 	 var formInput='userId='+$('#userId').val()+'&method=userIdStatus';
	   $.getJSON('postJson.action', formInput,function(data) {
	     	//var json = data.responseJSON.MERCHANT_CHECK_INFO.RESULT_COUNT;
	     	//console.log(data);
	     	var resultCnt=data.responseJSON.RESULT_COUNT; 
	     	var userResultCnt = data.responseJSON.RESULT_COUNT_USER;
			if(resultCnt == 1 ) {
				alert("User already exists with "+$('#userId').val());
				status = false;
			} 
			
			if(userResultCnt == 0) {
				alert("Invalid User.");
				status1 = false;
			}
	   });
	   
}

</script>
</head>
<body> 
 <form name="form1" id="form1" method="post" action=""> 
	
		
	<div id="content" class="span10"> 
			    <div> 
					<ul class="breadcrumb">
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">User Locking System</a>  </li> 
					 
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
					<i class="icon-edit"></i>User Details
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

				</div>
			</div>  
		<div class="box-content">
			<fieldset> 
			 <table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
					<tr class="even">
						<td ><strong><label for="User ID">User ID<font color="red">*</font></label></strong></td>
						<td ><input name="userId" id="userId" class="field" type="text" onblur="checkUserID()" required="required" autocomplete="off"/></td>
					</tr>
					<tr class="odd">
						<td ><strong><label for="Mac Address">Mac Address </label></strong></td>
						<td ><input name="macAddress" id="macAddress" class="field" type="text" required="required" autocomplete="off"/></td>
					</tr>
					<tr class="even">
						<td ><strong><label for="Lan IP">Lan IP </label></strong></td>
						<td ><input name="lanIp" id="lanIp" class="field" type="text" required="required" autocomplete="off"/></td>
					</tr>
					 
	 		</table>
	 	</fieldset> 
	 </div>
	 </div> 
</div>
<div class="form-actions">
   <input type="button" class="btn btn-success" name="btn-submit" id="btn-submit" value="Submit" width="100"  ></input> 
	&nbsp;<input type="button" class="btn" type="text"  name="btn-cancel" id="btn-cancel" value="Cancel" width="100" ></input> 
</div> 
</div>
 
</form> 
</body>
 
</html>