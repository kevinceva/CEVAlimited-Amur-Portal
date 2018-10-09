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
$(document).ready(function () {
    $('#save').on('click', function () {  
	      var userId = $('#userId').val() == undefined ? ' ' : $('#userId').val(); 
			if(userId == '') {
				alert('please Enter UserId!');
				return false;
	 		} else { 
				$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/resetID.action';
				$("#form1").submit();
				return true; 
			}
    });

}); 

</script>
</head>
<body>
	<form name="form1" id="form1"> 
		<div id="content" class="span10">  
		 
		<div>
			<ul class="breadcrumb">
			  <li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
			  <li><a href="serviceMgmtAct.action?pid=10">Transaction Pin</a>  </li> 
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
		<div class="row-fluid sortable"><!--/span--> 
			<div class="box span12"> 
				<div class="box-header well" data-original-title>Transaction Information
				  <div class="box-icon"> 
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a> 
				  </div>
				</div> 
				<div class="box-content"> 
					<fieldset>
						<table width="950" border="0" cellpadding="5" cellspacing="1" class="table">
							<tr>
								<td><strong><label for="User ID">User ID<font color="red">*</font></label></strong></td>
								<td><input name="userId" id="userId" class="field" type="text" /></td>
							</tr> 
						</table>
					</fieldset>
				</div>
            </div>
		</div>
	<div class="form-actions">
		 <input type="button"  class="btn btn-success"   name="save" id="save" value="Reset"   ></input> 
	</div>
</div> 
 </form> 
</body> 
</html>