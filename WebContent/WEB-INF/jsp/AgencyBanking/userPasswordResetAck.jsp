
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
</style>
<SCRIPT type="text/javascript">  
var toDisp = '${type}';

$(document).ready(function(){ 
  
	$('#btn-submit').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/userGrpCreation.action?pid=7";
		$("#form1").submit();					
	});  
	
});
//--> 
</SCRIPT>
    
 
		
</head>

<body>
	<form name="form1" id="form1" method="post"> 
	<div id="content" class="span10"> 
		<div> 
			<ul class="breadcrumb">
			  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li> <a href="#">User Management</a> <span class="divider"> &gt;&gt; </span></li>
				  <li><a href="#">User ${type}</a></li>
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
				 <i class="icon-edit"></i>User Password Details Acknowledge
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
				</div>
			</div>
							
			<div class="box-content">
				<fieldset> 
					<table width="950"  border="0" cellpadding="5" cellspacing="1" 
						class="table table-striped table-bordered bootstrap-datatable " > 
							<tr class="odd"> 
								<td colspan="4"><strong>Password has been send to their respective email.</strong></td>  
							</tr>  
					</table>  
				</fieldset>  
			</div>
		</div> 
          </div>    						 
		<div class="form-actions">
			  <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Next" width="100" ></input>
			 
		</div> 
</div><!--/#content.span10--> 
 </form>
</body>
</html>

