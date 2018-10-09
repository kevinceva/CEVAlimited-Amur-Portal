
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>
<%@taglib uri="/struts-tags" prefix="s"%>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

	 
 <script type="text/javascript" > 
$(function(){		
	$('#btn-submit').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/userGrpCreation.action?pid=7";
		$("#form1").submit();				
	}); 
}); 
	//--> 
</script>
</head>
<body>
	<form name="form1" id="form1" method="post" action="">
	
		
		<div id="content" class="span10"> 
			 
			<ul class="breadcrumb">
				<li>
					<a href="home.action"><b>Home</b></a> <span class="divider">&gt;&gt;</span>
				</li>
				<li><a href="#"><b>User Management</b></a>  <span class="divider">&gt;&gt;</span>
				</li>
				<li><a href="#"><b>User Rights Acknowledge</b></a> 
				</li>				
			</ul> 
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
							<i class="icon-edit"></i>User Rights Acknowledge
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i
								class="icon-cog"></i></a> <a href="#"
								class="btn btn-minimize btn-round"><i
								class="icon-chevron-up"></i></a>

						</div>
					</div> 
					<div class="box-content">
						 <fieldset> 
							 <table width="100%" border="0" cellpadding="5" cellspacing="1" id="table-select"> 
								<tr class="odd"> 
									<td width="11%" ><strong> Rights assigned successfully for the User Id '${userId}'.</strong></td> 
								</tr>  
							</table>  
						</fieldset> 
					</div>
				</div>
			</div> 
			<div class="form-actions">
               <input type="button" class="btn btn-primary" type="text"  name="btn-submit" id="btn-submit" value="Next" width="100" ></input>
               &nbsp;<input type="button" class="btn" type="text"  name="save" id="save" value="Cancel" width="100" ></input> 
           </div> 
		</div>
		 
</form>
</body>
</html> 