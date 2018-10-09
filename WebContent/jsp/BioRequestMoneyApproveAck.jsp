<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<title>Ceva</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="Another one from the CodeVinci">
<meta name="author" content="Team">
<%@page import="com.ceva.base.common.utils.CevaCommonConstants"%>

<%String ctxstr = request.getContextPath(); %>
<% String appName= session.getAttribute(CevaCommonConstants.ACCESS_APPL_NAME).toString(); %>


<script type="text/javascript">

$(function(){
	
	
	$("#back").click(function(){
		$("#form1")[0].action="<%=ctxstr%>/<%=appName %>/bioAct.action";
		$("#form1").submit();
	});
	
});
</script>

</head>
<body  >
	<form name="form1" id="form1" method="get" action="" autocomplete="off">
		
		
		<div id="content" class="span10">  
			<div>
				 <ul class="breadcrumb">
					<li><a href="home.action">Home</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="home.action">Bio Activities</a> <span class="divider"> &gt;&gt; </span></li>
					<li><a href="#">Bio Request Money Approve</a></li>
				</ul>
			</div>
			
			<div>
					<ul class="breadcrumb">
						<li><a href="#" style="font-size: 15px;font-variant: inherit;">Bio Request Money  Approve</a></li>
					</ul>
				</div>
	
	
				<div class="row-fluid sortable" >
						<div class="box span12" >
						<div class="box-header well" data-original-title>
								<i class="icon-edit"></i>Bio Send Money Acknowledgement
							<div class="box-icon">
								<a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
								<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
							</div>
						</div>
			
						<div class="box-content">
						 <fieldset>
						 <table width="950" border="0" cellpadding="5" cellspacing="1" class="table table-striped table-bordered bootstrap-datatable ">
							<tr  class="even">
								 <td width="20%"> ${responseJSON.MESSAGE}</td>
								 
							</tr>
							<tr  class="even">
								 <td width="20%">
								 	<input class="btn btn-danger" type="submit" name="back" id="back" value="Next" /> &nbsp;&nbsp;&nbsp;
								 </td>
								 
							</tr>
						</table>
						</fieldset>
						</div>
						</div>
				</div>
				
			</div>
			</form>
</body>
</html>
