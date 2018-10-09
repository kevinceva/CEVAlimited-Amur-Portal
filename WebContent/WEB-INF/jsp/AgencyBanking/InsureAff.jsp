<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"> 
<%@taglib uri="/struts-tags" prefix="s"%>  
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
 
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %> 
 

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
<script type="text/javascript">

var fromMessage = "Enter from Date";
var toMessage = "Enter to Date";

var fromDateRules={required: true};
var toDateRules={required: true};

var fromDateMessages = {required: fromMessage};
var toDateMessages = {required: toMessage};

var valid = {
		
		rules :{
			fromDate : fromDateRules,
			toDate : toDateRules
		},
		messages :{
			
			fromDate: fromDateMessages,
			toDate: toDateMessages
		}	
};
function queryUser()
{
	$("#form1").validate(valid);
	if($("#form1").valid())
	{
			$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/queryDeails.action'; 
			$("#form1").submit();
			
			return true;
	}
	else
	{
			return false;
	}
}


var list = "fromDate,toDate".split(",");
var datepickerOptions = {
				showTime: true,
				showHour: true,
				showMinute: true,
	  		dateFormat:'dd/mm/yy',
	  		alwaysSetTime: false,
	  		yearRange: '1910:2020',
			changeMonth: true,
			changeYear: true
};	 
$(function() {
	$(list).each(function(i,val){
		$('#'+val).datepicker(datepickerOptions);
	}); 
});

</script>

</head>


<body>
<form name="form1" id="form1" method="post"> 
		
			<div id="content" class="span10">  
			    <div> 
					<ul class="breadcrumb">
					  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
					  <li> <a href="#">Audit Trail</a>  </li> 
 					</ul>
				</div>  

				<table>
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
					<i class="icon-edit"></i>Audit Trail
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
					<td width="20%" ><label for="User Id"><span id="enname">User ID</span> <font color="red"></font></label></td>
					<td width="30%" >
						<input type="text" class="userID" id="userID" name="userID" />
					</td>
					
					<td width="20%" ><label for=""><span id="enadd"></span> <font color="red"></font></label> </td>
					<td width="30%" >&nbsp</td>
				</tr> 
				<tr class="even">
					<td ><label for="From Date">From Date<font color="red">*</font></label></td>
					<td > 
						<input type="text" maxlength="10"  class="fromDate" id="fromDate" name="fromDate" required=true />  							
					</td>
					<td  ><label for="To Date"><span id="enadd">To Date</span> <font color="red">*</font></label> </td>
					<td  >
						<input type="text" maxlength="10"   class="toDate" id="toDate" name="toDate" required=true />
					</td>
					
				</tr>  
		 </table>
		</fieldset> 
	 	  </div> 
	  </div>
	  </div> 
		<div class="form-actions">
				<td  colspan=4><input type="button"  class="btn btn-success"  name="save" id="save" value="Get Audit Report" onClick="queryUser()"></input></td>					 
		</div> 
	</div> 	 
 </form>
</body> 
</html>