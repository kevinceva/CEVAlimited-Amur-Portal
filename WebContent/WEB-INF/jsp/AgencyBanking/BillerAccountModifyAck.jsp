
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
 <%@taglib uri="/struts-tags" prefix="s"%> 
<style type="text/css">
.errors {
	  font-weight: bold;
	  color: red;
	  padding: 2px 8px;
	  margin-top: 2px;
}
</style>
<SCRIPT type="text/javascript">  
$(document).ready(function(){  
	 
	$('#btn-submit').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/newPayBillAct.action";
		$("#form1").submit();					
	}); 
	
	$('#btn-Cancel').live('click',function() {  
		$("#form1")[0].action="<%=request.getContextPath()%>/<%=appName %>/home.action";
		$("#form1").submit();					
	}); 
	
});
</SCRIPT>  
</head> 
<body>
	<form name="form1" id="form1" method="post">
	  <div id="content" class="span10"> 
			 
		    <div> 
				<ul class="breadcrumb">
				  <li> <a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li><a href="billerRegistration.action?pid=91">Biller Registration</a> <span class="divider"> &gt;&gt; </span> </li>
				   <li><a href="#">Biller Account Modify Acknowledgement </a>  </li>
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
				 <i class="icon-edit"></i>Biller Account Modify Acknowledge
				<div class="box-icon">
					<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
					
				</div>
			</div>
						
			<div class="box-content">
				<fieldset> 
				<table width="950"  border="0" cellpadding="5" cellspacing="1" 
					class="table table-striped table-bordered bootstrap-datatable " >
					  <tr > 
							<td> <strong>Biller Account details updated successfully.</strong> </td>
						</tr> 
				</table>
			</fieldset>  
		</div>
		</div>
		</div> 
         	<div class="form-actions" >
         		<input type="button" class="btn btn-primary" type="hidden"  name="btn-submit" id="btn-submit" value="Next" width="100" ></input>
          	 	&nbsp; <input type="button" class="btn " type="text"  name="btn-Cancel" id="btn-Cancel" value="Home" width="100" ></input>
            </div>  
               						 
	</div> 
 </form>
</body>
</html> 