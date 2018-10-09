
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

function getresetIDPage(){
	$("#form1")[0].action='<%=request.getContextPath()%>/<%=appName %>/reSetIdAct.action';
	$("#form1").submit();
	return true;
} 
</script>

</head>

<body>
	<form name="form1" id="form1" method="post" action="">
	<!-- topbar ends -->
	 
	<div id="content" class="span10"> 
		 
			<div>
				<ul class="breadcrumb">
				  <li> <a href="#">Home</a> <span class="divider"> &gt;&gt; </span> </li>
				  <li> <a href="#">Transaction PIN Acknowledgement</a>  </li>
				</ul>
			</div>
			<div class="row-fluid sortable"><!--/span--> 
				<div class="box span12"> 
					 <div class="box-header well" data-original-title>
							<i class="icon-edit"></i>Transaction PIN Acknowledgement
						<div class="box-icon">
							<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a> 
							<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>

						</div>
					</div>  
					<div class="box-content" id="primaryDetails">  
					  <fieldset> 
							<table width="950" border="0" cellpadding="5" cellspacing="1" 
								class="table table-striped table-bordered bootstrap-datatable ">
								<tr class="even">
									<td ><strong> For ${userId} Transaction Pin Successfully Send Through Mail</strong></td>
								</tr>
							</table>
						 </fieldset> 
					</div> 
				</div> 
			</div> 	 
			<div class="form-actions">
				<a  class="btn btn-danger" href="#" onClick="getresetIDPage()">Next</a> 
			</div> 
	</div><!--/#content.span10--> 
</form> 
</body>
</html>
